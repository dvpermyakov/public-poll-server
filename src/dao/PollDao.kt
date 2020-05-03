package com.public.poll.dao

import com.public.poll.table.PollTable
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import java.util.*

class PollDao(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<PollDao>(PollTable)

    var created by PollTable.created
    var updated by PollTable.updated
    var owner by UserDao referencedOn PollTable.ownerId
    var status by PollTable.status
    var question by PollTable.question
    val participantsRequired by PollTable.participantsRequired
}