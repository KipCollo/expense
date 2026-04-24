package com.example.expense.util

import java.security.spec.KeySpec
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec

object PasswordUtils {

    private const val ITERATIONS = 65536
    private const val KEY_LENGTH = 256

    /**
     * Hashes [password] using PBKDF2WithHmacSHA1 with [salt] as the per-user salt.
     * Using the user's email as the salt ensures that equal passwords produce
     * different hashes for different accounts, preventing basic rainbow-table attacks.
     */
    fun hash(password: String, salt: String): String {
        val spec: KeySpec = PBEKeySpec(
            password.toCharArray(),
            salt.toByteArray(Charsets.UTF_8),
            ITERATIONS,
            KEY_LENGTH
        )
        val factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1")
        val bytes = factory.generateSecret(spec).encoded
        return bytes.joinToString("") { "%02x".format(it) }
    }
}
