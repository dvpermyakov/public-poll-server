package com.public.poll.repositories

import com.public.poll.dao.PollDao
import com.public.poll.dto.PollCollectionDto
import com.public.poll.dto.UserDto
import com.public.poll.mapper.PollMapper
import com.public.poll.table.PollStatus
import com.public.poll.table.PollTable
import com.public.poll.table.UserTable
import com.public.poll.utils.toUUID
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.or
import org.jetbrains.exposed.sql.transactions.transaction

class PollCollectionRepositoryImpl(
    private val pollMapper: PollMapper
) : PollCollectionRepository {

    override fun getPollsByUser(userDto: UserDto): PollCollectionDto {
        val userId = EntityID(userDto.id.toUUID(), UserTable)

        return transaction {
            PollCollectionDto(items = PollDao
                .find { PollTable.ownerId eq userId }
                .sortedByDescending { PollTable.created }
                .map { pollEntity ->
                    pollMapper.map(pollEntity)
                }
            )
        }
    }

    override fun getFeed(): PollCollectionDto {
        return transaction {
            PollCollectionDto(items = PollDao
                .find {
                    (PollTable.status eq PollStatus.APPROVED) or (PollTable.status eq PollStatus.ACTIVE)
                }
                .sortedByDescending { PollTable.created }
                .map { pollEntity ->
                    pollMapper.map(pollEntity)
                }
            )
        }
    }

    override fun getPollsByIds(ids: List<String>): PollCollectionDto {
        val pollIds = ids.map { it.toUUID() }
        return transaction {
            PollCollectionDto(items = PollDao
                .find {
                    (PollTable.id inList pollIds)
                }
                .map { pollEntity ->
                    pollMapper.map(pollEntity)
                }
            )
        }
    }
}