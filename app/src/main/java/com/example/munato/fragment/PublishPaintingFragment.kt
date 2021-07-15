package com.example.munato.fragment

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Context.CLIPBOARD_SERVICE
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.example.munato.R
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.Snackbar
import java.security.SecureRandom


private const val ARG_CODE = "code"
const val PAINTING_ID_SIZE = 256

/**
 * A simple [Fragment] subclass.
 * Use the [PublishPaintingFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PublishPaintingFragment : Fragment() {
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment PublishPaintingFragment.
         */
        @JvmStatic
        fun newInstance(code: String) =
            PublishPaintingFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_CODE, code)
                }
            }
    }

    private var code: String? = null
    private var paintingID: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            code = it.getString(ARG_CODE)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_publish_painting, container, false)
        val tvPaintingID = view.findViewById<TextView>(R.id.tv_painting_id)
        val cardViewPaintingIDBar = view.findViewById<CardView>(R.id.card_view_painting_id_bar)
        val btnRecreatePaintingID = view.findViewById<MaterialButton>(R.id.btn_publish_recreate_paintign_id)
        val btnCopyPaintingID = view.findViewById<MaterialButton>(R.id.btn_publish_copy_painting_id)

        paintingID = generatePaintingID()

        tvPaintingID.text = paintingID

        cardViewPaintingIDBar.setOnClickListener {
            if(tvPaintingID.isVisible) {
                tvPaintingID.visibility = View.GONE
            } else {
                tvPaintingID.visibility = View.VISIBLE
            }
        }

        btnRecreatePaintingID.setOnClickListener {
            paintingID = generatePaintingID()

            tvPaintingID.text = paintingID
        }

        btnCopyPaintingID.setOnClickListener {
            val activity = activity

            activity?.let {
                val sdk = Build.VERSION.SDK_INT
                if (sdk < Build.VERSION_CODES.HONEYCOMB) {
                    val clipboard: ClipboardManager? = getSystemService(activity, ClipboardManager::class.java)
                    clipboard?.text = "text to clip"
                } else {
                    val clipboard: ClipboardManager? = getSystemService(activity, ClipboardManager::class.java)
                    val clip = ClipData.newPlainText("paintingID", paintingID)
                    clipboard?.setPrimaryClip(clip)
                }

                Snackbar.make(view, "Copied to clipboard", Snackbar.LENGTH_SHORT).show()
            }
        }

        return view
    }

    private fun generatePaintingID(): String {
        val random = SecureRandom()
        val bytes = ByteArray(PAINTING_ID_SIZE)

        random.nextBytes(bytes)

        return bytes.toHexString()
    }

    private fun ByteArray.toHexString() : String {
        return this.joinToString("") {
            java.lang.String.format("%02x", it)
        }
    }
}