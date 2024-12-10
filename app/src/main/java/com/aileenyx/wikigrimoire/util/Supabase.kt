package com.aileenyx.wikigrimoire.util

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

val dotenv = dotenv {
    directory = "/assets"
    filename = "env"
}

val supabaseUrl: String = dotenv["SUPABASE_URL"]
val supabaseKey: String = dotenv["SUPABASE_KEY"]
val webGoogleClientId: String = dotenv["WEB_GOOGLE_CLIENT_ID"]
var userSingedIn: Boolean = false

val supabase = createSupabaseClient(supabaseUrl, supabaseKey) {
    install(Auth)
    install(Postgrest)
}

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

@Composable
fun GoogleSignInButton() {
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    val onClick: () -> Unit = {
        val credentialManager = androidx.credentials.CredentialManager.create(context)

        // Generate a nonce and hash it with sha-256
        // Providing a nonce is optional but recommended
        val rawNonce = UUID.randomUUID().toString() // Generate a random String. UUID should be sufficient, but can also be any other random string.
        val bytes = rawNonce.toString().toByteArray()
        val md = MessageDigest.getInstance("SHA-256")
        val digest = md.digest(bytes)
        val hashedNonce = digest.fold("") { str, it -> str + "%02x".format(it) } // Hashed nonce to be passed to Google sign-in


        val googleIdOption: GetGoogleIdOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(false)
            .setServerClientId(webGoogleClientId)
            .setNonce(hashedNonce) // Provide the nonce if you have one
            .build()

        val request: GetCredentialRequest = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()

        coroutineScope.launch {
            try {
                val result = credentialManager.getCredential(
                    request = request,
                    context = context,
                )

                val googleIdTokenCredential = GoogleIdTokenCredential
                    .createFrom(result.credential.data)

                val googleIdToken = googleIdTokenCredential.idToken

                supabase.auth.signInWith(IDToken) {
                    idToken = googleIdToken
                    provider = Google
                    nonce = rawNonce
                }

                val session = supabase.auth.currentSessionOrNull()
                print(session)
                userSingedIn = true
            } catch (e: GetCredentialException) {
                // Handle GetCredentialException thrown by `credentialManager.getCredential()`
            } catch (e: GoogleIdTokenParsingException) {
                // Handle GoogleIdTokenParsingException thrown by `GoogleIdTokenCredential.createFrom()`
            } catch (e: RestException) {
                // Handle RestException thrown by Supabase
            } catch (e: Exception) {
                // Handle unknown exceptions
            }
        }
    }

    Button(
        onClick = onClick,
    ) {
        Text("Sign in with Google")
    }
}

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

fun signOut() {
    runBlocking {
        supabase.auth.signOut()
    }
    userSingedIn = false
}