package com.example.nutrigrow.backend.models

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.ResultRow

object UserCredentials : IntIdTable("user_credentials") {
    val email    = varchar("email_credential", 255)
    val password = varchar("password_credential", 255)
    val username = varchar("username", 255)
    val user     = reference("user_id_user", Users, onDelete = ReferenceOption.CASCADE)
}

@Serializable
data class UserCredentialDTO(
    val id: Int? = null,
    val email: String,
    val password: String,
    val username: String,
    val userId: Int
)

fun ResultRow.toUserCredentialDTO() = UserCredentialDTO(
    id       = this[UserCredentials.id].value,
    email    = this[UserCredentials.email],
    password = this[UserCredentials.password],
    username = this[UserCredentials.username],
    userId   = this[UserCredentials.user].value
)