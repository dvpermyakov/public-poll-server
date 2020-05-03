package com.public.poll.table

import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.Column
import java.util.*

object PollAnswerTable : UUIDTable() {
    val pollId: Column<EntityID<UUID>> = reference("poll_id", PollTable).index()
    val text: Column<String> = text("text")
}