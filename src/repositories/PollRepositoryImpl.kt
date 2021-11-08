package com.public.poll.repositories

import com.public.poll.cache.Cache
import com.public.poll.dao.*
import com.public.poll.dto.CreatedPollDto
import com.public.poll.dto.PollDto
import com.public.poll.dto.UserDto
import com.public.poll.files.FileUploader
import com.public.poll.mapper.PollMapper
import com.public.poll.table.*
import com.public.poll.utils.toUUID
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.Op
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.DateTime
import java.util.*

class PollRepositoryImpl(
    private val pollMapper: PollMapper,
    private val pollSearchRepository: PollSearchRepository,
    private val pollBrokerRepository: PollBrokerRepository,
    private val cache: Cache,
    private val fileProvider: FileUploader
) : PollRepository {

    override fun createPoll(userDto: UserDto, createdPollDto: CreatedPollDto): PollDto {
        return transaction {
            val pollEntity = PollDao.new {
                created = DateTime.now()
                updated = DateTime.now()
                owner = UserDao.findById(getUserEntityId(userDto))!!
                status = PollStatus.CREATED
                question = createdPollDto.question
                engagementRequired = 10
            }
            createdPollDto.answers.forEach { answer ->
                PollAnswerDao.new {
                    poll = pollEntity
                    text = answer
                }
            }
            pollSearchRepository.addPollToSearch(
                id = pollEntity.id.value.toString(),
                question = pollEntity.question
            )
            pollBrokerRepository.addPoll(
                id = pollEntity.id.value.toString(),
                question = pollEntity.question
            )
            pollMapper.map(pollEntity).also { pollDto ->
                fileProvider.uploadFile(
                    name = "question_${pollDto.id}.txt",
                    data = pollDto.question.toByteArray(),
                    contentType = "text"
                )
                cache.putPoll(pollDto.id, pollDto)
            }
        }
    }

    override fun getPoll(pollId: String): PollRepository.GetPollResult {
        val pollUuid = getSafeUuid(pollId) ?: return PollRepository.GetPollResult.WrongIdFormat
        val pollFromCache = cache.getPoll(pollId)
        if (pollFromCache != null) {
            return PollRepository.GetPollResult.Success(pollFromCache)
        }
        return transaction {
            PollDao.findById(pollUuid)?.let { pollEntity ->
                PollRepository.GetPollResult.Success(pollMapper.map(pollEntity))
            } ?: PollRepository.GetPollResult.NotFound
        }
    }

    override fun editPoll(
        userDto: UserDto,
        pollId: String,
        createdPollDto: CreatedPollDto
    ): PollRepository.EditPollResult {
        val pollUuid = getSafeUuid(pollId) ?: return PollRepository.EditPollResult.WrongIdFormat

        return transaction {
            val pollEntity = PollDao.findById(pollUuid)
            when {
                pollEntity == null -> {
                    PollRepository.EditPollResult.WrongIdFormat
                }
                pollEntity.owner.id != getUserEntityId(userDto) -> {
                    PollRepository.EditPollResult.NoAccess
                }
                pollEntity.status == PollStatus.CREATED -> {
                    transaction {
                        pollEntity.updated = DateTime.now()
                        pollEntity.question = createdPollDto.question
                        pollEntity.answers.forEach { answerEntity ->
                            answerEntity.delete()
                        }
                        createdPollDto.answers.forEach { answer ->
                            PollAnswerDao.new {
                                poll = pollEntity
                                text = answer
                            }
                        }
                        PollRepository.EditPollResult.Success(
                            pollMapper.map(pollEntity).also { pollDto ->
                                cache.putPoll(pollDto.id, pollDto)
                            }
                        )
                    }
                }
                else -> PollRepository.EditPollResult.WrongStatus(pollEntity.status)
            }
        }
    }

    override fun createEngagement(userDto: UserDto, pollId: String): PollRepository.CreateEngagementResult {
        val pollUuid = getSafeUuid(pollId) ?: return PollRepository.CreateEngagementResult.WrongIdFormat
        val userId = getUserEntityId(userDto)

        val engagementCount = transaction {
            PollEngagementDao.count(Op.build {
                (PollEngagementTable.ownerId eq userId) and (PollEngagementTable.pollId eq pollUuid)
            })
        }
        return if (engagementCount == 0L) {
            transaction {
                val pollEntity = PollDao.findById(pollUuid)
                if (pollEntity != null) {
                    when {
                        pollEntity.status != PollStatus.APPROVED -> {
                            PollRepository.CreateEngagementResult.WrongStatus(pollEntity.status)
                        }
                        pollEntity.owner.id == userId -> {
                            PollRepository.CreateEngagementResult.OwnerCannotBeEngaged
                        }
                        else -> {
                            PollEngagementDao.new {
                                created = DateTime.now()
                                poll = pollEntity
                                owner = UserDao.findById(getUserEntityId(userDto))!!
                            }
                            PollRepository.CreateEngagementResult.Success
                        }
                    }
                } else PollRepository.CreateEngagementResult.PollNotFound
            }
        } else PollRepository.CreateEngagementResult.UserAlreadyEngaged
    }

    override fun createVote(userDto: UserDto, pollId: String, answerId: String): PollRepository.CreateVoteResult {
        val pollUuid = getSafeUuid(pollId) ?: return PollRepository.CreateVoteResult.WrongPollIdFormat
        val answerUuid = getSafeUuid(answerId) ?: return PollRepository.CreateVoteResult.WrongAnswerIdFormat
        val userId = getUserEntityId(userDto)

        val voteCount = transaction {
            PollVoteDao.count(Op.build {
                (PollVoteTable.ownerId eq userId) and (PollVoteTable.pollId eq pollUuid)
            })
        }
        return if (voteCount == 0L) {
            transaction {
                val pollEntity = PollDao.findById(pollUuid)
                if (pollEntity != null) {
                    if (pollEntity.status != PollStatus.ACTIVE) {
                        PollRepository.CreateVoteResult.WrongStatus(pollEntity.status)
                    } else {
                        val answerEntity = PollAnswerDao.findById(answerUuid)
                        if (answerEntity != null) {
                            PollVoteDao.new {
                                created = DateTime.now()
                                poll = pollEntity
                                owner = UserDao.findById(getUserEntityId(userDto))!!
                                answer = answerEntity
                            }
                            PollRepository.CreateVoteResult.Success
                        } else PollRepository.CreateVoteResult.AnswerNotFound
                    }
                } else PollRepository.CreateVoteResult.PollNotFound
            }
        } else PollRepository.CreateVoteResult.UserAlreadyVoted
    }

    override fun createReport(userDto: UserDto, pollId: String): PollRepository.CreateReportResult {
        val pollUuid = getSafeUuid(pollId) ?: return PollRepository.CreateReportResult.WrongIdFormat
        val userId = getUserEntityId(userDto)

        val reportCount = transaction {
            PollReportDao.count(Op.build {
                (PollReportTable.ownerId eq userId) and (PollReportTable.pollId eq pollUuid)
            })
        }
        return if (reportCount == 0L) {
            transaction {
                val pollEntity = PollDao.findById(pollUuid)
                if (pollEntity != null) {
                    PollReportDao.new {
                        created = DateTime.now()
                        poll = pollEntity
                        owner = UserDao.findById(userId)!!
                    }
                    PollRepository.CreateReportResult.Success
                } else PollRepository.CreateReportResult.PollNotFound
            }
        } else PollRepository.CreateReportResult.UserAlreadyReported
    }

    override fun approvePoll(userDto: UserDto, pollId: String): PollRepository.ApprovePollResult {
        val pollUuid = getSafeUuid(pollId) ?: return PollRepository.ApprovePollResult.WrongIdFormat
        val userId = getUserEntityId(userDto)
        val hasPermission = transaction {
            PermissionDao.count(Op.build {
                (PermissionTable.ownerId eq userId) and (PermissionTable.permission eq Permission.POLL_APPROVE)
            }) > 0
        }
        if (!hasPermission) {
            return PollRepository.ApprovePollResult.NoAccess
        }
        return transaction {
            val pollEntity = PollDao.findById(pollUuid)
            if (pollEntity != null) {
                if (pollEntity.status != PollStatus.CREATED) {
                    PollRepository.ApprovePollResult.WrongStatus(pollEntity.status)
                } else {
                    pollEntity.status = PollStatus.APPROVED
                    PollRepository.ApprovePollResult.Success
                }
            } else {
                PollRepository.ApprovePollResult.PollNotFound
            }
        }
    }

    private fun getSafeUuid(id: String): UUID? {
        return try {
            id.toUUID()
        } catch (ex: Exception) {
            return null
        }
    }

    private fun getUserEntityId(userDto: UserDto): EntityID<UUID> {
        return EntityID(userDto.id.toUUID(), UserTable)
    }
}