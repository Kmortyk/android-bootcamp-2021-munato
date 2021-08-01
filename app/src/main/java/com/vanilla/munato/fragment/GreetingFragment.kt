package com.vanilla.munato.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.vanilla.munato.R
import com.vanilla.munato.activity.EntryActivity

class GreetingFragment : Fragment() {
    companion object {
        @JvmStatic
        fun newInstance() = GreetingFragment().apply { arguments = Bundle().apply {} }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {}
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_greeting, container, false)

        view.findViewById<View>(R.id.greeting_background).setOnClickListener {
            activity?.let {
                val activity = it as EntryActivity
                activity.openLoginFragment()
            }
        }

        return view
    }
}