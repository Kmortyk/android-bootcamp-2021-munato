package com.vanilla.munato.fragment.logic

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.webkit.WebView

fun webViewShot(view: WebView) : Bitmap {
    val bitmap = Bitmap.createBitmap(view.measuredWidth, view.measuredHeight, Bitmap.Config.ARGB_8888)

    val canvas = Canvas(bitmap)

    val paint = Paint()

    canvas.drawBitmap(bitmap, 0f, canvas.height.toFloat(), paint)

    view.draw(canvas)

    return bitmap
}

fun getHTMLPageTemplate(context: Context) : String {
    val stream = context.resources.assets.open("templates/page_template.html")
    return stream.readBytes().decodeToString()
}
