package com.vanilla.munato.fragment

import android.app.AlertDialog
import android.content.DialogInterface
import android.graphics.Bitmap
import android.os.Bundle
import android.view.*
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.vanilla.munato.R


// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_JAVASCRIPT_CODE = "javascript_code"
private const val ARG_PAINTING_MODEL = "painting_model"

/**
 * A simple [Fragment] subclass.
 * Use the [PaintingViewFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PaintingViewEditorFragmentDebug : PaintingViewEditorFragment() {
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         * @return A new instance of fragment CreatePaintingFragment.
         */
        @JvmStatic
        fun newInstance(code: String) =
            PaintingViewEditorFragmentDebug().apply {
                arguments = Bundle().apply {
                    putString(ARG_JAVASCRIPT_CODE, code)
                }
            }
    }

    /**
     * Debug function. Shows dialog with preview.
     */
    public fun showPreviewDialog(b: Bitmap) {
        val alertDialog = LayoutInflater.from(context).inflate(R.layout.alert_dialog_image, null)

        val imageView = alertDialog.findViewById<ImageView>(R.id.alert_dialog_image)
        imageView.setImageBitmap(b)

        // show dialog with preview
        AlertDialog.Builder(context)
            .setView(alertDialog)
            .setPositiveButton(android.R.string.ok) { dialogInterface: DialogInterface, _: Int ->
                dialogInterface.dismiss()
            }
            .setNegativeButton(android.R.string.cancel) { dialogInterface: DialogInterface, _: Int ->
                dialogInterface.cancel()
            }
            .show()
    }
}