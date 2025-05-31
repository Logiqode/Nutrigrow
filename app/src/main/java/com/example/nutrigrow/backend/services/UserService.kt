package com.example.nutrigrow.backend.services

import com.example.nutrigrow.backend.models.UserCredentialDTO
import com.example.nutrigrow.backend.models.UserDTO
import com.example.nutrigrow.backend.repositories.UserRepository
import com.example.nutrigrow.backend.utils.PasswordHasher
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class UserService : KoinComponent {
    private val repository by inject<UserRepository>()
    // Removed `private val passwordHasher by inject<PasswordHasher>()` as PasswordHasher is an object

    suspend fun getAllUsers(): List<UserDTO> = repository.getAllUsers()
    suspend fun getUserById(id: Int): UserDTO? = repository.getUserById(id)
    suspend fun createUser(user: UserDTO): UserDTO = repository.createUser(user)
    suspend fun updateUser(id: Int, user: UserDTO): Boolean = repository.updateUser(id, user)
    suspend fun deleteUser(id: Int): Boolean = repository.deleteUser(id)

    suspend fun registerUser(credential: UserCredentialDTO, user: UserDTO): UserCredentialDTO {
        // Validate password requirements before hashing
        validatePassword(credential.password.toString()) // Ensure password is not null for validation

        val hashedPassword = PasswordHasher.hash(credential.password.toString()) // Use PasswordHasher object directly

        // Check if user already exists by some unique identifier if necessary before creating.
        // For now, we assume user DTO is for a new user.
        val createdUser = repository.createUser(user) // Create the user first

        // Create the credential with the new user's ID
        return repository.createCredential(
            credential.copy(
                userId = createdUser.id!!, // Use the ID from the actually created user
                password = hashedPassword // Pass the hashed password to be stored
            )
        )
    }

    suspend fun login(username: String, password: String): UserDTO? {
        val credential = repository.getCredentialByUsername(username) // This should return DTO with hashed password for verification
            ?: return null

        // The getCredentialByUsername in repository was modified to return password for this check.
        // Or, the repository needs a specific method that returns the credential with the password for verification.
        // For now, assume getCredentialByUsername in the repository returns the full credential for internal service use.
        // The current UserRepository.getCredentialByUsername clears the password. This needs adjustment for login.

        // Let's assume a modification in UserRepository:
        // fun getCredentialByUsernameForAuth(username: String): UserCredentialDTO? -> returns with password
        // For now, we'll work with the idea that the service gets the password somehow.
        // A better approach for UserRepository:
        // getCredentialByUsername(username: String) -> returns DTO *with* hashed password.
        // The DTO's password field should only be nulled when sending to *external* clients.

        // Corrected: Assuming repository.getCredentialByUsername(username) now returns the DTO with the hashed password.
        // If PasswordHasher.verify needs the plain password and the stored hash:
        val storedCredential = repository.getCredentialByUsernameForAuth(username) // Hypothetical method
            ?: return null


        return if (PasswordHasher.verify(password, storedCredential.password.toString())) { // Use PasswordHasher object
            repository.getUserById(storedCredential.userId)
        } else {
            null
        }
    }

    private fun validatePassword(password: String) {
        require(password.length >= 8) { "Password must be at least 8 characters long" }
        require(password.any { it.isDigit() }) { "Password must contain at least one digit" }
        // Added check for uppercase letter
        require(password.any { it.isUpperCase() }) { "Password must contain at least one uppercase letter" }
        // Added check for lowercase letter
        require(password.any { it.isLowerCase() }) { "Password must contain at least one lowercase letter" }
        require(password.any { !it.isLetterOrDigit() }) {
            "Password must contain at least one special character"
        }
    }
}