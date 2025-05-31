package com.example.nutrigrow.backend.models

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.javatime.date
import org.jetbrains.exposed.sql.javatime.datetime
import java.time.LocalDateTime

object Users : IntIdTable("users") {
    val name = varchar("nama_user", 255)
    val address = varchar("alamat_user", 255).nullable()
    val phone = varchar("no_telepon_user", 13).nullable()
    val gender = char("jenis_kelamin").nullable()
    val birthDate = date("tanggal_lahir").nullable()
    val profilePicture = varchar("profile_picture", 255).nullable()
    val createdAt = datetime("created_at").clientDefault { LocalDateTime.now() }
    val updatedAt = datetime("updated_at").nullable()
}

@Serializable
data class UserDTO(
    val id: Int? = null,
    val name: String,
    val address: String? = null,
    val phone: String? = null,
    val gender: Char? = null,
    val birthDate: String? = null,
    val profilePicture: String? = null,
    val createdAt: String? = null,
    val updatedAt: String? = null
)

fun ResultRow.toUserDTO() = UserDTO(
    id = this[Users.id].value,
    name = this[Users.name],
    address = this[Users.address],
    phone = this[Users.phone],
    gender = this[Users.gender],
    birthDate = this[Users.birthDate]?.toString(),
    profilePicture = this[Users.profilePicture],
    createdAt = this[Users.createdAt].toString(),
    updatedAt = this[Users.updatedAt]?.toString()
)