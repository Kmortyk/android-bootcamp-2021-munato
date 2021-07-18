package com.vanilla.munato.fragment.logic

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.view.View
import android.webkit.WebView

fun viewShot(view: View) : Bitmap {
    val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)

    val canvas = Canvas(bitmap)

    view.draw(canvas)

    return bitmap
}

fun getHTMLPageTemplate(context: Context) : String {
    val stream = context.resources.assets.open("templates/page_template.html")
    return stream.readBytes().decodeToString()
}
