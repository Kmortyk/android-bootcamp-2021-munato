package com.vanilla.munato.fragment

import android.content.ClipData
import android.content.ClipboardManager
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.vanilla.munato.R
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.Snackbar
import com.vanilla.munato.activity.HomeActivity
import com.vanilla.munato.model.PaintingModel
import com.vanilla.munato.model.PaintingPublishData
import java.security.SecureRandom


private const val ARG_CODE = "code"
private const val ARG_USERNAME = "username"
private const val ARG_IMAGE= "image"

const val PAINTING_ID_SIZE = 128

class PublishPaintingFragment : Fragment() {
    companion object {
        @JvmStatic
        fun newInstance(username: String, code: String, image: Bitmap) =
            PublishPaintingFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_USERNAME, username)
                    putString(ARG_CODE, code)
                    putParcelable(ARG_IMAGE, image)
                }
            }
    }

    private var code: String? = null
    private var username: String? = null
    private var image: Bitmap? = null
    private var paintingID: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            username = it.getString(ARG_USERNAME)
            code = it.getString(ARG_CODE)
            image = it.getParcelable(ARG_IMAGE)
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
        val btnPublish = view.findViewById<MaterialButton>(R.id.fragment_publish_btn_publish)

        val etAuthor = view.findViewById<EditText>(R.id.fragment_publish_et_author)
        val etTitle = view.findViewById<EditText>(R.id.fragment_publish_et_title)
        val userImage = view.findViewById<ImageView>(R.id.fragment_publish_userImage)

        if (image == null) {
            throw NullPointerException("User's image is null")
        }

        userImage.setImageBitmap(image)

        etAuthor.setText(if(username != null) username else "")

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

        btnPublish.setOnClickListener {
            if(etTitle.text.isBlank()) {
                etTitle.error = "Title cannot be empty or blank"
            } else {
                val paintingModel = PaintingModel(
                    paintingID, // unknown at the moment
                    etAuthor.text.toString(),
                    etTitle.text.toString(),
                    code,
                    0,
                )

                (activity as HomeActivity)
                    .publishPainting(PaintingPublishData(paintingModel, image!!))
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