package com.example.munato.fragment

import android.content.res.Resources
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.munato.R
import com.example.munato.adapter.PaintingsRecyclerViewAdapter
import com.example.munato.model.PaintingModel


/**
 * A simple [Fragment] subclass.
 * Use the [ExploreFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ExploreFragment : Fragment() {
    companion object {
        private const val ARG_PARAM1 = "param1"
        private const val NUM_COLUMNS = 3
        private const val LOG_TAG = "ExploreFragment"

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         * @return A new instance of fragment ExploreFragment.
         */

        @JvmStatic
        fun newInstance(/*param1: String*/) =
            ExploreFragment().apply {
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
        val view = inflater.inflate(R.layout.fragment_explore, container, false)

        // TODO test data
        val data = listOf(
            PaintingModel(1, "kmortyk", "good one", "", 1, ""),
            PaintingModel(2, "memphis", "stars", "", 21, ""),
            PaintingModel(3, "determin", "fine too", "", 1, ""),
            PaintingModel(4, "kettie", "last one", "", 56, ""),
            PaintingModel(5, "stephan", "star dance", "", 128, ""),
            PaintingModel(6, "marie", "eye u", "", 0, ""),
            PaintingModel(7, "kevin", "when it comes", "", 5677, ""),
            PaintingModel(8, "evik", "loved", "", 1, ""),
            PaintingModel(9, "buster", "best", "", 45, ""),
        )

        val rvExplore = view.findViewById<RecyclerView>(R.id.rv_explore)
        val adapter = PaintingsRecyclerViewAdapter(data)

        val paddingSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10f, resources.displayMetrics).toInt()

        rvExplore.adapter = adapter
        rvExplore.layoutManager = StaggeredGridLayoutManager(NUM_COLUMNS, RecyclerView.VERTICAL)
        rvExplore.addItemDecoration(PaintingsItemDecoration(paddingSize))

        return view
    }
}