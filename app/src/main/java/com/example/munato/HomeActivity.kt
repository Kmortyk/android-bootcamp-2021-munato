package com.example.munato

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import com.example.munato.databinding.ActivityHomeBinding
import com.example.munato.fragment.*
import com.example.munato.model.PaintingModel

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        openStartingFragment()

        binding.bottomNavigationView.setOnItemSelectedListener {
            val ftx = supportFragmentManager.beginTransaction()

            when(it.itemId) {
                R.id.itm_menu_explore -> {
                    ftx.replace(R.id.home_fragment_container, ExploreFragment.newInstance())
                }
                R.id.itm_menu_collection -> {
                    ftx.replace(R.id.home_fragment_container, CollectionFragment.newInstance())
                }
                R.id.itm_menu_create -> {
                    ftx.replace(R.id.home_fragment_container, PaintingViewFragment.newInstance(null, getScriptTemplate(this)))
                }
            }

            ftx.addToBackStack("select_menu")
            ftx.commit()

            true
        }
    }

    fun getScriptTemplate(context: Context) : String {
        val stream = context.resources.assets.open("templates/script_template.js")
        return stream.readBytes().decodeToString()
    }

    fun openEditorFragment(code: String?) {
        val ftx = supportFragmentManager.beginTransaction()
        ftx.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
        ftx.replace(R.id.home_fragment_container, EditorFragment.newInstance(code))
        ftx.commit()
    }

    fun returnFromEditorFragment(code: String) {
        val ftx = supportFragmentManager.beginTransaction()
        ftx.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE)
        ftx.replace(R.id.home_fragment_container, PaintingViewFragment.newInstance(null, code))
        ftx.commit()
    }

    private fun openStartingFragment() {
        val ftx = supportFragmentManager.beginTransaction()
        ftx.add(R.id.home_fragment_container, ExploreFragment.newInstance())
        ftx.commit()
    }

    fun openPublishPaintingFragment(code: String) {
        val ftx = supportFragmentManager.beginTransaction()
        ftx.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
        ftx.replace(R.id.home_fragment_container, PublishPaintingFragment.newInstance(code))
        ftx.commit()
    }
}