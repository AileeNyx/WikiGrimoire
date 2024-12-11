package com.aileenyx.wikigrimoire.components

import android.annotation.SuppressLint
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavHostController
import kotlinx.serialization.Serializable

val LocalNavController = staticCompositionLocalOf<NavHostController> { error("NavController not provided") }

object NavControllerProvider {
    @SuppressLint("StaticFieldLeak")
    lateinit var navController: NavHostController
}

@Serializable
sealed class Screen {
    @Serializable
    data object HomeScreen : Screen()

    @Serializable
    data object SearchScreen : Screen()

    @Serializable
    data object CreateScreen : Screen()

    @Serializable
    data object LoginScreen : Screen()

    @Serializable
    data object ProfileScreen : Screen()
}

data class NavItem(
    val screen: Screen,
    val label: String,
    val icon: ImageVector
)

val tabs = listOf(
    NavItem(
        label = "Home",
        icon = Icons.Filled.Home,
        screen = Screen.HomeScreen,
    ),
    NavItem(
        label = "Search",
        icon = Icons.Filled.Search,
        screen = Screen.SearchScreen,
    ),
    NavItem(
        label = "Create",
        icon = Icons.Filled.Add,
        screen = Screen.CreateScreen,
    )
)