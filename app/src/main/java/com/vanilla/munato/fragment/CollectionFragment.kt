package com.vanilla.munato.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.vanilla.munato.R
import com.vanilla.munato.adapter.CollectionRecyclerViewAdapter
import com.vanilla.munato.model.PaintingModel
import com.google.android.material.tabs.TabLayout


// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"

/**
 * A simple [Fragment] subclass.
 * Use the [CollectionFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CollectionFragment : Fragment() {
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         * @return A new instance of fragment CollectionFragment.
         */
        @JvmStatic
        fun newInstance(/*param1: String*/) =
            CollectionFragment().apply {
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
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_collection, container, false)

        val recyclerView = view.findViewById<RecyclerView>(R.id.fragment_collection_recycler_view)
        val layoutManager = LinearLayoutManager(activity)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = CollectionRecyclerViewAdapter(listOf(
            PaintingModel(1, "kmortyk", "good one", "", 1, ""),
            PaintingModel(2, "memphis", "stars", "", 21, ""),
            PaintingModel(3, "determin", "fine too", "", 1, ""),
            PaintingModel(4, "kettie", "last one", "", 56, ""),
            PaintingModel(5, "stephan", "star dance", "", 128, ""),
            PaintingModel(6, "marie", "eye u", "", 0, ""),
            PaintingModel(7, "kevin", "when it comes", "", 5677, ""),
            PaintingModel(8, "evik", "loved", "", 1, ""),
            PaintingModel(9, "buster", "best", "", 45, ""),
        ))

        val dividerItemDecoration = DividerItemDecoration(recyclerView.context, layoutManager.orientation)
        val spaceDrawable = ResourcesCompat.getDrawable(resources, R.drawable.space, null)

        if(spaceDrawable != null) { // in case
            dividerItemDecoration.setDrawable(spaceDrawable)
        }

        recyclerView.addItemDecoration(dividerItemDecoration)

        view.findViewById<TabLayout>(R.id.collection_tab_layout).addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                if(tab != null) {
                    when(tab.id) {
                        R.id.collection_tab_my -> {

                        }

                        R.id.collection_tab_favourites -> {

                        }
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) { /* */ }

            override fun onTabReselected(tab: TabLayout.Tab?) { /* */ }
        })

        return view
    }
}