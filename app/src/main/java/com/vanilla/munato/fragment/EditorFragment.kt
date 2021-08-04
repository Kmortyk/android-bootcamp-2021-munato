package com.vanilla.munato.fragment

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import com.vanilla.munato.activity.HomeActivity
import com.vanilla.munato.R
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
    private var editor: CodeEditor? = null // TODO check after screen rotating

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setHasOptionsMenu(true)

        arguments?.let {
            if(it.containsKey(ARG_CODE))
                initCode = it.getString(ARG_CODE)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.editor_action_bar, menu)
        super.onCreateOptionsMenu(menu,inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem) : Boolean {
        val activity = activity as HomeActivity

        return when (item.itemId) {
            R.id.action_open_painting_view -> {
                val code = if (editor != null) {
                    editor!!.text.toString()
                } else {
                    ""
                }

                activity.returnFromEditorFragment(code)

                true
            }
            R.id.action_open_presets -> {
                activity.openPresetsFragment()

                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val activity = activity as HomeActivity
        val view = inflater.inflate(R.layout.fragment_editor, container, false)

        editor = view.findViewById(R.id.code_editor)
        editor!!.apply {
            setEditorLanguage(UniversalLanguage(JavaScriptDescription()))
            typefaceText = ResourcesCompat.getFont(activity, R.font.jetbrainsmono_medium)
            if(initCode != null) {
                setText(initCode)
            }
        }

        return view
    }

    fun addCodeFragment(code: String) {
        editor?.let { editor ->
            activity?.let { activity ->
                val pos = editor.cursor.left
                var editorCode = editor.text.toString()

                editorCode =
                    editorCode.substring(0, pos) +
                    code +
                    editorCode.substring(pos, editorCode.length)

                initCode = editorCode
            }
        }
    }
}