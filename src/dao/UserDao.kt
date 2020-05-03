package com.public.poll.dao

import com.public.poll.table.UserTable
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import java.util.*

class UserDao(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<UserDao>(UserTable)

    var created by UserTable.created
    var name by UserTable.name
    var password by UserTable.password
    var email by UserTable.email
}