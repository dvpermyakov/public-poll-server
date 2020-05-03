package com.public.poll.table

import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.jodatime.datetime
import org.joda.time.DateTime
import java.util.*

object PollModerationResultTable : UUIDTable() {
    val created: Column<DateTime> = datetime("created")
    val pollId: Column<UUID> = uuid("poll_id").references(PollTable.id).index()
    val approved: Column<Boolean> = bool("approved")
    val reason: Column<String?> = text("reason").nullable()
}