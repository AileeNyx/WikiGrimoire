package com.aileenyx.wikigrimoire.util

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialException
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.cdimascio.dotenv.dotenv
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.Google
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.auth.providers.builtin.IDToken
import io.github.jan.supabase.exceptions.RestException
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.security.MessageDigest
import java.util.UUID

//Variables de entorno
val dotenv = dotenv {
    directory = "/assets"
    filename = "env"
}

val supabaseUrl: String = dotenv["SUPABASE_URL"]
val supabaseKey: String = dotenv["SUPABASE_KEY"]
val webGoogleClientId: String = dotenv["WEB_GOOGLE_CLIENT_ID"]

val supabase = createSupabaseClient(supabaseUrl, supabaseKey) {
    install(Auth)
    install(Postgrest)
}

//Iniciado y creación de Sesión
suspend fun signUpNewUser(newEmail: String, newPassword: String) {
    supabase.auth.signUpWith(Email) {
        email = newEmail
        password = newPassword
    }
}

suspend fun signInWithEmail(userEmail: String, userPassword: String) {
    supabase.auth.signInWith(Email) {
        email = userEmail
        password = userPassword
    }
}

//Getters de Información
fun isActiveSession(): Boolean {
    val session = supabase.auth.currentSessionOrNull()
    return session != null
}

fun getUsername(): String? {
    if (isActiveSession()) {
        val supabaseClient = supabase
        var username: String? = null
        runBlocking {
            val user = supabase.auth.retrieveUserForCurrentSession(updateSession = true)
            username = user.email
        }
        return username
    } else {
        return null
    }
}

fun getUserId() :String? {
    if (isActiveSession()) {
        var userId: String? = null
        runBlocking {
            val user = supabase.auth.retrieveUserForCurrentSession(updateSession = true)
            userId = user.id
        }
        return userId
    } else {
        return null
    }
}

//Guardado de la sesión
fun storeSessionToken(context: Context, token: String) {
    val sharedPreferences: SharedPreferences = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
    with(sharedPreferences.edit()) {
        putString("session_token", token)
        apply()
    }
}

fun getSessionToken(context: Context): String? {
    val sharedPreferences: SharedPreferences = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
    return sharedPreferences.getString("session_token", null)
}

fun clearSessionToken(context: Context) {
    val sharedPreferences: SharedPreferences = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
    with(sharedPreferences.edit()) {
        remove("session_token")
        apply()
    }
}

//Cerrar sesión
fun signOut(context: Context) {
    runBlocking {
        supabase.auth.signOut()
    }
    clearSessionToken(context)
}

