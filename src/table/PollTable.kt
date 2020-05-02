package com.public.poll.table

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Column

object PollTable : IntIdTable() {
    val question: Column<String> = varchar("question", length = 50)
    val answers: Column<String> = varchar("answers", length = 255)
}