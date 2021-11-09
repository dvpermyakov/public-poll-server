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
    }
    val socketFactory = System.getenv("SQL_DATABASE_SOCKET_FACTORY")
    if (socketFactory.isNotBlank()) {
        connProps.setProperty("socketFactory", socketFactory)
        connProps.setProperty("cloudSqlInstance", System.getenv("SQL_DATABASE_CLOUD_INSTANCE"))
        connProps.setProperty("sslmode", "disable")
        connProps.setProperty("enableIamAuth", "true")
    }
    val databaseHost = System.getenv("SQL_DATABASE_HOST")
    val databaseName = System.getenv("SQL_DATABASE_NAME")
    val config = HikariConfig().apply {
        jdbcUrl = "jdbc:postgresql://$databaseHost/$databaseName"
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