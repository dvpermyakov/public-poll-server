package com.public.poll.dao

import com.public.poll.table.PollVoteTable
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import java.util.*

class PollVoteDao(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<PollVoteDao>(PollVoteTable)

    var created by PollVoteTable.created
    var poll by PollDao referencedOn PollVoteTable.pollId
    var owner by UserDao referencedOn PollVoteTable.ownerId
    var answer by PollAnswerDao referencedOn PollVoteTable.answerId
}