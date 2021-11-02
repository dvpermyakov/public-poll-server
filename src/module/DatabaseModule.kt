package com.public.poll.module

import com.public.poll.table.*
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*

fun databaseModule() {
    val connProps = Properties().apply {
        setProperty("user", System.getenv("SQL_DATABASE_USER"))
        setProperty("password", System.getenv("SQL_DATABASE_PASSWORD"))
        setProperty("cloudSqlInstance", System.getenv("SQL_DATABASE_INSTANCE"))
        setProperty("sslmode", "disable")
        setProperty("socketFactory", "com.google.cloud.sql.postgres.SocketFactory")
        setProperty("enableIamAuth", "true")
    }
    val config = HikariConfig().apply {
        jdbcUrl = "jdbc:postgresql:///testdb"
        dataSourceProperties = connProps
        connectionTimeout = 10000 // 10s
    }
    Database.connect(HikariDataSource(config))

    transaction {
        addLogger(StdOutSqlLogger)
        SchemaUtils.create(
            PollAnswerTable,
            PollEngagementTable,
            PollModerationResultTable,
            PollReportTable,
            PollTable,
            PollVoteTable,
            UserTable,
            PermissionTable
        )
    }
}