package com.public.poll.dao

import com.public.poll.table.PollModerationResultTable
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import java.util.*

class PollModerationResultDao(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<PollModerationResultDao>(PollModerationResultTable)

    var created by PollModerationResultTable.created
    var poll by PollDao referencedOn PollModerationResultTable.pollId
    var approved by PollModerationResultTable.approved
    var reason by PollModerationResultTable.reason
}