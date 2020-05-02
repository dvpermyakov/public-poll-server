package com.public.poll.dao

import com.public.poll.table.PollTable
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class PollDao(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<PollDao>(PollTable)

    var question by PollTable.question
    var answers by PollTable.answers
}