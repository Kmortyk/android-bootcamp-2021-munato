package com.vanilla.munato.activity

import android.content.Context
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import com.vanilla.munato.R
import com.vanilla.munato.databinding.ActivityHomeBinding
import com.vanilla.munato.fragment.*
import com.vanilla.munato.model.PaintingModel

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding

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

        requestPaintings() // TODO remove
    }

    fun getScriptTemplate(context: Context) : String {
        val stream = context.resources.assets.open("templates/script_template.js")
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

    fun openPaintingViewFragment(paintingModel: PaintingModel) {
        val ftx = supportFragmentManager.beginTransaction()
        ftx.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
        ftx.replace(R.id.home_fragment_container, PaintingViewExploreFragment.newInstance(paintingModel))
        ftx.addToBackStack("painting_view")
        ftx.commit()
    }

    /* -- Database ------------------------------------------------------------------------------ */
    // TODO MVVM move to model

    fun publishPainting(paintingModel: PaintingModel) {
        Thread {
            // get container refs
            val paintingsRef = Firebase.database.getReference("paintings")
            val childRef = paintingsRef.push()

            // serialize and post
            val serialized = Gson().toJson(paintingModel)
            childRef.setValue(serialized)

            // show snack
            runOnUiThread {
                Snackbar.make(binding.root, "Painting successfully published ✨", Snackbar.LENGTH_SHORT).show()
            }
        }.run()

        openExploreFragment(false)
    }

    fun requestPaintings() : List<PaintingModel> {
        val paintingsRef = Firebase.database.getReference("paintings")

        paintingsRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for(paintingSnapshot in snapshot.children) {
                    val str = paintingSnapshot.value.toString()
                    val paintingModel = Gson().fromJson(str, PaintingModel::class.java)

                    Log.d("a", paintingModel.toString())
                }
            }

            override fun onCancelled(error: DatabaseError) { /* none */ }
        })

        return listOf()
    }

}