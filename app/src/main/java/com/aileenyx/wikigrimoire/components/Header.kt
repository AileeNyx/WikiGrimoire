import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.filled.AccountCircle
import com.aileenyx.wikigrimoire.components.LocalNavController
import com.aileenyx.wikigrimoire.components.Screen

@Composable
fun GrimoireHeader(
    showProfilePicture: Boolean,
    showBackArrow: Boolean
) {
    val navController = LocalNavController.current
    val title = "Wiki Grimoire"

    Column {
        TopAppBar(
            title = { Text(text = title, modifier = Modifier.fillMaxWidth().wrapContentWidth(Alignment.CenterHorizontally)) },
            navigationIcon = if (showBackArrow) {
                {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                        .clickable {
                            navController.navigate(Screen.HomeScreen)
                        }
                    )
                }
            } else null,
            actions = {
                if (showProfilePicture) {
                    Icon(
                        imageVector = Icons.Filled.AccountCircle,
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                            .clickable {
                                navController.navigate(Screen.ProfileScreen)
                            }
                    )
                }
            },
            modifier = Modifier.fillMaxWidth()
        )
    }
}