package com.example.munato.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.munato.HomeActivity
import com.example.munato.R
import com.google.android.material.floatingactionbutton.FloatingActionButton

// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"

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
        fun newInstance(/*param1: String*/) =
            CreatePaintingFragment().apply {
                arguments = Bundle().apply {
                    // putString(ARG_PARAM1, param1)
                }
            }
    }

    // private var param1: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            // param1 = it.getString(ARG_PARAM1)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_create_painting, container, false)

        val btnOpenEditor = view.findViewById<FloatingActionButton>(R.id.btn_open_editor)

        btnOpenEditor.setOnClickListener {
            Log.d("a", activity.toString())
            (activity as HomeActivity).openEditorFragment()
        }

        // Inflate the layout for this fragment
        return view
    }
}