package com.public.poll.dao

import com.public.poll.table.PollAnswerTable
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import java.util.*

class PollAnswerDao(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<PollAnswerDao>(PollAnswerTable)

    var poll by PollDao referencedOn PollAnswerTable.pollId
    var text by PollAnswerTable.text
}