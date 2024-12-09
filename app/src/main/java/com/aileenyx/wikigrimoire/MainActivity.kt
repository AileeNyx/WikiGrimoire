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
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.aileenyx.wikigrimoire.screens.CreateScreen
import com.aileenyx.wikigrimoire.screens.HomeScreen
import com.aileenyx.wikigrimoire.screens.SearchScreen
import com.aileenyx.wikigrimoire.ui.theme.WikiGrimoireTheme
import com.aileenyx.wikigrimoire.components.Screen
import com.aileenyx.wikigrimoire.components.tabs
import com.aileenyx.wikigrimoire.util.migrateTemplates
import com.aileenyx.wikigrimoire.util.populateTemplates

class MainActivity : ComponentActivity() {
    // Update the `onCreate` method in `MainActivity.kt`
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        if (isFirstLaunch(this)) {
            migrateTemplates(this)
            setFirstLaunchCompleted(this)
        }
        populateTemplates(this)
        setContent {
            WikiGrimoireTheme() {
                val navController = rememberNavController()
                val navBackStackEntry = navController.currentBackStackEntryAsState().value

                Scaffold(modifier = Modifier.fillMaxSize(), bottomBar = {
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
                }) { innerPadding ->
                    Box(modifier = Modifier.padding(innerPadding)) {
                        NavHost(
                            navController = navController,
                            startDestination = Screen.HomeScreen
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