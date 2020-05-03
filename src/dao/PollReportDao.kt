package com.public.poll.dao

import com.public.poll.table.PollReportTable
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import java.util.*

class PollReportDao(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<PollReportDao>(PollReportTable)

    var created by PollReportTable.created
    var poll by PollDao referencedOn PollReportTable.pollId
    var owner by UserDao referencedOn PollReportTable.ownerId
}