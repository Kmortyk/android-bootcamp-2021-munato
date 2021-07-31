package com.vanilla.munato.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.FragmentTransaction
import com.vanilla.munato.R
import com.vanilla.munato.fragment.GreetingFragment
import com.vanilla.munato.fragment.LoginFragment

class EntryActivity : AppCompatActivity() {
    companion object {
        const val KEY_FIRST_START = "first_start"
        const val SHARED_PREFS_NAME = "munato"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_entry)

        supportActionBar?.hide()

        if(firstStart()) {
            openGreetingFragment()
        } else {
            openLoginFragment()
        }
    }

    private fun openGreetingFragment() {
        val ftx = supportFragmentManager.beginTransaction()
        ftx.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
        ftx.replace(R.id.entry_fragment_container, GreetingFragment.newInstance())
        ftx.commit()
    }

    fun openLoginFragment() {
        val ftx = supportFragmentManager.beginTransaction()
        ftx.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
        ftx.replace(R.id.entry_fragment_container, LoginFragment.newInstance())
        ftx.commit()
    }

    fun firstStart() : Boolean {
        return !getSharedPreferences(SHARED_PREFS_NAME, MODE_PRIVATE).contains(KEY_FIRST_START)
    }

    fun addFirstStartFlag() {
        val sp = getSharedPreferences(SHARED_PREFS_NAME, MODE_PRIVATE)
        val editor = sp.edit()
        editor.putBoolean(KEY_FIRST_START, true)
        editor.apply()
    }

    fun openHomeActivity() {
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
    }
}
