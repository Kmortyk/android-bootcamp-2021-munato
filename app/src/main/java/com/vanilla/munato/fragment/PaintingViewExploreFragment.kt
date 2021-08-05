package com.vanilla.munato.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.webkit.WebView
import androidx.core.content.res.ResourcesCompat
import com.vanilla.munato.activity.HomeActivity
import com.vanilla.munato.R
import com.vanilla.munato.model.PaintingModel

private const val ARG_PAINTING_MODEL = "painting_model"

/**
 * A simple [Fragment] subclass.
 * Use the [PaintingViewFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PaintingViewExploreFragment : Fragment() {
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         * @return A new instance of fragment CreatePaintingFragment.
         */
        @JvmStatic
        fun newInstance(paintingModel: PaintingModel) =
            PaintingViewExploreFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_PAINTING_MODEL, paintingModel)
                }
            }
    }

    private var paintingModel: PaintingModel? = null
    private var fragmentMenu: Menu? = null // TODO check after rotate screen

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setHasOptionsMenu(true)

        arguments?.let {
            if(it.containsKey(ARG_PAINTING_MODEL))
                paintingModel = it.getParcelable(ARG_PAINTING_MODEL)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.painting_view_explore_action_bar, menu)
        super.onCreateOptionsMenu(menu,inflater)

        fragmentMenu = menu

        // update app bar after init
        updateAppBar()
    }

    private fun updateAppBar() {
        if(activity == null) {
            return
        }

        val activity = activity as HomeActivity
        val paintingID = paintingModel?.paintingID ?: return

        activity.isStarred(paintingID,
            onSuccess = { starred ->
                menuFillStar(starred)

                activity.isFavourite(paintingID,
                    onSuccess = { favourite ->
                        menuFillHeart(favourite)
                    },
                    onFailure = {
                        activity.failSnack("Oops something went wrong ($it)")
                    })
            },
            onFailure = {
                activity.failSnack("Oops something went wrong ($it)")
            })
    }

    private fun menuFillStar(starred: Boolean) {
        val activity = activity as HomeActivity
        val menu = fragmentMenu ?: return

        val drawable = if(starred) {
            ResourcesCompat.getDrawable(activity.resources, R.drawable.outline_star_white_24, activity.theme)
        } else {
            ResourcesCompat.getDrawable(activity.resources, R.drawable.outline_star_outline_white_24, activity.theme)
        }

        menu.findItem(R.id.action_star).icon = drawable
    }

    private fun menuFillHeart(favourite: Boolean) {
        val activity = activity as HomeActivity
        val menu = fragmentMenu ?: return

        val drawable = if(favourite) {
            ResourcesCompat.getDrawable(activity.resources, R.drawable.outline_favorite_white_24, activity.theme)
        } else {
            ResourcesCompat.getDrawable(activity.resources, R.drawable.outline_favorite_border_white_24, activity.theme)
        }

        menu.findItem(R.id.action_add_to_favourite).icon = drawable
    }

    override fun onOptionsItemSelected(item: MenuItem) : Boolean {
        if(activity == null) {
            Log.e("PaintingView", "activity is null")
            return false
        }

        val paintingModel = paintingModel

        if(paintingModel == null) {
            Log.e("PaintingView", "painting model is null")
            return false
        }

        val paintingID = paintingModel.paintingID
        val activity = activity as HomeActivity

        return when (item.itemId) {
            R.id.action_star -> {
                if(paintingID == null) {
                    activity.failSnack("Painting is not published")
                    return false
                }

                activity.isStarred(paintingID, { starred ->
                    if(starred) {
                        activity.removeStarFromPainting(paintingID)
                    } else {
                        activity.addStarToPainting(paintingID)
                    }

                    menuFillStar(!starred)
                }, {
                    activity.failSnack("Oops something went wrong ${it.message}")
                })

                true
            }

            R.id.action_add_to_favourite -> {
                if(paintingID == null) {
                    activity.failSnack("Painting is not published")
                    return false
                }

                activity.isFavourite(paintingID, { favourite ->

                    if(favourite) {
                        activity.removeFromFavourite(paintingID)
                    } else {
                        activity.addToFavourite(paintingID)
                    }

                    menuFillHeart(!favourite)
                }, {
                    activity.failSnack("Oops something went wrong ${it.message}")
                })

                true
            }

            R.id.action_fork -> {
                activity.openEditorFragment(paintingModel.code)
                activity.successSnack("Successfully forked")
                true
            }

            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

    @SuppressLint("SetJavaScriptEnabled") // don't care
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_painting_view, container, false)
        val activity = activity as HomeActivity

        val webView = view.findViewById<WebView>(R.id.web_view)
        var template = getHTMLPageTemplate(activity)

        if(paintingModel != null) {
            webView.settings.javaScriptEnabled = true
            template += "<script>${paintingModel!!.code}</script>"
        }

        template += "<script>draw(ctx, width, height);</script>"

        // https://stackoverflow.com/questions/37090396/android-webview-doesnt-load-html-sometimes

        webView.postDelayed({
            webView.loadDataWithBaseURL(null, template, null, "UTF-8", null);
        }, 100)

        return view
    }

    fun getHTMLPageTemplate(context: Context) : String {
        val stream = context.resources.assets.open("templates/page_template.html")
        return stream.readBytes().decodeToString()
    }
}