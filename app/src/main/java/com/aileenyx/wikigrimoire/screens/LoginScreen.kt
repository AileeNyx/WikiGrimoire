package com.aileenyx.wikigrimoire.screens

import GrimoireHeader
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.aileenyx.wikigrimoire.util.GoogleSignInButton
import com.aileenyx.wikigrimoire.util.signInWithEmail
import com.aileenyx.wikigrimoire.util.signUpNewUser
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import androidx.compose.material3.Scaffold

@Composable
fun LoginScreen(navController: NavController) {
    val context = LocalContext.current
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

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
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            TextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            TextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = {
                CoroutineScope(Dispatchers.Main).launch {
                    signInWithEmail(email, password)
                }
            }) {
                Text("Log in")
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = {
                CoroutineScope(Dispatchers.Main).launch {
                    signUpNewUser(email, password)
                }
            }) {
                Text("Sign up")
            }
            Spacer(modifier = Modifier.height(16.dp))
            GoogleSignInButton()
        }
    }
}