package com.example.hobbyhub.data

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth // <-- UPDATED IMPORT
import io.github.jan.supabase.auth.providers.builtin.Email
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Repository for handling authentication-related operations.
 *
 * This class provides methods for signing up and logging in users with Supabase.
 */
class AuthRepository @Inject constructor(private val supabase: SupabaseClient) {

    /**
     * Signs up a new user with the provided email and password.
     *
     * @param email The user's email address.
     * @param password The user's password.
     */
    suspend fun signUp(email: String, password: String) {
        withContext(Dispatchers.IO) {
            supabase.auth.signUpWith(Email) { // <-- UPDATED USAGE
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
            supabase.auth.signInWith(Email) { // <-- UPDATED USAGE
                this.email = email
                this.password = password
            }
        }
    }
}