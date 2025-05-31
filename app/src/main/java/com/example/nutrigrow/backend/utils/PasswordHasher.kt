package com.example.nutrigrow.backend.utils

import at.favre.lib.crypto.bcrypt.BCrypt

object PasswordHasher {
    private const val BCRYPT_COST = 12 // Security parameter (10-14 is typical)

    fun hash(password: String): String {
        require(password.isNotBlank()) { "Password cannot be blank" }
        return BCrypt.withDefaults().hashToString(BCRYPT_COST, password.toCharArray())
    }

    fun verify(password: String, hash: String): Boolean {
        require(password.isNotBlank()) { "Password cannot be blank" }
        require(hash.isNotBlank()) { "Hash cannot be blank" }
        return BCrypt.verifyer().verify(password.toCharArray(), hash).verified
    }
}