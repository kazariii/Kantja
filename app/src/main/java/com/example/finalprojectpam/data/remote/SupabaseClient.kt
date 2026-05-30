package com.example.finalprojectpam.data.remote

import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest

object SupabaseClient {
    private const val SUPABASE_URL = "https://wtkkjhpwxlzcemarqwdy.supabase.co/rest/v1/"
    private const val SUPABASE_KEY = "sb_publishable_UBLKNV_1ympe0pRI6eukjQ_ZeaWDImK"

    val client = createSupabaseClient(
        supabaseUrl = SUPABASE_URL,
        supabaseKey = SUPABASE_KEY
    ) {
        install(Auth)
        install(Postgrest)
    }
}
