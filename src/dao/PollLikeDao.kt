package com.public.poll.dao

import com.public.poll.table.PollLikeTable
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import java.util.*

class PollLikeDao(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<PollLikeDao>(PollLikeTable)

    var created by PollLikeTable.created
    var poll by PollDao referencedOn PollLikeTable.pollId
    var owner by UserDao referencedOn PollLikeTable.ownerId
}