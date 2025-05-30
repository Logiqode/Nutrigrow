package com.example.nutrigrow.backend.models

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ResultRow

object Users : IntIdTable("users") {
    val name      = varchar("nama_user", 255)
    val address   = varchar("alamat_user", 255)
    val phone     = varchar("no_telepon_user", 13)
    val gender    = char("jenis_kelamin")
}

@Serializable
data class UserDTO(
    val id: Int? = null,
    val name: String,
    val address: String,
    val phone: String,
    val gender: Char
)

fun ResultRow.toUserDTO() = UserDTO(
    id      = this[Users.id].value,
    name    = this[Users.name],
    address = this[Users.address],
    phone   = this[Users.phone],
    gender  = this[Users.gender]
)