package com.example.munato.fragment.logic.tools

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.webkit.WebView
import android.widget.ImageView
import com.example.munato.R

class ScreenshotWebViewClientDebug(private val ctx: Context) : ScreenshotWebViewClient() {

    override fun onPageFinished(view: WebView?, url: String?) {
        super.onPageFinished(view, url) // if view is null, exception will be here

        val bitmap = webViewShot(view!!)

        showDialog(bitmap)
    }

    @SuppressLint("InflateParams")
    private fun showDialog(b: Bitmap) {
        val alertDialog = LayoutInflater.from(ctx).inflate(R.layout.alert_dialog_image, null)

        val imageView = alertDialog.findViewById<ImageView>(R.id.alert_dialog_image)
        imageView.setImageBitmap(b)

        val dBuilder = AlertDialog.Builder(ctx)
        dBuilder.setView(alertDialog)

        configDialogButtons(dBuilder)

        dBuilder.show()
    }

    private fun configDialogButtons(dBuilder: AlertDialog.Builder) {
        dBuilder.setPositiveButton(android.R.string.ok) {
                dialogInterface: DialogInterface, _: Int ->
            dialogInterface.dismiss()
        }

        dBuilder.setNegativeButton(android.R.string.cancel) {
                dialogInterface: DialogInterface, _: Int ->
            dialogInterface.cancel()
        }
    }
}