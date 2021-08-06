package com.vanilla.munato.model

import android.R
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.util.Base64
import android.util.Log
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import java.io.ByteArrayOutputStream

typealias PaintingPreview = Bitmap

val EmptyPaintingPreview: PaintingPreview = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888)

class PaintingPreviewMethods {
    companion object {
        fun toBase64(preview: PaintingPreview) : String {
            return Base64.encodeToString(compressForPublish(preview), Base64.DEFAULT)
        }

        fun fromBase64(base64: String) : PaintingPreview {
            val data = if(base64.contains(',')) {
                base64.substring(base64.indexOf(",") + 1)
            } else {
                base64
            }

            val decodedString: ByteArray = Base64.decode(data, Base64.DEFAULT)

            if(decodedString.isEmpty()) {
                Log.e("PaintingPreviewMethods", "Failed to decode string. Replace with empty painting preview")
                return EmptyPaintingPreview
            }

            return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
        }

        fun fromBytes(bytes: ByteArray?) : PaintingPreview {
            if(bytes == null)
                return EmptyPaintingPreview

            return BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
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

        fun downloadPainting(
            context: Context,
            previewURL: Uri,
            intoImageView: ImageView,
            onResourceReady: (Drawable?, Any?, Target<Drawable>?, DataSource?, Boolean) -> Unit) {

            Glide.with(context)
                .load(previewURL)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean) = false

                    override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                        onResourceReady(resource, model, target, dataSource, isFirstResource)

                        return false
                    }
                })
                .into(intoImageView)
        }
    }
}