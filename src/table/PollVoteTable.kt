package com.public.poll.table

import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.jodatime.datetime
import org.joda.time.DateTime
import java.util.*

object PollVoteTable : UUIDTable() {
    val created: Column<DateTime> = datetime("created")
    val pollId: Column<UUID> = uuid("poll_id").references(PollTable.id)
    val answerId: Column<UUID> = uuid("answer_id").references(PollAnswerTable.id)
    val ownerId: Column<UUID> = PollTable.uuid("owner_id").references(UserTable.id)
}