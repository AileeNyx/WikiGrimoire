package com.aileenyx.wikigrimoire.screens

import GrimoireHeader
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material3.Scaffold
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.ui.platform.LocalContext
import com.aileenyx.wikigrimoire.components.LocalNavController
import com.aileenyx.wikigrimoire.components.Screen
import com.aileenyx.wikigrimoire.util.getEmail
import com.aileenyx.wikigrimoire.util.getUsername
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

@Composable
fun ProfileScreen() {
    val user = getEmail()
    val navController = LocalNavController.current
    val localContext = LocalContext.current

    Scaffold(
        topBar = {
            GrimoireHeader(
                showProfilePicture = false,
                showBackArrow = true
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
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = user!!, fontSize = 24.sp, color = Color.Black)
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = {
                    Firebase.auth.signOut()
                    navController.navigate(Screen.LoginScreen)
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Log Out")
            }
        }
    }
}