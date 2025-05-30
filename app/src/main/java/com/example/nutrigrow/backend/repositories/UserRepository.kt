package com.example.nutrigrow.backend.repositories

import com.example.nutrigrow.backend.models.UserCredentialDTO
import com.example.nutrigrow.backend.models.UserCredentials
import com.example.nutrigrow.backend.models.UserDTO
import com.example.nutrigrow.backend.models.Users
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import com.example.nutrigrow.backend.models.toUserCredentialDTO
import com.example.nutrigrow.backend.models.toUserDTO
// import com.example.nutrigrow.backend.utils.PasswordHasher // Not used directly here for hashing if service does it
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.*

class UserRepository {
    suspend fun getAllUsers(): List<UserDTO> = newSuspendedTransaction {
        Users.selectAll().map { it.toUserDTO() }
    }

    suspend fun getUserById(id: Int): UserDTO? = newSuspendedTransaction {
        Users.select { Users.id eq id }.singleOrNull()?.toUserDTO()
    }

    suspend fun createUser(user: UserDTO): UserDTO = newSuspendedTransaction {
        val id = Users.insert {
            it[name] = user.name
            it[address] = user.address
            it[phone] = user.phone
            it[gender] = user.gender
            it[birthDate] = user.birthDate?.let { bd -> java.time.LocalDate.parse(bd) } // Parse date string
        } get Users.id

        user.copy(id = id.value)
    }

    suspend fun updateUser(id: Int, user: UserDTO): Boolean = newSuspendedTransaction {
        Users.update({ Users.id eq id }) {
            it[name] = user.name
            it[address] = user.address
            it[phone] = user.phone
            it[gender] = user.gender
            it[birthDate] = user.birthDate?.let { bd -> java.time.LocalDate.parse(bd) }
        } > 0
    }

    suspend fun deleteUser(id: Int): Boolean = newSuspendedTransaction {
        Users.deleteWhere { Users.id eq id } > 0
    }

    suspend fun createCredential(credential: UserCredentialDTO): UserCredentialDTO = newSuspendedTransaction {
        // Assumes credential.password is already hashed by the service
        val id = UserCredentials.insert {
            it[email] = credential.email
            it[password] = credential.password.toString() // Store the pre-hashed password
            it[username] = credential.username
            it[user] = credential.userId
        } get UserCredentials.id

        credential.copy(id = id.value) // Password is not re-hashed or cleared here, service handles that
    }

    // This version returns the credential with the hashed password for internal authentication use
    suspend fun getCredentialByUsernameForAuth(username: String): UserCredentialDTO? = newSuspendedTransaction {
        UserCredentials.select { UserCredentials.username eq username }
            .singleOrNull()
            ?.toUserCredentialDTO() // This DTO must contain the hashed password
    }

    // This version can be used if you need to expose credential info without the password
    suspend fun getCredentialByUsername(username: String): UserCredentialDTO? = newSuspendedTransaction {
        UserCredentials.select { UserCredentials.username eq username }
            .singleOrNull()
            ?.toUserCredentialDTO()
            ?.copy(password = null) // Clear password for general DTO usage
    }
}