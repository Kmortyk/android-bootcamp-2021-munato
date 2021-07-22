package com.vanilla.munato.model

import android.graphics.Bitmap

val EmptyPaintingPreview = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888)

data class PaintingPublishData(
    val model: PaintingModel,
    val preview: PaintingPreview
)
