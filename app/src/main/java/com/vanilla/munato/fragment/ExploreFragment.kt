package com.vanilla.munato.fragment

import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.vanilla.munato.activity.HomeActivity
import com.vanilla.munato.R
import com.vanilla.munato.adapter.PaintingsRecyclerViewAdapter
import com.vanilla.munato.model.PaintingModel


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
            PaintingModel("", "kmortyk", "good one", "", 1),
            PaintingModel("", "memphis", "stars", "", 21),
            PaintingModel("", "determin", "fine too", "", 1),
            PaintingModel("", "kettie", "last one", "", 56),
            PaintingModel("", "stephan", "star dance", "", 128),
            PaintingModel("", "marie", "eye u", "", 0),
            PaintingModel("", "kevin", "when it comes", "", 5677),
            PaintingModel("", "evik", "loved", "", 1),
            PaintingModel("", "buster", "best", "", 45),
        )

        val rvExplore = view.findViewById<RecyclerView>(R.id.rv_explore)
        val adapter = PaintingsRecyclerViewAdapter(activity as HomeActivity, data)

        val paddingSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5f, resources.displayMetrics).toInt()

        rvExplore.adapter = adapter
        rvExplore.layoutManager = StaggeredGridLayoutManager(NUM_COLUMNS, RecyclerView.VERTICAL)
        rvExplore.addItemDecoration(PaintingsItemDecoration(paddingSize))

        return view
    }
}