package com.public.poll.repositories

import com.public.poll.dao.PollAnswerDao
import com.public.poll.dao.PollDao
import com.public.poll.dao.UserDao
import com.public.poll.dto.CreatedPollDto
import com.public.poll.dto.PollDto
import com.public.poll.dto.UserDto
import com.public.poll.mapper.PollMapper
import com.public.poll.table.PollStatus
import com.public.poll.table.UserTable
import com.public.poll.utils.toUUID
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.DateTime
import java.util.*

class PollRepositoryImpl(
    private val pollMapper: PollMapper
) : PollRepository {

    override fun createPoll(userDto: UserDto, createdPollDto: CreatedPollDto): PollDto {
        val userId = EntityID(userDto.id.toUUID(), UserTable)

        return transaction {
            val pollEntity = PollDao.new {
                created = DateTime.now()
                updated = DateTime.now()
                owner = UserDao.findById(userId)!!
                status = PollStatus.APPROVED
                question = createdPollDto.question
                engagementRequired = 10
            }
            createdPollDto.answers.forEach { answer ->
                PollAnswerDao.new {
                    poll = pollEntity
                    text = answer
                }
            }
            pollMapper.map(pollEntity)
        }
    }

    override fun getPoll(pollId: String): PollRepository.GetPollResult {
        val pollUuid = getPollUuid(pollId) ?: return PollRepository.GetPollResult.WrongIdFormat
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
        val pollUuid = getPollUuid(pollId) ?: return PollRepository.EditPollResult.WrongIdFormat
        val userId = EntityID(userDto.id.toUUID(), UserTable)

        return transaction {
            val pollEntity = PollDao.findById(pollUuid)
            when {
                pollEntity == null -> {
                    PollRepository.EditPollResult.WrongIdFormat
                }
                pollEntity.owner.id != userId -> {
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
                            pollMapper.map(pollEntity)
                        )
                    }
                }
                else -> {
                    PollRepository.EditPollResult.WrongStatus(pollEntity.status)
                }
            }
        }
    }

    private fun getPollUuid(pollId: String): UUID? {
        return try {
            pollId.toUUID()
        } catch (ex: Exception) {
            return null
        }
    }
}