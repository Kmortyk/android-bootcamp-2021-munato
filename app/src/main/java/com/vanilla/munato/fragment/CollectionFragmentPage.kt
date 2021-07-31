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

class CollectionFragmentPage(private val adapter: RecyclerView.Adapter<*>) : Fragment() {
    companion object {
        @JvmStatic
        fun newInstance(adapter: RecyclerView.Adapter<*>) = CollectionFragmentPage(adapter)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_collection_page, container, false)

        val recyclerView = view.findViewById<RecyclerView>(R.id.collection_page_recycler_view)
        val layoutManager = LinearLayoutManager(context)

        recyclerView.adapter = adapter
        recyclerView.layoutManager = layoutManager

        val dividerItemDecoration = DividerItemDecoration(recyclerView.context, layoutManager.orientation)
        val spaceDrawable = ResourcesCompat.getDrawable(resources, R.drawable.space, null)

        if(spaceDrawable != null) { // in case
            dividerItemDecoration.setDrawable(spaceDrawable)
        }

        recyclerView.addItemDecoration(dividerItemDecoration)

        return view
    }
}