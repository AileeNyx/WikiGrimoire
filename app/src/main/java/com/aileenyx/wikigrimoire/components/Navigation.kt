package com.aileenyx.wikigrimoire.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.graphics.vector.ImageVector
import kotlinx.serialization.Serializable


@Serializable
sealed class Screen {
    @Serializable
    data object HomeScreen : Screen()

    @Serializable
    data object SearchScreen : Screen()

    @Serializable
    data object CreateScreen : Screen()
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