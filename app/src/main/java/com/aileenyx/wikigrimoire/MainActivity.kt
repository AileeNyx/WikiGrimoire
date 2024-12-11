package com.aileenyx.wikigrimoire

import android.content.Context
import android.os.Bundle
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
import java.io.File

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

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