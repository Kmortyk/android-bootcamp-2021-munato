package com.vanilla.munato.fragment

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Button
import android.widget.FrameLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.vanilla.munato.R
import com.vanilla.munato.activity.EntryActivity
import egolabsapps.basicodemine.videolayout.VideoLayout

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
                if(activity.firstStart()) {
                    activity.addFirstStartFlag()
                }

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

        val videoLayout = view?.findViewById<VideoLayout>(R.id.videoLayout)
        videoLayout?.onResumeVideoLayout()

        view?.findViewById<FrameLayout>(R.id.login_ui)?.visibility = View.INVISIBLE

        val user = FirebaseAuth.getInstance().currentUser
        if (user == null) {
            runFirebaseLogin()
            return
        }

        view?.findViewById<FrameLayout>(R.id.login_ui)?.visibility = View.VISIBLE
        view?.findViewById<TextView>(R.id.tvHelloUser)?.text = helloUserText(user.displayName)
        view?.findViewById<TextView>(R.id.tvEmail)?.text = emailText(user.email)
    }

    private fun helloUserText(userFromDB: String?): String {
        var userName = resources.getText(R.string.log_in_hello_user_placeholder)

        userFromDB?.let {
            userName = it
        }

        return resources.getText(R.string.log_in_hello_user_text).toString() + " " + userName + "!"
    }

    private fun emailText(emailFromDB: String?): String {
        emailFromDB?.let {
            return resources.getText(R.string.log_in_email_prefix).toString() + " " + it
        }

        return ""
    }

    override fun onPause() {
        super.onPause()

        val videoLayout = view?.findViewById<VideoLayout>(R.id.videoLayout)
        videoLayout?.onPauseVideoLayout()
    }

    override fun onDestroy() {
        super.onDestroy()

        val videoLayout = view?.findViewById<VideoLayout>(R.id.videoLayout)
        videoLayout?.onDestroyVideoLayout()
    }

    private fun onSignInResult(result: FirebaseAuthUIAuthenticationResult) {
        val response = result.idpResponse

        // check if we successfully signed in
        if (result.resultCode != AppCompatActivity.RESULT_OK) {
            val errorCode = response?.error?.errorCode
            errorCode?.let {
                view?.let {
                    Snackbar.make(it, "${resources.getString(R.string.login_err_code)} $it", Snackbar.LENGTH_LONG).show()
                }
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