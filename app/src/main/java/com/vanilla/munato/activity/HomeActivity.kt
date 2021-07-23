package com.vanilla.munato.activity

import android.content.Context
import android.graphics.Bitmap
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.snackbar.Snackbar
import com.vanilla.munato.repository.server.PaintingsRepository
import com.vanilla.munato.R
import com.vanilla.munato.databinding.ActivityHomeBinding
import com.vanilla.munato.fragment.*
import com.vanilla.munato.model.PaintingDownloadData
import com.vanilla.munato.model.PaintingPublishData

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private val paintingsRepository = PaintingsRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        openExploreFragment(true)

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
                    ftx.replace(R.id.home_fragment_container, PaintingViewEditorFragment.newInstance(null, getScriptTemplate(this)))
                }
            }

            ftx.addToBackStack("select_menu")
            ftx.commit()

            true
        }
    }

    fun getScriptTemplate(context: Context) : String {
        val examples = arrayOf(
            "templates/script_example_1.js",
            "templates/script_example_2.js",
            "templates/script_example_3.js"
        )

        val stream = context.resources.assets.open(examples.random())
        return stream.readBytes().decodeToString()
    }

    fun openEditorFragment(code: String?) {
        val ftx = supportFragmentManager.beginTransaction()
        ftx.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
        ftx.replace(R.id.home_fragment_container, EditorFragment.newInstance(code))
        ftx.addToBackStack("editor")
        ftx.commit()
    }

    fun returnFromEditorFragment(code: String) {
        val ftx = supportFragmentManager.beginTransaction()
        ftx.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE)
        ftx.replace(R.id.home_fragment_container, PaintingViewEditorFragment.newInstance(null, code))
        ftx.addToBackStack("editor_return")
        ftx.commit()
    }

    private fun openExploreFragment(add: Boolean) {
        val ftx = supportFragmentManager.beginTransaction()

        if(add) {
            ftx.add(R.id.home_fragment_container, ExploreFragment.newInstance())
        } else {
            ftx.replace(R.id.home_fragment_container, ExploreFragment.newInstance())
            ftx.addToBackStack("explore")
        }

        ftx.commit()
    }

    fun openPublishPaintingFragment(code: String, image: Bitmap) {
        val username = "kmortyk" //TODO get username

        val ftx = supportFragmentManager.beginTransaction()
        ftx.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
        ftx.replace(R.id.home_fragment_container, PublishPaintingFragment.newInstance(username, code, image))
        ftx.addToBackStack("publish_painting")
        ftx.commit()
    }

    fun openPaintingViewFragment(painting: PaintingDownloadData) {
        val ftx = supportFragmentManager.beginTransaction()
        ftx.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
        ftx.replace(R.id.home_fragment_container, PaintingViewExploreFragment.newInstance(painting.model))
        ftx.addToBackStack("painting_view")
        ftx.commit()
    }

    fun publishPainting(paintingPreview: PaintingPublishData) {
        processSnack("Painting loading to the server")

        paintingsRepository.publishPainting(paintingPreview,
            onSuccessFunction = {
                successSnack("Painting successfully published")
                openExploreFragment(false)
            },
            onFailureFunction = {
                failSnack("Fail to publish painting (${it.message})")
            })
    }

    fun loadPaintings(onPaintingLoaded: (PaintingDownloadData) -> Unit) {
        paintingsRepository.loadPaintings(
            onPaintingLoaded=onPaintingLoaded,
            onFailure = {
                failSnack("Fail to load paintings (${it.message})")
            }
        )
    }

    private fun processSnack(message: String) {
        Snackbar.make(binding.root, "$message...", Snackbar.LENGTH_SHORT).show()
    }

    private fun successSnack(message: String) {
        Snackbar.make(binding.root, "$message ✨", Snackbar.LENGTH_SHORT).show()
    }

    private fun failSnack(message: String) {
        Snackbar.make(binding.root, "$message \uD83D\uDE1E", Snackbar.LENGTH_SHORT).show()
    }
}