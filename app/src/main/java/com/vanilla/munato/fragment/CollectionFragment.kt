package com.vanilla.munato.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.vanilla.munato.R
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.vanilla.munato.activity.HomeActivity
import com.vanilla.munato.adapter.FavouritePaintingsRecyclerViewAdapter
import com.vanilla.munato.adapter.MyPaintingsRecyclerViewAdapter

class CollectionPagerAdapter(
    private val activity: HomeActivity,
    private val myCollectionAdapter: MyPaintingsRecyclerViewAdapter,
    private val favouriteCollectionAdapter: FavouritePaintingsRecyclerViewAdapter) : FragmentStateAdapter(activity) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when(position) {
            0 -> {
                activity.apply {
                    loadMyPaintingsFromLocalStorage {
                        runOnUiThread {
                            myCollectionAdapter.setData(it)
                        }
                    }
                }

                CollectionFragmentPage.newInstance(myCollectionAdapter)
            }
            else -> {
                activity.apply {
                    loadFavouritePaintings {
                        runOnUiThread {
                            favouriteCollectionAdapter.addData(it)
                        }
                    }
                }

                CollectionFragmentPage.newInstance(favouriteCollectionAdapter)
            }
        }
    }
}

class CollectionFragment : Fragment() {
    companion object {
        @JvmStatic
        fun newInstance() = CollectionFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_collection, container, false)
        val activity = activity as HomeActivity

        val viewPagerAdapter = CollectionPagerAdapter(activity,
            MyPaintingsRecyclerViewAdapter { activity.openEditorFragment(it) },
            FavouritePaintingsRecyclerViewAdapter(),)

        val viewPager = view.findViewById<ViewPager2>(R.id.collection_view_pager)

        viewPager.adapter = viewPagerAdapter

        val tabLayout = view.findViewById<TabLayout>(R.id.collection_tab_layout)

        val tabStrategy = TabLayoutMediator.TabConfigurationStrategy { tab, position -> when (position) {
            0 -> {
                tab.text = getString(R.string.fragment_collection_my)
                tab.icon = ResourcesCompat.getDrawable(activity.resources, R.drawable.outline_face_24, null)
            }
            else -> {
                tab.text = getString(R.string.fragment_collection_favourites)
                tab.icon = ResourcesCompat.getDrawable(activity.resources, R.drawable.outline_favorite_border_24, null)
            }
        } }

        TabLayoutMediator(tabLayout, viewPager, tabStrategy).attach()

        return view
    }
}
