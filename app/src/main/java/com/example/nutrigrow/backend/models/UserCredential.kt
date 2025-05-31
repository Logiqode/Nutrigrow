package com.example.nutrigrow.backend.models

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.javatime.datetime
import java.time.LocalDateTime

object UserCredentials : IntIdTable("user_credentials") {
    val email = varchar("email_credential", 255).uniqueIndex()
    val password = varchar("password_credential", 255)
    val username = varchar("username", 255).uniqueIndex()
    val isVerified = bool("is_verified").default(false)
    val verificationToken = varchar("verification_token", 255).nullable()
    val tokenExpiresAt = datetime("token_expires_at").nullable()
    val lastLogin = datetime("last_login").nullable()
    val user = reference("user_id_user", Users, onDelete = ReferenceOption.CASCADE)
    val createdAt = datetime("created_at").clientDefault { LocalDateTime.now() }
    val updatedAt = datetime("updated_at").nullable()
}

@Serializable
data class UserCredentialDTO(
    val id: Int? = null,
    val email: String,
    val password: String? = null, // Should be null when returning to client
    val username: String,
    val isVerified: Boolean = false,
    val verificationToken: String? = null,
    val tokenExpiresAt: String? = null,
    val lastLogin: String? = null,
    val userId: Int,
    val createdAt: String? = null,
    val updatedAt: String? = null
)

fun ResultRow.toUserCredentialDTO() = UserCredentialDTO(
    id = this[UserCredentials.id].value,
    email = this[UserCredentials.email],
    password = this[UserCredentials.password],
    username = this[UserCredentials.username],
    isVerified = this[UserCredentials.isVerified],
    verificationToken = this[UserCredentials.verificationToken],
    tokenExpiresAt = this[UserCredentials.tokenExpiresAt]?.toString(),
    lastLogin = this[UserCredentials.lastLogin]?.toString(),
    userId = this[UserCredentials.user].value,
    createdAt = this[UserCredentials.createdAt].toString(),
    updatedAt = this[UserCredentials.updatedAt]?.toString()
)