package com.public.poll.dao

import com.public.poll.table.PollEngagementTable
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import java.util.*

class PollEngagementDao(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<PollEngagementDao>(PollEngagementTable)

    var created by PollEngagementTable.created
    var poll by PollDao referencedOn PollEngagementTable.pollId
    var owner by UserDao referencedOn PollEngagementTable.ownerId
}