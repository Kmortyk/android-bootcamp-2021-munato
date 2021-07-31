package com.vanilla.munato.adapter

import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.vanilla.munato.R
import com.vanilla.munato.activity.HomeActivity
import com.vanilla.munato.model.PaintingDownloadData
import com.vanilla.munato.model.PaintingPreviewMethods
import com.vanilla.munato.model.PaintingViewData


const val PAINTINGS_DIFFERENCE = true

class PaintingCardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val ivCardCover: ImageView = itemView.findViewById(R.id.itm_card_cover_img)
    private val tvStarsCount: TextView = itemView.findViewById(R.id.itm_stars_text)
    private val cardViewAccent: CardView = itemView.findViewById(R.id.accent_card)

    fun setData(painting: PaintingViewData) {
        tvStarsCount.text = painting.getModel().stars.toString()

        // start loading preview
        if(painting.getPreviewURL() != Uri.EMPTY) {
            PaintingPreviewMethods.downloadPainting(
                itemView.context,
                painting.getPreviewURL(),
                ivCardCover) { resource, _, _, _, _ ->
                if(resource != null && resource is BitmapDrawable && resource.bitmap != null) {
                    val color = averageColor(resource.bitmap)
                    cardViewAccent.backgroundTintList = ColorStateList.valueOf(color)
                    painting.averageColor = color // cache average to model
                }
            }
        } else {
            cardViewAccent.backgroundTintList = ColorStateList.valueOf(painting.averageColor)
        }
    }

    // https://stackoverflow.com/questions/12408431/how-can-i-get-the-average-colour-of-an-image

    private fun averageColor(bitmap: Bitmap) : Int {
        var redBucket: Long = 0
        var greenBucket: Long = 0
        var blueBucket: Long = 0
        var pixelCount: Long = 0

        for (y in 0 until bitmap.height step 2) {
            for (x in 0 until bitmap.width step 2) {
                val c = bitmap.getPixel(x, y)

                pixelCount++

                redBucket += Color.red(c)
                greenBucket += Color.green(c)
                blueBucket += Color.blue(c)
            }
        }

        return Color.rgb(
            (redBucket / pixelCount).toInt(),
            (greenBucket / pixelCount).toInt(),
            (blueBucket / pixelCount).toInt()
        )
    }
}

class PaintingsRecyclerViewAdapter(private val activity: HomeActivity) :
    RecyclerView.Adapter<PaintingCardViewHolder>() {

    private val paintings: MutableList<PaintingViewData> = mutableListOf()

    companion object {
        const val TYPE_BIG = 1
        const val TYPE_SMALL = 2
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PaintingCardViewHolder {
        return if(PAINTINGS_DIFFERENCE && viewType == TYPE_SMALL) {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.itm_card_layout_small, parent, false)
            PaintingCardViewHolder(view)
        } else {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.itm_card_layout_big, parent, false)
            PaintingCardViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: PaintingCardViewHolder, position: Int) {
        holder.setData(paintings[position])
        holder.itemView.setOnClickListener {
            activity.openPaintingViewFragment(paintings[position].paintingDownloadData)
        }
    }

    override fun getItemCount() = paintings.size

    override fun getItemViewType(position: Int): Int {
        return if(isBig(position)) TYPE_BIG else TYPE_SMALL
    }

    private fun isBig(position: Int) : Boolean {
        // paintings[position].getModel().stars > 10
        return position % 2 == 0 && position % 3 == 0
    }

    fun addData(paintingDownloadData: PaintingDownloadData) {
        paintings.add(PaintingViewData(paintingDownloadData))
        notifyItemInserted(paintings.size - 1)
    }
}