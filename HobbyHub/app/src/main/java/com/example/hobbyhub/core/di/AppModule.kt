package com.example.hobbyhub.core.di

import com.example.hobbyhub.data.AuthRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.auth.Auth // <-- UPDATED IMPORT
import io.github.jan.supabase.postgrest.Postgrest
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideSupabaseClient(): SupabaseClient {
        return createSupabaseClient(
            supabaseUrl = "https://klvcpnhhaohrqxwpuads.supabase.co",
            supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImtsdmNwbmhoYW9ocnF4d3B1YWRzIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NjAxMDM3NDcsImV4cCI6MjA3NTY3OTc0N30.XI2ar3fCwBJTdtMMBfmAIxNgfniXjJLKXaFboL7CWhc"
        ) {
            install(Auth) // <-- UPDATED MODULE
            install(Postgrest)
            //install other modules
        }
    }

    @Provides
    @Singleton
    fun provideAuthRepository(supabaseClient: SupabaseClient): AuthRepository {
        return AuthRepository(supabaseClient)
    }
}