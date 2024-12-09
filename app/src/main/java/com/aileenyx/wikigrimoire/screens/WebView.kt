package com.aileenyx.wikigrimoire.screens

import GrimoireHeader
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.runtime.Composable
import androidx.compose.ui.viewinterop.AndroidView

@Composable
fun WebViewScreen(url: String) {
    GrimoireHeader(
        title = "Wiki Grimoire",
        showProfilePicture = true,
        showBackArrow = true,
        onProfileClick = { /* Handle profile click */ },
        onBackClick = { /* Handle back click */ }
    )

    AndroidView(factory = { context ->
        WebView(context).apply {
            webViewClient = WebViewClient()
            loadUrl(url)
        }
    })
}