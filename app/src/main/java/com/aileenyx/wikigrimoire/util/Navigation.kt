package com.aileenyx.wikigrimoire.util
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavBackStackEntry
import androidx.navigation.toRoute
import kotlinx.serialization.Serializable

@Serializable
sealed class Screen {
    @Serializable
    data object HomeScreen : Screen()

    @Serializable
    data object SearchScreen : Screen()

    @Serializable
    data object CreateScreen : Screen()

    @Serializable
    data object LogInScreen : Screen()

    @Serializable
    data object SettingsScreen : Screen()

    @Serializable
    data object ProfileScreen : Screen()

    fun NavBackStackEntry.toScreen(): Screen? =
        when (destination.route?.substringAfterLast(".")?.substringBefore("/")) {
            "HomeScreen" -> toRoute<HomeScreen>()
            "SearchScreen" -> toRoute<SearchScreen>()
            "CreateScreen" -> toRoute<CreateScreen>()
            else -> null
        }
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

