package com.aileenyx.wikigrimoire.screens

import android.util.Log
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
import com.aileenyx.wikigrimoire.util.signInWithEmail
import com.aileenyx.wikigrimoire.util.signUpNewUser
import kotlinx.coroutines.launch
import androidx.compose.material3.SnackbarHostState
import androidx.compose.ui.text.input.PasswordVisualTransformation
import com.aileenyx.wikigrimoire.components.LocalNavController
import com.aileenyx.wikigrimoire.components.Screen
import com.aileenyx.wikigrimoire.util.getUserId
import com.aileenyx.wikigrimoire.util.getUsername
import com.aileenyx.wikigrimoire.util.isActiveSession
import kotlinx.coroutines.delay

@Composable
fun LoginScreen(onLoginSuccess: () -> Unit) {
    val navController = LocalNavController.current
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val snackbarHostState = remember { SnackbarHostState() }
    var loading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val coroutineScope = rememberCoroutineScope()

    fun validateInput(input: String): String {
        return input.filter { it != '\n' && it != '\t' }
    }

    suspend fun waitForActiveSession() {
        while (!isActiveSession()) {
            delay(1000)
        }
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
                coroutineScope.launch {
                    try {
                        signInWithEmail(email, password)
                        Log.d("LoginScreen", "User signed in with credentials: ${getUsername()}, ${getUserId()}")
                        waitForActiveSession()
                        onLoginSuccess()
                        navController.navigate(Screen.HomeScreen)
                    } catch (e: Exception) {
                        if (e.message?.contains("invalid_credentials") == true) {
                            Log.e("LoginScreen", "Sign in exception: ${e.message}")
                            errorMessage = "Wrong credentials. Please try again."
                        } else {
                            Log.e("LoginScreen", "Sign in exception: ${e.message}")
                            errorMessage = "An error occurred. Please try again."
                        }
                    } finally {
                        loading = false
                    }
                }
            }) {
                Text("Log in")
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = {
                loading = true
                coroutineScope.launch {
                    try {
                        signUpNewUser(email, password)
                        snackbarHostState.showSnackbar("Registered Successfully! Please log in.")
                    } catch (e: Exception) {
                        Log.e("LoginScreen", "Sign up exception: ${e.message}")
                        errorMessage = "Sign up failed. Please try again."
                    } finally {
                        loading = false
                    }
                }
            }) {
                Text("Sign up")
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}