package com.example.hobbyhub

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.example.hobbyhub.core.navigations.SetupNavGraph
import com.example.hobbyhub.ui.theme.HobbyHubTheme
import dagger.hilt.android.AndroidEntryPoint
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.gotrue.GoTrue
import io.github.jan.supabase.postgrest.Postgrest


val supabase = createSupabaseClient(
    supabaseUrl = "https://klvcpnhhaohrqxwpuads.supabase.co",
    supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImtsdmNwbmhoYW9ocnF4d3B1YWRzIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NjAxMDM3NDcsImV4cCI6MjA3NTY3OTc0N30.XI2ar3fCwBJTdtMMBfmAIxNgfniXjJLKXaFboL7CWhc"
) {
    install(GoTrue)
    install(Postgrest)
    //install other modules
}

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HobbyHubTheme {
                val navController = rememberNavController()
                SetupNavGraph(navController = navController)
            }
        }
    }
}