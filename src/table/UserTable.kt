package com.public.poll.table

import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.jodatime.datetime
import org.joda.time.DateTime

object UserTable : UUIDTable() {
    val created: Column<DateTime> = datetime("created")
    val name: Column<String> = varchar("name", length = 50)
    val password: Column<String> = text("password")
    val email: Column<String> = varchar("email", length = 50).uniqueIndex()
}