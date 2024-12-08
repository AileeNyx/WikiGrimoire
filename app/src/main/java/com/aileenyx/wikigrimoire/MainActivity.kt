package com.aileenyx.wikigrimoire

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
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.aileenyx.wikigrimoire.screens.CreateScreen
import com.aileenyx.wikigrimoire.screens.HomeScreen
import com.aileenyx.wikigrimoire.screens.SearchScreen
import com.aileenyx.wikigrimoire.util.Screen.CreateScreen.toScreen
import com.aileenyx.wikigrimoire.ui.theme.WikiGrimoireTheme
import com.aileenyx.wikigrimoire.util.Screen
import com.aileenyx.wikigrimoire.util.tabs

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WikiGrimoireTheme() {
                val navController = rememberNavController()
                val navBackStackEntry = navController.currentBackStackEntryAsState().value

                Scaffold(modifier = Modifier.fillMaxSize(), bottomBar = {
                    NavigationBar {
                        tabs.map { item ->
                            val isSelected = item.screen == navBackStackEntry?.toScreen()
                            NavigationBarItem(
                                selected = isSelected,
                                onClick = {
                                    navController.navigate(item.screen)
                                },
                                icon = {
                                    Icon(
                                        imageVector = item.icon,
                                        contentDescription = null,
                                        modifier = Modifier.then(
                                            if (isSelected) Modifier else Modifier
                                        ),
                                        tint = if (isSelected) MaterialTheme.colorScheme.primary else LocalContentColor.current
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

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    WikiGrimoireTheme {
        Greeting("Android")
    }
}