package com.vanilla.munato.fragment

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Bundle
import android.util.Log
import android.view.*
import android.webkit.WebView
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.vanilla.munato.R
import com.vanilla.munato.activity.HomeActivity
import com.vanilla.munato.model.PaintingModel


// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_JAVASCRIPT_CODE = "javascript_code"
private const val ARG_PAINTING_MODEL = "painting_model"

/**
 * A simple [Fragment] subclass.
 * Use the [PaintingViewFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PaintingViewEditorFragment : Fragment() {
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         * @return A new instance of fragment CreatePaintingFragment.
         */
        @JvmStatic
        fun newInstance(paintingModel: PaintingModel?, code: String?) =
            PaintingViewEditorFragment().apply {
                arguments = Bundle().apply {
                    if(paintingModel != null)
                        putParcelable(ARG_PAINTING_MODEL, paintingModel)
                    if(code != null)
                        putString(ARG_JAVASCRIPT_CODE, code)
                }
            }
    }

    private var paintingModel: PaintingModel? = null
    private var javascriptCode: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setHasOptionsMenu(true)

        arguments?.let {
            if(it.containsKey(ARG_PAINTING_MODEL))
                paintingModel = it.getParcelable(ARG_PAINTING_MODEL)
            if(it.containsKey(ARG_JAVASCRIPT_CODE))
                javascriptCode = it.getString(ARG_JAVASCRIPT_CODE)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.painting_view_editor_action_bar, menu)
        super.onCreateOptionsMenu(menu,inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem)= when (item.itemId) {
        R.id.action_publish -> {
            val code = when {
                javascriptCode != null -> { javascriptCode!! }
                paintingModel != null -> { paintingModel!!.code!! }
                else -> {
                    Log.e("PaintingEditorView", "empty code to publish")
                    ""
                }
            }

            val webView = requireView().findViewById<WebView>(R.id.web_view)
            val preview = takeScreenshotDebug(webView)

            // (activity as HomeActivity).openPublishPaintingFragment(code, preview)

            true
        }
        R.id.action_open_editor -> {
            val activity = activity as HomeActivity

            if(javascriptCode != null) {
                activity.openEditorFragment(javascriptCode)
            } else {
                activity.openEditorFragment(paintingModel?.code)
            }
            true
        }
        else -> {
            super.onOptionsItemSelected(item)
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_painting_view, container, false)
        val activity = activity as HomeActivity
        val webView = view.findViewById<WebView>(R.id.web_view)
        var template = getHTMLPageTemplate(activity)
        // val btnOpenEditor = view.findViewById<FloatingActionButton>(R.id.btn_open_editor)

        if(javascriptCode != null) {
            webView.settings.javaScriptEnabled = true
            template += "<script>${javascriptCode}</script>"
        } else if(paintingModel != null) {
            webView.settings.javaScriptEnabled = true
            template += "<script>${paintingModel!!.code}</script>"
        }

        template += "<script>draw(ctx, canvas);</script>"

        // https://stackoverflow.com/questions/37090396/android-webview-doesnt-load-html-sometimes
        webView.postDelayed({
            webView.loadDataWithBaseURL(null, template, null, "UTF-8", null);
        }, 100)

        return view
    }

    fun takeScreenshotDebug(view: WebView) : Bitmap {
        val result = takeScreenshot(view)

        Toast.makeText(view.context, result.width.toString() + " " + result.height.toString(), Toast.LENGTH_LONG).show()

        showPreviewDialog(view.context, result)

        return result
    }

    @SuppressLint("InflateParams")
    private fun showPreviewDialog(ctx: Context, b: Bitmap) {
        val alertDialog = LayoutInflater.from(ctx).inflate(R.layout.alert_dialog_image, null)

        val imageView = alertDialog.findViewById<ImageView>(R.id.alert_dialog_image)
        imageView.setImageBitmap(b)

        // show dialog with preview
        AlertDialog.Builder(ctx)
            .setView(alertDialog)
            .setPositiveButton(android.R.string.ok) { dialogInterface: DialogInterface, _: Int ->
                dialogInterface.dismiss()
            }
            .setNegativeButton(android.R.string.cancel) { dialogInterface: DialogInterface, _: Int ->
                dialogInterface.cancel()
            }
            .show()
    }

    private fun takeScreenshot(webView: WebView) : Bitmap {
        val bitmap = Bitmap.createBitmap(webView.width, webView.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)

        webView.draw(canvas)

        return bitmap
    }

    private fun getHTMLPageTemplate(context: Context) : String {
        val stream = context.resources.assets.open("templates/page_template.html")
        return stream.readBytes().decodeToString()
    }
}