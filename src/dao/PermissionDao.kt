package com.public.poll.dao

import com.public.poll.table.PermissionTable
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import java.util.*

class PermissionDao(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<PermissionDao>(PermissionTable)

    var owner by UserDao referencedOn PermissionTable.ownerId
    var permission by PermissionTable.permission
}