package com.aileenyx.wikigrimoire.components

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.aileenyx.wikigrimoire.util.getImageFromName

@Composable
fun WikiCard(
    name: String,
    url: String,
    image: String,
    isTemplate: Boolean,
    isLarge: Boolean
) {
    val context = LocalContext.current

    val imageBitmap = remember(image) { getImageFromName(image, isTemplate, context) }
    val imagePainter = imageBitmap.let { androidx.compose.ui.graphics.painter.BitmapPainter(it!!) }

    val cardHeight = if (isLarge) 200.dp else 100.dp
    val imageHeight = if (isLarge) 133.dp else 40.dp
    val textStyle = if (isLarge) MaterialTheme.typography.headlineMedium else MaterialTheme.typography.headlineSmall

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(cardHeight)
            .padding(8.dp)
            .clickable {
                val validUrl = if (url.startsWith("http://") || url.startsWith("https://")) {
                    url
                } else {
                    "http://$url"
                }
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(validUrl))
                context.startActivity(intent)
            },
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Column {
            Image(
                painter = imagePainter,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(imageHeight)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp, 3.dp, 8.dp, 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = name,
                    style = textStyle,
                    modifier = Modifier.offset(y = (-4).dp)
                )
                Icon(
                    imageVector = Icons.Filled.ArrowForward,
                    contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}