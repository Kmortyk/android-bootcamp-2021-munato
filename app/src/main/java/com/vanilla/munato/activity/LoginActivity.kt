package com.vanilla.munato.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.ktx.Firebase
import com.vanilla.munato.R
import com.vanilla.munato.databinding.ActivityLoginBinding
import java.lang.NullPointerException

/*
*   Необходимо использовать многопоточность
    Необходимо использовать (БД + любую стороннюю библиотеку) ИЛИ сервисы Firebase
    Приложение выполняет только заявленные функции, безопасно и не содержит вредоносного кода.
*  */

class LoginActivity : AppCompatActivity() {

    // See: https://developer.android.com/training/basics/intents/result
    private val signInLauncher = registerForActivityResult(
        FirebaseAuthUIActivityResultContract()
    ) { res ->
        this.onSignInResult(res)
    }

    // Choose authentication providers
    private val providers = arrayListOf(
        AuthUI.IdpConfig.EmailBuilder().build()
    )

    // Create and launch sign-in intent
    private val signInIntent = AuthUI.getInstance()
        .createSignInIntentBuilder()
        .setAvailableProviders(providers)
        .setLogo(R.mipmap.ic_launcher)
        .build()

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.hide()

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnLogIn.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }

        binding.btnLogOut.setOnClickListener {
            AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener {
                    runFirebaseLogin()
                }
                .addOnCanceledListener {
                    Snackbar.make(it, R.string.log_out_error_text, Snackbar.LENGTH_LONG).show()
                }
        }
    }

    override fun onResume() {
        super.onResume()

        val user = FirebaseAuth.getInstance().currentUser
        if (user == null) {
            runFirebaseLogin()
        }
    }

    private fun runFirebaseLogin() {
        signInLauncher.launch(signInIntent)
    }

    private fun onSignInResult(result: FirebaseAuthUIAuthenticationResult) {
        val response = result.idpResponse
        if (result.resultCode == RESULT_OK) {
            // Successfully signed in
            val user = FirebaseAuth.getInstance().currentUser
            if (user != null) {
                return
            }
        }

        val errorCode = response?.error?.errorCode
        errorCode?.let {
            Snackbar.make(binding.root, "${resources.getString(R.string.login_err_code)} $it", Snackbar.LENGTH_LONG).show()
        }
    }
}

public fun onUserLogout(activity: Activity?) {
    activity?.let {
        Toast.makeText(it, R.string.log_out_another_device_text, Toast.LENGTH_LONG).show()
        it.finish()
    }
}
