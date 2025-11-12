package com.jeyix.schooljeyix.data.util

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FileUtil @Inject constructor(
    @ApplicationContext private val context: Context
) {

    /**
     * Crea un MultipartBody.Part desde un Uri.
     * Copia el archivo a la caché local, lo prepara y lo devuelve.
     */
    fun createMultipartBody(uri: Uri, partName: String): MultipartBody.Part? {
        return try {
            val file = File(context.cacheDir, getFileName(uri))
            val inputStream = context.contentResolver.openInputStream(uri)
            val outputStream = FileOutputStream(file)
            inputStream?.copyTo(outputStream)
            inputStream?.close()
            outputStream.close()

            val requestFile = file.asRequestBody(
                context.contentResolver.getType(uri)?.toMediaTypeOrNull()
            )

            MultipartBody.Part.createFormData(partName, file.name, requestFile)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    /**
     * Obtiene el nombre original del archivo desde el ContentResolver.
     */
    private fun getFileName(uri: Uri): String {
        var result: String? = null
        if (uri.scheme == "content") {
            val cursor = context.contentResolver.query(uri, null, null, null, null)
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    val index = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                    if (index >= 0) {
                        result = cursor.getString(index)
                    }
                }
            } finally {
                cursor?.close()
            }
        }
        if (result == null) {
            result = uri.path
            val cut = result!!.lastIndexOf('/')
            if (cut != -1) {
                result = result.substring(cut + 1)
            }
        }
        return result ?: "temp_file_${System.currentTimeMillis()}"
    }
}