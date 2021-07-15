package com.example.munato.fragment.logic.tools

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import com.google.android.material.snackbar.Snackbar

open class ScreenshotWebViewClient : WebViewClient() {

    override fun onPageFinished(view: WebView?, url: String?) {
        super.onPageFinished(view, url)

        if (view == null) {
            throw NullPointerException("WebView is null in onPageFinished in " + javaClass.canonicalName)
        }

        configureWebViewMeasure(view)

        val bitmap = webViewShot(view)

        Snackbar.make(view, "It works!", Snackbar.LENGTH_LONG).show()
    }

    private fun configureWebViewMeasure(view: WebView) {
        view.measure(
            View.MeasureSpec.makeMeasureSpec(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED),
            View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        )

        view.layout(0, 0, view.measuredWidth, view.measuredHeight)
    }

    protected fun webViewShot(view: WebView) : Bitmap {
        val bitmap = Bitmap.createBitmap(view.measuredWidth, view.measuredHeight, Bitmap.Config.ARGB_8888)

        val canvas = Canvas(bitmap)

        val paint = Paint()

        canvas.drawBitmap(bitmap, 0f, canvas.height.toFloat(), paint)

        view.draw(canvas)

        return bitmap
    }
}