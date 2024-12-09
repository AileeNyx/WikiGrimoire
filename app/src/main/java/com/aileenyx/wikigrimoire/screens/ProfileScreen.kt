package com.aileenyx.wikigrimoire.screens

import GrimoireHeader
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.compose.material3.Scaffold
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text

fun ProfileHandler(navController: NavController) {
    navController.navigate("login")
}

@Composable
fun ProfileScreen(
    profilePicture: Int,
    name: String,
    email: String,
    onSyncDataClick: () -> Unit,
    onLogOutClick: () -> Unit,
    navController: NavController
) {
    Scaffold(
        topBar = {
            GrimoireHeader(
                title = "Wiki Grimoire",
                showProfilePicture = false,
                showBackArrow = true,
                onProfileClick = { /* Handle profile click */ },
                onBackClick = { navController.navigate("home") }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = profilePicture),
                contentDescription = "Profile Picture",
                modifier = Modifier
                    .size(100.dp)
                    .clickable { /* Handle profile picture click */ }
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = name, fontSize = 24.sp, color = Color.Black)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = email, fontSize = 16.sp, color = Color.Gray)
            Spacer(modifier = Modifier.height(32.dp))
            Button(
                onClick = onSyncDataClick,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Sync Data")
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = onLogOutClick,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Log Out")
            }
        }
    }
}