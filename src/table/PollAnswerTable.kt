package com.public.poll.table

import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.Column
import java.util.*

object PollAnswerTable : UUIDTable() {
    val pollId: Column<UUID> = uuid("poll_id").references(PollTable.id).index()
    val text: Column<String> = text("text")
}