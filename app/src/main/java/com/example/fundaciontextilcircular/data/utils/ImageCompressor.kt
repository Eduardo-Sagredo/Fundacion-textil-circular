package com.example.fundaciontextilcircular.data.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import java.io.File
import java.io.FileOutputStream
import java.util.UUID

object ImageCompressor {

    fun comprimirYGuardar(context: Context, uri: Uri): String? {
        return try {
            val inputStream = context.contentResolver.openInputStream(uri)
            val bitmapOriginal = BitmapFactory.decodeStream(inputStream)
            inputStream?.close()

            if (bitmapOriginal == null) return null

            val nombreArchivo = "IMG_${UUID.randomUUID()}.jpg"
            val archivoDestino = File(context.filesDir, nombreArchivo)

            val outputStream = FileOutputStream(archivoDestino)
            bitmapOriginal.compress(Bitmap.CompressFormat.JPEG, 70, outputStream)
            outputStream.flush()
            outputStream.close()

            archivoDestino.absolutePath
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}