package com.example.hobbyhub.data

import com.example.hobbyhub.supabase
import io.github.jan.supabase.gotrue.gotrue
import io.github.jan.supabase.gotrue.providers.builtin.Email
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Repository for handling authentication-related operations.
 *
 * This class provides methods for signing up and logging in users with Supabase.
 */
class AuthRepository {

    /**
     * Signs up a new user with the provided email and password.
     *
     * @param email The user's email address.
     * @param password The user's password.
     */
    suspend fun signUp(email: String, password: String) {
        withContext(Dispatchers.IO) {
            supabase.gotrue.signUpWith(Email) {
                this.email = email
                this.password = password
            }
        }
    }

    /**
     * Logs in a user with the provided email and password.
     *
     * @param email The user's email address.
     * @param password The user's password.
     */
    suspend fun login(email: String, password: String) {
        withContext(Dispatchers.IO) {
            supabase.gotrue.signInWith(Email) {
                this.email = email
                this.password = password
            }
        }
    }
}