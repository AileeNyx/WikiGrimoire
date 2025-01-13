package com.aileenyx.wikigrimoire.util

import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

fun signInUser(email: String, password: String, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
    val auth = FirebaseAuth.getInstance()
    auth.signInWithEmailAndPassword(email, password)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                // Sign in success
                onSuccess()
            } else {
                // If sign in fails, pass the exception to the onFailure callback
                task.exception?.let { onFailure(it) }
            }
        }
}

fun createAccount(email: String, password: String, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
    val auth = FirebaseAuth.getInstance()
    auth.createUserWithEmailAndPassword(email, password)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                // Account creation success
                onSuccess()
            } else {
                // If account creation fails, pass the exception to the onFailure callback
                task.exception?.let { onFailure(it) }
            }
        }
}

fun activeSession(): Boolean {
    val user = Firebase.auth.currentUser
    return user != null
}

fun getUsername(): String? {
    val user = Firebase.auth.currentUser
    return user?.displayName
}

fun getEmail(): String? {
    val user = Firebase.auth.currentUser
    return user?.email
}

fun getPhotoUrl(): String? {
    val user = Firebase.auth.currentUser
    return user?.photoUrl.toString()
}

fun getUID(): String? {
    val user = Firebase.auth.currentUser
    return user?.uid
}