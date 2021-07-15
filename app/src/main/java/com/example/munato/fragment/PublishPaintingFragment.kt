package com.example.munato.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.munato.R

private const val ARG_CODE = "code"

/**
 * A simple [Fragment] subclass.
 * Use the [PublishPaintingFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PublishPaintingFragment : Fragment() {
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment PublishPaintingFragment.
         */
        @JvmStatic
        fun newInstance(code: String) =
            PublishPaintingFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_CODE, code)
                }
            }
    }

    private var code: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            code = it.getString(ARG_CODE)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_publish_painting, container, false)

        return view
    }
}