package com.tghrsyahptra.storyapp.utils

import android.app.Application
import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import android.os.Environment
import com.tghrsyahptra.storyapp.R
import java.io.*
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

private const val FILENAME_FORMAT = "dd-MMM-yyyy"
private const val MAX_IMAGE_SIZE = 1000000
private const val IMAGE_EXTENSION = ".jpg"

val currentTimestamp: String = SimpleDateFormat(
    FILENAME_FORMAT,
    Locale.US
).format(System.currentTimeMillis())

// Creates a temporary file for camera intent
fun createTempFileForCamera(context: Context): File {
    val storageDirectory: File? = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
    return File.createTempFile(currentTimestamp, IMAGE_EXTENSION, storageDirectory)
}

// Creates a file in the app's external media directory or fallback to internal storage
fun createAppFile(application: Application): File {
    val mediaDirectory = application.externalMediaDirs.firstOrNull()?.let {
        File(it, application.resources.getString(R.string.app_name)).apply { mkdirs() }
    }
    val outputDirectory = mediaDirectory?.takeIf { it.exists() } ?: application.filesDir
    return File(outputDirectory, "$currentTimestamp$IMAGE_EXTENSION")
}

// Rotates the image file based on the camera orientation (front or back)
fun rotateImageFile(file: File, isBackCamera: Boolean = false) {
    val rotationMatrix = Matrix()
    val bitmap = BitmapFactory.decodeFile(file.path)
    val rotationAngle = if (isBackCamera) -90f else 90f
    rotationMatrix.postRotate(rotationAngle)

    val rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, rotationMatrix, true)
    rotatedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, FileOutputStream(file))
}

// Converts a URI to a file and saves it temporarily
fun convertUriToFile(uri: Uri, context: Context): File {
    val contentResolver: ContentResolver = context.contentResolver
    val tempFile = createTempFileForCamera(context)

    contentResolver.openInputStream(uri)?.use { inputStream ->
        FileOutputStream(tempFile).use { outputStream ->
            val buffer = ByteArray(1024)
            var length: Int
            while (inputStream.read(buffer).also { length = it } > 0) {
                outputStream.write(buffer, 0, length)
            }
        }
    }
    return tempFile
}

// Reduces the image size to under 1MB by compressing it iteratively
fun compressImageFile(file: File): File {
    val bitmap = BitmapFactory.decodeFile(file.path)
    var quality = 100
    var streamLength: Int
    do {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, byteArrayOutputStream)
        streamLength = byteArrayOutputStream.toByteArray().size
        quality -= 5
    } while (streamLength > MAX_IMAGE_SIZE)

    bitmap.compress(Bitmap.CompressFormat.JPEG, quality, FileOutputStream(file))
    return file
}

// Formats a timestamp string into a human-readable date format
fun String.toFormattedDate(): String {
    val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault()).apply {
        timeZone = TimeZone.getTimeZone("UTC")
    }
    val date = format.parse(this) as Date
    return DateFormat.getDateInstance(DateFormat.FULL).format(date)
}