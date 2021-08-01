package com.vanilla.munato.activity

import android.content.Context
import android.graphics.Bitmap
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.DatabaseError
import com.vanilla.munato.repository.server.PaintingsRepository
import com.vanilla.munato.R
import com.vanilla.munato.databinding.ActivityHomeBinding
import com.vanilla.munato.fragment.*
import com.vanilla.munato.model.PaintingDownloadData
import com.vanilla.munato.model.PaintingPreview
import com.vanilla.munato.model.PaintingPublishData
import com.vanilla.munato.repository.localstore.LocalRepository
import com.vanilla.munato.repository.server.UserRepository

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding

    private val paintingsRepository = lazy { PaintingsRepository() }
    private val usersRepository = lazy { UserRepository() }
    private val localRepository = lazy { LocalRepository(applicationContext) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        openExploreFragment(add=true)

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
                    ftx.replace(R.id.home_fragment_container, PaintingViewEditorFragment.newInstance(getScriptTemplate(this)))
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
        ftx.replace(R.id.home_fragment_container, PaintingViewEditorFragment.newInstance(code))
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
        val ftx = supportFragmentManager.beginTransaction()
        ftx.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
        ftx.replace(R.id.home_fragment_container, PublishPaintingFragment.newInstance(
            usersRepository.value.userName(),
            code,
            image
        ))

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

        paintingsRepository.value.publishPainting(paintingPreview,
            onSuccessFunction = {
                successSnack("Painting successfully published")
                openExploreFragment(add=false)
            },
            onFailureFunction = {
                failSnack("Fail to publish painting (${it.message})")
            })
    }

    fun loadPaintings(onPaintingLoaded: (PaintingDownloadData) -> Unit) {
        paintingsRepository.value.loadPaintings(
            onPaintingLoaded = onPaintingLoaded,
            onFailure = {
                failSnack("Fail to load paintings (${it.message})")
            }
        )
    }

    private fun processSnack(message: String) {
        Snackbar.make(binding.root, "$message...", Snackbar.LENGTH_SHORT).show()
    }

    fun successSnack(message: String) {
        Snackbar.make(binding.root, "$message ✨", Snackbar.LENGTH_SHORT).show()
    }

    fun failSnack(message: String) {
        Snackbar.make(binding.root, "$message \uD83D\uDE1E", Snackbar.LENGTH_SHORT).show()
    }

    fun addToFavourite(paintingID: String) {
        usersRepository.value.addFavourite(paintingID)
        successSnack("Added to favourite")
    }

    fun removeFromFavourite(paintingID: String) {
        usersRepository.value.removeFavourite(paintingID)
        successSnack("Removed from favourite")
    }

    fun loadFavouritePaintings(onFavouriteLoaded: (PaintingDownloadData) -> Unit) {
        loadFavourites {
            for(paintingID in it) {
                paintingsRepository.value.loadPainting(paintingID, onFavouriteLoaded, { err ->
                    failSnack("Sorry, can't load painting (${err.message})")
                })
            }
        }
    }

    fun loadFavourites(onFavouritesLoaded: (List<String>) -> Unit) {
        usersRepository.value.loadFavourites(
            onFavouritesLoaded = {
                onFavouritesLoaded(it)
            },
            onFailure = {
                failSnack("Fail to load favourites (${it.message})")
            }
        )
    }

    fun isStarred(paintingID: String, onSuccess: (Boolean) -> Unit, onFailure: (DatabaseError) -> Unit) {
        usersRepository.value.hasStarred(paintingID,
            onSuccess = onSuccess,
            onFailure = onFailure)
    }

    fun isFavourite(paintingID: String, onSuccess: (Boolean) -> Unit, onFailure: (DatabaseError) -> Unit) {
        usersRepository.value.isFavourite(paintingID,
            onSuccess = onSuccess,
            onFailure = onFailure)
    }

    fun addStarToPainting(paintingID: String) {
        usersRepository.value.hasStarred(paintingID,
            onSuccess = {
                if(!it) {
                    // if not starred in account
                    // add paintingID to account
                    usersRepository.value.addStarred(paintingID)
                    // increase painting stars counter
                    paintingsRepository.value.addStar(paintingID,
                        onSuccess = {
                            successSnack("Painting is starred")
                        },
                        onFailure = { err ->
                            failSnack("Oops something went wrong (${err.message})")
                        },
                    )
                } else {
                    // if starred in account
                    // do nothing
                    successSnack("Already starred")
                }
            }, onFailure = {
                failSnack("Oops something went wrong (${it.message})")
            })
    }

    fun removeStarFromPainting(paintingID: String) {
        usersRepository.value.hasStarred(paintingID,
            onSuccess = {
                if(it) {
                    // if starred in account
                    // remove paintingID from account
                    usersRepository.value.removeStarred(paintingID)
                    // increase painting stars counter
                    paintingsRepository.value.removeStar(paintingID,
                        onSuccess = {
                            successSnack("Star removed")
                        },
                        onFailure = { err ->
                            failSnack("Oops something went wrong (${err.message})")
                        },
                    )
                } else {
                    successSnack("Was not starred")
                }
            }, onFailure = {
                failSnack("Oops something went wrong (${it.message})")
            })
    }

    fun saveToLocalStorage(code: String, preview: PaintingPreview) {
        localRepository.value.savePainting(
            usersRepository.value.userName(),
            code,
            preview
        )

        successSnack("Painting saved")
    }

    fun loadMyPaintingsFromLocalStorage(onLoad: (List<PaintingPublishData>) -> Unit) {
        localRepository.value.loadPaintings {
            onLoad(it)
        }
    }
}