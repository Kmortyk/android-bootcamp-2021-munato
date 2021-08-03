package com.vanilla.munato.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.vanilla.munato.R
import com.vanilla.munato.adapter.PresetsRecyclerViewAdapter

class PresetsFragment : Fragment() {
    companion object {
        @JvmStatic
        fun newInstance() = PresetsFragment().apply {}
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
                val fragment = it.fragments.lastOrNull()

                (fragment as EditorFragment).addCodeFragment(code)

                it.popBackStack()
            }
        }

        return view
    }
}