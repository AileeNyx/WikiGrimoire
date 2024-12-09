import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.Dp
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height

@Composable
fun GrimoireHeader(
    title: String,
    showProfilePicture: Boolean,
    showBackArrow: Boolean,
    onProfileClick: () -> Unit,
    onBackClick: () -> Unit,
    headerHeight: Dp = 56.dp // Default height of TopAppBar
) {
    Column {
        TopAppBar(
            title = { Text(text = title, modifier = Modifier.fillMaxWidth().wrapContentWidth(Alignment.CenterHorizontally)) },
            navigationIcon = if (showBackArrow) {
                {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                    )
                }
            } else null,
            actions = {
                /*if (showProfilePicture) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_profile_picture),
                        contentDescription = "Profile",
                        modifier = Modifier
                            .size(40.dp)
                            .clickable { onProfileClick() }
                    )
                }*/
            },
            modifier = Modifier.fillMaxWidth()
        )
    }
}