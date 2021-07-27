package com.vanilla.munato.model

import android.R
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import java.io.ByteArrayOutputStream

typealias PaintingPreview = Bitmap

val EmptyPaintingPreview: PaintingPreview = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888)

class PaintingPreviewMethods {
    companion object {
        fun toBase64(preview: PaintingPreview) : String {
            return Base64.encodeToString(compressForPublish(preview), Base64.DEFAULT)
        }

        fun fromBase64(base64: String) : PaintingPreview {
            val data = base64.substring(base64.indexOf(",") + 1)
            val decodedString: ByteArray = Base64.decode(data, Base64.DEFAULT)

            if(decodedString.isEmpty()) {
                return EmptyPaintingPreview
            }

            return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
        }

        fun compressForPublish(preview: PaintingPreview): ByteArray {
            val scaled = Bitmap.createScaledBitmap(
                preview,
                preview.width / 2,
                preview.height / 2,
                false)

            val compressedStream = ByteArrayOutputStream()

            scaled.compress(Bitmap.CompressFormat.JPEG, 70, compressedStream)

            return compressedStream.toByteArray()
        }
    }
}