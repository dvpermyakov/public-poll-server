package com.public.poll.table

import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.jodatime.datetime
import org.joda.time.DateTime
import java.util.*

object PollTable : UUIDTable() {
    val created: Column<DateTime> = datetime("created")
    val updated: Column<DateTime> = datetime("updated")
    val ownerId: Column<EntityID<UUID>> = reference("owner_id", UserTable)
    val status: Column<PollStatus> = enumeration("status", PollStatus::class)
    val question: Column<String> = text("question")
    val participantsRequired: Column<Int> = integer("participants_required")
}