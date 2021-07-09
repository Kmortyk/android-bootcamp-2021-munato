package com.example.munato

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.munato.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.bottomNavigationView.setOnItemSelectedListener {
            when(it.itemId) {
                R.id.itm_menu_explore -> {
                }
                R.id.itm_menu_create -> {
                }
            }

            true
        }

    }
}