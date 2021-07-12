package com.example.munato.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import com.example.munato.HomeActivity
import com.example.munato.R
import com.example.munato.model.PaintingModel
import com.google.android.material.floatingactionbutton.FloatingActionButton

// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_JAVASCRIPT_CODE = "javascript_code"
private const val ARG_PAINTING_MODEL = "painting_model"

/**
 * A simple [Fragment] subclass.
 * Use the [PaintingViewFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PaintingViewFragment : Fragment() {
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         * @return A new instance of fragment CreatePaintingFragment.
         */
        @JvmStatic
        fun newInstance(paintingModel: PaintingModel?, code: String?) =
            PaintingViewFragment().apply {
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
        arguments?.let {
            if(it.containsKey(ARG_PAINTING_MODEL))
                paintingModel = it.getParcelable(ARG_PAINTING_MODEL)
            if(it.containsKey(ARG_JAVASCRIPT_CODE))
                javascriptCode = it.getString(ARG_JAVASCRIPT_CODE)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_painting_view, container, false)
        val btnOpenEditor = view.findViewById<FloatingActionButton>(R.id.btn_open_editor)
        val activity = activity as HomeActivity

        btnOpenEditor.setOnClickListener {
            activity.openEditorFragment(paintingModel?.code)
        }

        val webView = view.findViewById<WebView>(R.id.web_view)
        var template = getHTMLPageTemplate(activity)

        if(javascriptCode != null) {
            webView.settings.javaScriptEnabled = true
            template = "<script>${javascriptCode}</script>" + template
        } else if(paintingModel != null) {
            webView.settings.javaScriptEnabled = true
            template = "<script>${paintingModel!!.code}</script>" + template
        }

        Log.d("a", template)

        webView.loadDataWithBaseURL(null, template, "text/html", "utf-8", null)

        return view
    }

    fun getScriptTemplate(context: Context) : String {
        val stream = context.resources.assets.open("templates/script_template.js")
        return stream.readBytes().decodeToString()
    }

    fun getHTMLPageTemplate(context: Context) : String {
        val stream = context.resources.assets.open("templates/page_template.html")
        return stream.readBytes().decodeToString()
    }
}