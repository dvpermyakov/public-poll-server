package com.public.poll.table

import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.Column
import java.util.*

object PermissionTable : UUIDTable() {
    val ownerId: Column<EntityID<UUID>> = reference("owner_id", UserTable)
    val permission: Column<Permission> = enumeration("permission", Permission::class)
}