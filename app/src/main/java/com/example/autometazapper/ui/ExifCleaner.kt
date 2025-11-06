package com.example.autometazapper.ui

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import androidx.exifinterface.media.ExifInterface
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

object ExifCleaner {

    /**
     * Returns the cleaned image File stored in cache (or null on error).
     */
    fun cleanImage(context: Context, uri: Uri, fieldsToRemove: Set<ExifField>): File? {
        return try {
            Log.d("ExifCleaner", "Starting cleaning for URI: $uri")

            val inputStream: InputStream = context.contentResolver.openInputStream(uri)
                ?: throw IllegalArgumentException("Unable to open image input stream")
            val originalFile = File(context.cacheDir, "original_${System.currentTimeMillis()}.jpg")
            inputStream.use { input ->
                FileOutputStream(originalFile).use { output ->
                    input.copyTo(output)
                }
            }
            Log.d("ExifCleaner", "Original file copied to cache: ${originalFile.absolutePath}")

            val bitmap: Bitmap = decodeBitmap(context, Uri.fromFile(originalFile))
                ?: throw IllegalArgumentException("Failed to decode bitmap")

            val cleanedFile = File(context.cacheDir, "cleaned_${System.currentTimeMillis()}.jpg")
            FileOutputStream(cleanedFile).use { output ->
                bitmap.compress(Bitmap.CompressFormat.JPEG, 95, output)
            }
            Log.d("ExifCleaner", "Cleaned file created: ${cleanedFile.absolutePath}")

            val originalExif = ExifInterface(originalFile.absolutePath)
            val newExif = ExifInterface(cleanedFile.absolutePath)

            val allTags = mapOf(
                ExifField.LOCATION to listOf(
                    ExifInterface.TAG_GPS_LATITUDE,
                    ExifInterface.TAG_GPS_LONGITUDE,
                    ExifInterface.TAG_GPS_LATITUDE_REF,
                    ExifInterface.TAG_GPS_LONGITUDE_REF
                ),
                ExifField.CAMERA_MAKE to listOf(ExifInterface.TAG_MAKE),
                ExifField.CAMERA_MODEL to listOf(ExifInterface.TAG_MODEL),
                ExifField.DATE_TIME to listOf(ExifInterface.TAG_DATETIME),
                ExifField.FLASH to listOf(ExifInterface.TAG_FLASH),
                ExifField.FOCAL_LENGTH to listOf(ExifInterface.TAG_FOCAL_LENGTH)
            )

            for ((field, tags) in allTags) {
                if (!fieldsToRemove.contains(field)) {
                    for (tag in tags) {
                        val value = originalExif.getAttribute(tag)
                        if (value != null) newExif.setAttribute(tag, value)
                    }
                }
            }

            newExif.saveAttributes()
            Log.d("ExifCleaner", "EXIF successfully saved to cleaned file")

            cleanedFile
        } catch (e: Exception) {
            Log.e("ExifCleaner", "Error cleaning image: ${e.message}", e)
            null
        }
    }

    private fun decodeBitmap(context: Context, uri: Uri): Bitmap? {
        return try {
            if (Build.VERSION.SDK_INT >= 28) {
                val source = ImageDecoder.createSource(context.contentResolver, uri)
                ImageDecoder.decodeBitmap(source)
            } else {
                MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
            }
        } catch (e: Exception) {
            Log.e("ExifCleaner", "Bitmap decode failed: ${e.message}", e)
            null
        }
    }
}
