package com.cayot.flyingmore.data.repository

import android.content.ContentValues.TAG
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import androidx.core.content.FileProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class ImageRepository(private val context: Context) {

    suspend fun saveBitmapToCache(bitmap: Bitmap): Uri? = withContext(Dispatchers.IO) {
        val folder = File(context.cacheDir, "generatedImages")
        var uri: Uri? = null
        try {
            folder.mkdirs()
            val imageFile = File(folder, System.currentTimeMillis().toString())

            FileOutputStream(imageFile).use { stream ->
                bitmap.compress(Bitmap.CompressFormat.PNG, 90, stream)
            }
            uri = FileProvider.getUriForFile(context, "com.cayot.flyingmore.fileprovider", imageFile)
        } catch (e: IOException) {
            Log.e(TAG, "IOException while trying to write file for sharing: ${e.message}")
        }
        uri
    }
}