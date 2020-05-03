package com.public.poll.table

import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.jodatime.datetime
import org.joda.time.DateTime
import java.util.*

object PollTable : UUIDTable() {
    val created: Column<DateTime> = datetime("created")
    val updated: Column<DateTime> = datetime("updated")
    val ownerId: Column<UUID> = uuid("owner_id").references(UserTable.id)
    val status: Column<PollStatus> = enumeration("status", PollStatus::class)
    val question: Column<String> = text("question")
    val participantsRequired: Column<Int> = integer("participants_required")
}