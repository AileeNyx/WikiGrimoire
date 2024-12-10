package com.aileenyx.wikigrimoire.screens

import GrimoireHeader
import android.util.Log
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
import androidx.compose.material3.SnackbarHostState
import androidx.compose.ui.text.input.PasswordVisualTransformation
import com.aileenyx.wikigrimoire.components.LocalNavController
import com.aileenyx.wikigrimoire.components.Screen
import com.aileenyx.wikigrimoire.util.getUserId
import com.aileenyx.wikigrimoire.util.getUsername

@Composable
fun LoginScreen() {
    val navController = LocalNavController.current
    val context = LocalContext.current
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val snackbarHostState = remember { SnackbarHostState() }


    Column(
        modifier = Modifier
            .fillMaxSize()
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
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation()
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            CoroutineScope(Dispatchers.Main).launch {
                try {
                    signInWithEmail(email, password)
                    Log.d("LoginScreen", "User signed in with credentials: ${getUsername()}, ${getUserId()}")
                    navController.navigate(Screen.HomeScreen)
                } catch (e: Exception) {
                    Log.e("LoginScreen", "Sign up exception: ${e.message}")
                    // Show exception message to the user
                }
            }
        }) {
            Text("Log in")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            CoroutineScope(Dispatchers.Main).launch {
                try {
                    signUpNewUser(email, password)
                    CoroutineScope(Dispatchers.Main).launch {
                        snackbarHostState.showSnackbar("Registered Succesfully! Please log in.")
                    }
                } catch (e: Exception) {
                    Log.e("LoginScreen", "Sign up exception: ${e.message}")
                    // Show exception message to the user
                }
            }
        }) {
            Text("Sign up")
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}
