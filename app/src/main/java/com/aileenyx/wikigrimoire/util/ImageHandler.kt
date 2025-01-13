package com.aileenyx.wikigrimoire.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.InputStream

fun getImageFromName(imageName: String, isTemplate: Boolean, context: Context): ImageBitmap {
    val inputStream: InputStream = if (isTemplate) {
        context.assets.open("images/$imageName.webp")
    } else {
        FileInputStream(File("${context.filesDir}/images/$imageName.webp"))
    }
    val bitmap = BitmapFactory.decodeStream(inputStream)
    return bitmap.asImageBitmap()
}

fun generateImageFileName(name: String): String {
    val userid = getUID()
    return "$name-$userid"
}

fun saveBitmapAsWebp(context: Context, bitmap: Bitmap, fileName: String) {
    val directory = File(context.filesDir, "images")
    val file = File(directory, "$fileName.webp")
    FileOutputStream(file).use { outputStream ->
        bitmap.compress(Bitmap.CompressFormat.WEBP, 100, outputStream)
    }
}