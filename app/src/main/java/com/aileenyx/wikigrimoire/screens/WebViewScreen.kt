package com.aileenyx.wikigrimoire.screens

import GrimoireHeader
import android.app.PictureInPictureParams
import android.os.Build
import android.util.Rational
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.ComponentActivity
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView

@Composable
fun WebViewScreen(url: String) {
    val context = LocalContext.current as ComponentActivity

    GrimoireHeader(
        showProfilePicture = true,
        showBackArrow = true
    )

    AndroidView(factory = { ctx ->
        WebView(ctx).apply {
            webViewClient = CustomWebViewClient()
            loadUrl(url)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.enterPictureInPictureMode(createPipParams())
            }
        }
    })
}

@RequiresApi(Build.VERSION_CODES.O)
private fun createPipParams(): PictureInPictureParams {
    return PictureInPictureParams.Builder()
        .setAspectRatio(Rational(16, 9))
        .build()
}

class CustomWebViewClient : WebViewClient() {
    override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
        view?.loadUrl(request?.url.toString())
        return true
    }

    override fun onReceivedError(
        view: WebView?,
        request: WebResourceRequest?,
        error: WebResourceError?
    ) {
        super.onReceivedError(view, request, error)
    }
}