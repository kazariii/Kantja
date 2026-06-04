package com.example.finalprojectpam.data.remote

import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest

object SupabaseClient {
    private const val SUPABASE_URL = "https://cwlabcetduatbtyimhbh.supabase.co"
    private const val SUPABASE_KEY = "sb_publishable_cv7ZW6Sfz2pCavT7r8gzsw_YagGqfLN"

    val client = createSupabaseClient(
        supabaseUrl = SUPABASE_URL,
        supabaseKey = SUPABASE_KEY
    ) {
        install(Postgrest)
    }
}
