package com.vanilla.munato.model

import android.graphics.Color

data class PaintingViewData(
    val paintingDownloadData: PaintingDownloadData,
    var averageColor: Int = Color.parseColor("#636363")
) {
    public fun getModel() = paintingDownloadData.model

    public fun getPreviewURL() = paintingDownloadData.previewURL
}