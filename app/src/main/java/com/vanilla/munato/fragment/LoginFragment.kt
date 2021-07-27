package com.vanilla.munato.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.vanilla.munato.R
import com.vanilla.munato.activity.EntryActivity

class LoginFragment : Fragment() {
    companion object {
        @JvmStatic
        fun newInstance() =
            LoginFragment().apply {
                arguments = Bundle().apply {}
            }
    }

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {}
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_login, container, false)

        activity?.let {
            val activity = it as EntryActivity

            view.findViewById<Button>(R.id.btn_log_in).setOnClickListener {
                activity.openHomeActivity()
            }

            view.findViewById<Button>(R.id.btn_log_out).setOnClickListener {
                AuthUI.getInstance()
                    .signOut(activity)
                    .addOnCompleteListener {
                        runFirebaseLogin()
                    }
                    .addOnCanceledListener {
                        Snackbar.make(it, R.string.log_out_error_text, Snackbar.LENGTH_LONG).show()
                    }
            }
        }

        return view
    }

    override fun onResume() {
        super.onResume()

        val user = FirebaseAuth.getInstance().currentUser
        if (user == null) {
            runFirebaseLogin()
        }
    }

    private fun onSignInResult(result: FirebaseAuthUIAuthenticationResult) {
        val response = result.idpResponse

        // check if we successfully signed in
        if (result.resultCode == AppCompatActivity.RESULT_OK) {
            val user = FirebaseAuth.getInstance().currentUser
            if (user != null) {
                return
            }
        }

        val errorCode = response?.error?.errorCode
        errorCode?.let {
            view?.let {
                Snackbar.make(it, "${resources.getString(R.string.login_err_code)} $it", Snackbar.LENGTH_LONG).show()
            }
        }
    }

    private fun runFirebaseLogin() {
        signInLauncher.launch(signInIntent)
    }

//    public fun onUserLogout(activity: Activity?) {
//        activity?.let {
//            Toast.makeText(it, R.string.log_out_another_device_text, Toast.LENGTH_LONG).show()
//            it.finish()
//        }
//    }
}