package com.aileenyx.wikigrimoire.screens

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import androidx.compose.material3.SnackbarHostState
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import com.aileenyx.wikigrimoire.MainActivity
import com.aileenyx.wikigrimoire.components.LocalNavController
import com.aileenyx.wikigrimoire.components.Screen
import com.aileenyx.wikigrimoire.util.createAccount
import com.aileenyx.wikigrimoire.util.signInUser
import kotlinx.coroutines.delay

@Composable
fun LoginScreen() {
    val navController = LocalNavController.current
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }
    var loading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val coroutineScope = rememberCoroutineScope()

    fun validateInput(input: String): String {
        return input.filter { it != '\n' && it != '\t' }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Login to Your Account",
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        TextField(
            value = email,
            onValueChange = { email = validateInput(it) },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = password,
            onValueChange = { password = validateInput(it) },
            label = { Text("Password") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation()
        )
        Spacer(modifier = Modifier.height(16.dp))
        errorMessage?.let {
            Text(text = it, color = androidx.compose.ui.graphics.Color.Red)
            Spacer(modifier = Modifier.height(8.dp))
        }
        if (loading) {
            CircularProgressIndicator()
        } else {
            Button(onClick = {
                loading = true
                signInUser(email, password,
                    onSuccess = {
                        navController.navigate(Screen.HomeScreen)
                    },
                    onFailure = {
                        errorMessage = if (it.message?.contains("invalid_credentials") == true) {
                            "Wrong credentials. Please try again."
                        } else {
                            "An error occurred. Please try again."
                        }
                    }
                )
                loading = false
            }) {
                Text("Log in")
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = {
                loading = true
                createAccount(email, password,
                    onSuccess = {
                        Log.d("LoginScreen", "Account created successfully")
                        Toast.makeText(
                            context,
                            "Sign Up Succesful! Please Log In.",
                            Toast.LENGTH_SHORT
                        ).show()
                    },
                    onFailure = {
                        Log.e("LoginScreen", "Account creation failed: ${it.message}")
                        errorMessage = "Sign up failed. Please try again."
                    }
                )
                loading = false
            }) {
                Text("Sign up")
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}