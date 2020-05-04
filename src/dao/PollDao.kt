package com.public.poll.dao

import com.public.poll.table.PollAnswerTable
import com.public.poll.table.PollEngagementTable
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
    var engagementRequired by PollTable.engagementRequired

    val answers by PollAnswerDao referrersOn PollAnswerTable.pollId
    val engagementCount by PollEngagementDao referrersOn PollEngagementTable.pollId
}