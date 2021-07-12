package com.example.munato.fragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import com.example.munato.HomeActivity
import com.example.munato.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import io.github.rosemoe.editor.langs.desc.JavaScriptDescription
import io.github.rosemoe.editor.langs.universal.UniversalLanguage
import io.github.rosemoe.editor.widget.CodeEditor

// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_CODE = "code"

/**
 * A simple [Fragment] subclass.
 * Use the [EditorFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class EditorFragment : Fragment() {
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         * @return A new instance of fragment EditorFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(code: String?) =
            EditorFragment().apply {
                arguments = Bundle().apply {
                    if(code != null)
                        putString(ARG_CODE, code)
                }
            }
    }

    private var initCode: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            if(it.containsKey(ARG_CODE))
                initCode = it.getString(ARG_CODE)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_editor, container, false)
        val editor = view.findViewById<CodeEditor>(R.id.code_editor)

        editor.setEditorLanguage(UniversalLanguage(JavaScriptDescription()))

        if(initCode != null) {
            editor.setText(initCode)
        }

        view.findViewById<FloatingActionButton>(R.id.btn_return_from_editor).setOnClickListener {
            (activity as HomeActivity).returnFromEditorFragment(editor.text.toString())
        }

        // Inflate the layout for this fragment
        return view
    }
}