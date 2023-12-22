package com.epilepto.dhyanapp.utils

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Picture
import android.os.Build
import android.widget.Toast
import androidx.core.content.FileProvider
import com.epilepto.dhyanapp.model.session.Score
import java.io.File
import java.io.FileOutputStream

fun saveBitmapToFile(context: Context, bitmap: Bitmap, filename: String): File {
    val file = File(context.externalCacheDir, filename)
    FileOutputStream(file).use {
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, it)
    }
    return file
}

fun shareImage(context: Context, imageFile: File) {
    try {
        val uri = FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider", // Make sure this matches your manifest
            imageFile
        )

        val shareIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_STREAM, uri)
            type = "image/png"
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }

        context.startActivity(Intent.createChooser(shareIntent, "Share Image"))
    } catch (e: Exception) {
        Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
    }

}

fun saveAndShareSession(
    context: Context,
    picture: Picture,
    index: Int,
    score: Score
) {
    val bitmap = picture.toBitmap()
    try {
        val filename = "Session_${index + 1}_${score.duration}.jpeg"
        val file = saveBitmapToFile(context, bitmap, filename)
        shareImage(context, file)
    } catch (e: Exception) {
        Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
    }
}

private fun Picture.toBitmap(): Bitmap {
    // [START android_compose_draw_into_bitmap_convert_picture]
    val bitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
        Bitmap.createBitmap(this)
    } else {
        val bitmap = Bitmap.createBitmap(
            width,
            height,
            Bitmap.Config.ARGB_8888
        )
        val canvas = android.graphics.Canvas(bitmap)
        canvas.drawColor(android.graphics.Color.WHITE)
        canvas.drawPicture(this)
        bitmap
    }
    // [END android_compose_draw_into_bitmap_convert_picture]
    return bitmap
}