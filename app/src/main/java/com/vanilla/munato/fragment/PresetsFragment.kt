package com.vanilla.munato.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.vanilla.munato.R
import com.vanilla.munato.adapter.PresetsRecyclerViewAdapter

class PresetsFragment(private val editorFragment: EditorFragment) : Fragment() {
    companion object {
        @JvmStatic
        fun newInstance(editorFragment: EditorFragment) = PresetsFragment(editorFragment).apply {}
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_presets, container, false)

        val rvPresets = view.findViewById<RecyclerView>(R.id.rv_presets)

        rvPresets.layoutManager = LinearLayoutManager(activity)
        rvPresets.adapter = PresetsRecyclerViewAdapter { code ->
            val fm = activity?.supportFragmentManager
            fm?.let {
                editorFragment.addCodeFragment(code)

                it.popBackStackImmediate()
            }
        }

        return view
    }
}