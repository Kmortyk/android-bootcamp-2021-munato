package com.example.munato

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.munato.databinding.ActivityHomeBinding
import com.example.munato.fragment.CreatePaintingFragment
import com.example.munato.fragment.EditorFragment
import com.example.munato.fragment.ExploreFragment

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
                R.id.itm_menu_create -> {
                    ftx.replace(R.id.home_fragment_container, CreatePaintingFragment.newInstance())
                }
            }

            ftx.addToBackStack("select_menu")
            ftx.commit()

            true
        }
    }

    fun openEditorFragment() {
        val ftx = supportFragmentManager.beginTransaction()
        ftx.add(R.id.home_fragment_container, ExploreFragment.newInstance())
        ftx.addToBackStack(null)
        ftx.commit()
    }

    fun openStartingFragment() {
        val ftx = supportFragmentManager.beginTransaction()
        ftx.add(R.id.home_fragment_container, ExploreFragment.newInstance())
        ftx.commit()
    }
}