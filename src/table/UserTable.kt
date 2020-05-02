package com.public.poll.table

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Column

object UserTable : IntIdTable() {
    val name: Column<String> = varchar("name", length = 50).uniqueIndex()
    val password: Column<String> = varchar("password", length = 256)
    val email: Column<String> = varchar("email", length = 50).uniqueIndex()
}