package com.public.poll.dao

import com.public.poll.table.PollDislikeTable
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import java.util.*

class PollDislikeDao(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<PollDislikeDao>(PollDislikeTable)

    var created by PollDislikeTable.created
    var poll by PollDao referencedOn PollDislikeTable.pollId
    var owner by UserDao referencedOn PollDislikeTable.ownerId
}