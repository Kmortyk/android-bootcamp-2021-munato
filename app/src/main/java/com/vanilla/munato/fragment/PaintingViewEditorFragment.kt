package com.vanilla.munato.fragment

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.*
import android.webkit.WebView
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.vanilla.munato.R
import com.vanilla.munato.activity.HomeActivity
import com.vanilla.munato.model.PaintingModel
import com.vanilla.munato.model.PaintingPreview
import com.vanilla.munato.model.PaintingPreviewMethods


// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_JAVASCRIPT_CODE = "javascript_code"
private const val ARG_PAINTING_MODEL = "painting_model"

/**
 * A simple [Fragment] subclass.
 * Use the [PaintingViewFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
open class PaintingViewEditorFragment : Fragment() {
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         * @return A new instance of fragment CreatePaintingFragment.
         */
        @JvmStatic
        fun newInstance(code: String) =
            PaintingViewEditorFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_JAVASCRIPT_CODE, code)
                }
            }
    }

    private var javascriptCode: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setHasOptionsMenu(true)

        arguments?.let {
            if(it.containsKey(ARG_JAVASCRIPT_CODE))
                javascriptCode = it.getString(ARG_JAVASCRIPT_CODE) ?: ""
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.painting_view_editor_action_bar, menu)
        super.onCreateOptionsMenu(menu,inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem)= when (item.itemId) {
        R.id.action_publish -> {
            val webView = requireView().findViewById<WebView>(R.id.web_view)
            fetchScreenshot(webView) { preview ->
                (activity as HomeActivity).openPublishPaintingFragment(javascriptCode, preview)
            }; true
        }
        R.id.action_open_editor -> {
            (activity as HomeActivity).openEditorFragment(javascriptCode); true
        }

        R.id.action_save_to_local_storage -> {
            val webView = requireView().findViewById<WebView>(R.id.web_view)
            fetchScreenshot(webView) { preview ->
                (activity as HomeActivity).saveToLocalStorage(javascriptCode, preview)
            }; true
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

        webView.settings.javaScriptEnabled = true
        template += "<script>${javascriptCode}\n\ndraw(ctx, width, height);</script>"

        // https://stackoverflow.com/questions/37090396/android-webview-doesnt-load-html-sometimes
        webView.postDelayed({
            webView.loadDataWithBaseURL(null, template, null, "UTF-8", null);
        }, 100)

        return view
    }

    private fun fetchScreenshot(webView: WebView, onFetch: (PaintingPreview) -> Unit) {
        webView.evaluateJavascript("canvas.toDataURL();") {
            if(it == null || it == "" || !it.contains(",")) {
                val view = view
                if(view != null) {
                    Snackbar.make(view, "Sorry, can't publish your work \uD83D\uDE13", Snackbar.LENGTH_SHORT).show()
                }
            } else {
                Log.d("PaintingViewEditor", it)

                val preview = PaintingPreviewMethods.fromBase64(it)

                // showPreviewDialog(preview)

                onFetch(preview)
            }
        }
    }

    private fun getHTMLPageTemplate(context: Context) : String {
        val stream = context.resources.assets.open("templates/page_template.html")
        return stream.readBytes().decodeToString()
    }
}