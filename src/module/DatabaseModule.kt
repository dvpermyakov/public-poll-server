package com.public.poll.module

import com.public.poll.table.*
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.transactions.transaction

fun databaseModule() {
    Database.connect(
        url = "jdbc:postgresql://127.0.0.1:5432/testdb",
        user = "dmitriip",
        password = ""
    )
    transaction {
        addLogger(StdOutSqlLogger)

        SchemaUtils.create(PollAnswerTable)
        SchemaUtils.create(PollEngagementTable)
        SchemaUtils.create(PollModerationResultTable)
        SchemaUtils.create(PollReportTable)
        SchemaUtils.create(PollTable)
        SchemaUtils.create(PollVoteTable)
        SchemaUtils.create(UserTable)
        SchemaUtils.create(PermissionTable)
    }
}