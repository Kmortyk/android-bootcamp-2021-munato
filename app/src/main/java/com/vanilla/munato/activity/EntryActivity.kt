package com.vanilla.munato.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.FragmentTransaction
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.ktx.Firebase
import com.vanilla.munato.R
import com.vanilla.munato.databinding.ActivityEntryBinding
import com.vanilla.munato.fragment.GreetingFragment
import com.vanilla.munato.fragment.LoginFragment
import com.vanilla.munato.fragment.PaintingViewEditorFragment
import java.lang.NullPointerException

/*
*   Необходимо использовать многопоточность
    Необходимо использовать (БД + любую стороннюю библиотеку) ИЛИ сервисы Firebase
    Приложение выполняет только заявленные функции, безопасно и не содержит вредоносного кода.
*  */

class EntryActivity : AppCompatActivity() {
    companion object {
        const val KEY_FIRST_START = "first_start"
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

    private fun firstStart() : Boolean {
        val sp = getSharedPreferences("munato", MODE_PRIVATE)

        if(!sp.contains(KEY_FIRST_START)) {
            val editor = sp.edit()
            editor.putBoolean(KEY_FIRST_START, true)
            editor.apply()

            return true
        }

        return false
    }

    fun openHomeActivity() {
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
    }
}
