package com.aileenyx.wikigrimoire

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.media3.common.util.Log
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.aileenyx.wikigrimoire.components.LocalNavController
import com.aileenyx.wikigrimoire.components.NavControllerProvider
import com.aileenyx.wikigrimoire.screens.CreateScreen
import com.aileenyx.wikigrimoire.screens.HomeScreen
import com.aileenyx.wikigrimoire.screens.LoginScreen
import com.aileenyx.wikigrimoire.screens.SearchScreen
import com.aileenyx.wikigrimoire.ui.theme.WikiGrimoireTheme
import com.aileenyx.wikigrimoire.components.Screen
import com.aileenyx.wikigrimoire.components.tabs
import com.aileenyx.wikigrimoire.screens.ProfileScreen
import com.aileenyx.wikigrimoire.util.getSessionToken
import com.aileenyx.wikigrimoire.util.isActiveSession
import com.aileenyx.wikigrimoire.util.migrateTemplates
import com.aileenyx.wikigrimoire.util.populateTemplates
import com.aileenyx.wikigrimoire.util.storeSessionToken
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import java.io.File

class MainActivity : ComponentActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        auth = Firebase.auth

        if (isFirstLaunch(this)) {
            val directory = File(this.filesDir, "images")
            if (!directory.exists()) {
                directory.mkdirs()
            }
            migrateTemplates(this)
            setFirstLaunchCompleted(this)
        }


        populateTemplates(this)
        setContent {
            WikiGrimoireTheme {
                val navController = rememberNavController()
                NavControllerProvider.navController = navController

                var isActiveSession by remember { mutableStateOf(getSessionToken(this@MainActivity) != null) }

                fun updateSessionStatus() {
                    isActiveSession = getSessionToken(this@MainActivity) != null
                }

                CompositionLocalProvider(LocalNavController provides navController) {
                    Scaffold(modifier = Modifier.fillMaxSize(), bottomBar = {
                        if (isActiveSession) {
                            NavigationBar {
                                tabs.map { item ->
                                    NavigationBarItem(
                                        selected = false,
                                        onClick = {
                                            navController.navigate(item.screen)
                                        },
                                        icon = {
                                            Icon(
                                                imageVector = item.icon,
                                                contentDescription = null
                                            )
                                        }
                                    )
                                }
                            }
                        }
                    }) { innerPadding ->
                        Box(modifier = Modifier.padding(innerPadding)) {
                            NavHost(
                                navController = navController,
                                startDestination = if (isActiveSession) {
                                    Screen.HomeScreen
                                } else {
                                    Screen.LoginScreen
                                }
                            ) {
                                composable<Screen.HomeScreen> {
                                    HomeScreen()
                                }
                                composable<Screen.SearchScreen> {
                                    SearchScreen()
                                }
                                composable<Screen.CreateScreen> {
                                    CreateScreen()
                                }
                                composable<Screen.LoginScreen> {
                                    LoginScreen(onLoginSuccess = {
                                        storeSessionToken(this@MainActivity, "your_session_token")
                                        updateSessionStatus()
                                    })
                                }
                                composable<Screen.ProfileScreen> {
                                    ProfileScreen()
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if (currentUser != null) {
            reload()
        }
    }

    private fun createAccount(email: String, password: String) {
        // [START create_user_with_email]
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "createUserWithEmail:success")
                    val user = auth.currentUser
                    updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
                    Toast.makeText(
                        baseContext,
                        "Authentication failed.",
                        Toast.LENGTH_SHORT,
                    ).show()
                    updateUI(null)
                }
            }
        // [END create_user_with_email]
    }

    private fun signIn(email: String, password: String) {
        // [START sign_in_with_email]
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithEmail:success")
                    val user = auth.currentUser
                    updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithEmail:failure", task.exception)
                    Toast.makeText(
                        baseContext,
                        "Authentication failed.",
                        Toast.LENGTH_SHORT,
                    ).show()
                    updateUI(null)
                }
            }
        // [END sign_in_with_email]
    }

    private fun sendEmailVerification() {
        // [START send_email_verification]
        val user = auth.currentUser!!
        user.sendEmailVerification()
            .addOnCompleteListener(this) { task ->
                // Email Verification sent
            }
        // [END send_email_verification]
    }

    private fun updateUI(user: FirebaseUser?) {
    }

    private fun reload() {
    }

    companion object {
        private const val TAG = "EmailPassword"
    }
}

private fun isFirstLaunch(context: Context): Boolean {
    val sharedPreferences = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
    return sharedPreferences.getBoolean("first_launch", true)
}

private fun setFirstLaunchCompleted(context: Context) {
    val sharedPreferences = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
    with(sharedPreferences.edit()) {
        putBoolean("first_launch", false)
        apply()
    }
}