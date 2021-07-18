package com.vanilla.munato.fragment.logic

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.webkit.WebView
import android.widget.ImageView
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import com.vanilla.munato.R

fun viewShotDebug(view: View) : Bitmap {
    val result = viewShot(view)

    Toast.makeText(view.context, result.width.toString() + " " + result.height.toString(), Toast.LENGTH_LONG).show()

    showDialog(view.context, result)

    return result
}

@SuppressLint("InflateParams")
private fun showDialog(ctx: Context, b: Bitmap) {
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

