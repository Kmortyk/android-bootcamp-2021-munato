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
import com.google.android.material.tabs.TabLayout
import com.vanilla.munato.activity.HomeActivity
import com.vanilla.munato.adapter.FavouritePaintingsRecyclerViewAdapter
import com.vanilla.munato.adapter.MyPaintingsRecyclerViewAdapter


// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"

/**
 * A simple [Fragment] subclass.
 * Use the [CollectionFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CollectionFragment : Fragment() {
    companion object {
        @JvmStatic
        fun newInstance() = CollectionFragment().apply {}
    }

    private var recyclerView: RecyclerView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_collection, container, false)

        recyclerView = view.findViewById(R.id.fragment_collection_recycler_view)

        recyclerView?.let { recyclerView ->
            val layoutManager = LinearLayoutManager(activity)
            recyclerView.layoutManager = layoutManager

            selectMyTab()

            val dividerItemDecoration = DividerItemDecoration(recyclerView.context, layoutManager.orientation)
            val spaceDrawable = ResourcesCompat.getDrawable(resources, R.drawable.space, null)

            if(spaceDrawable != null) { // in case
                dividerItemDecoration.setDrawable(spaceDrawable)
            }

            recyclerView.addItemDecoration(dividerItemDecoration)
        }

        view.findViewById<TabLayout>(R.id.collection_tab_layout).addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                if(tab != null) {
                    when(tab.id) {
                        R.id.collection_tab_my -> {
                            selectMyTab()
                        }
                        R.id.collection_tab_favourites -> {
                            selectFavouritesTab()
                        }
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) { /* */ }

            override fun onTabReselected(tab: TabLayout.Tab?) { /* */ }
        })

        return view
    }

    fun selectMyTab() {
        val adapter = MyPaintingsRecyclerViewAdapter()
        recyclerView?.adapter = MyPaintingsRecyclerViewAdapter()
        (activity as HomeActivity).loadMyPaintingsFromLocalStorage {
            adapter.setData(it)
        }
    }

    fun selectFavouritesTab() {
        val adapter = FavouritePaintingsRecyclerViewAdapter()
        recyclerView?.adapter = adapter
    }
}