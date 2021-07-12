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
import com.google.android.material.floatingactionbutton.FloatingActionButton

// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_JAVASCRIPT_CODE = "javascript_code"

/**
 * A simple [Fragment] subclass.
 * Use the [CreatePaintingFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CreatePaintingFragment : Fragment() {
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         * @return A new instance of fragment CreatePaintingFragment.
         */
        @JvmStatic
        fun newInstance(javascriptCode: String) =
            CreatePaintingFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_JAVASCRIPT_CODE, javascriptCode)
                }
            }
    }

    private var javascriptCode: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            javascriptCode = it.getString(ARG_JAVASCRIPT_CODE)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_painting_preview, container, false)
        val btnOpenEditor = view.findViewById<FloatingActionButton>(R.id.btn_open_editor)
        val activity = activity as HomeActivity

        btnOpenEditor.setOnClickListener {
            activity.openEditorFragment()
        }

        Log.d("a", javascriptCode+"")

        val webView = view.findViewById<WebView>(R.id.web_view)
        val template = getHTMLPageTemplate(activity)

        Log.d("a", template+"")

        webView.loadDataWithBaseURL(null, template,
            "text/html", "utf-8", null);

        // Inflate the layout for this fragment
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