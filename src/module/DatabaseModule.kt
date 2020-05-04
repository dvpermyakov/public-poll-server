package com.public.poll.module

import com.public.poll.table.*
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.transactions.transaction

fun databaseModule() {
    Database.connect(
        url = "jdbc:h2:~/test",
        driver = "org.h2.Driver",
        user = "root",
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