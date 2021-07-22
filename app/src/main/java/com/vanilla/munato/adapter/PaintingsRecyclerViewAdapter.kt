package com.vanilla.munato.adapter

import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
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


const val PAINTINGS_DIFFERENCE = true

class PaintingCardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//    private val tvHeader: TextView = itemView.findViewById(R.id.itm_card_header_text)
    private val ivCardCover: ImageView = itemView.findViewById(R.id.itm_card_cover_img)
    private val tvStarsCount: TextView = itemView.findViewById(R.id.itm_stars_text)
    private val cardViewAccent: CardView = itemView.findViewById(R.id.accent_card)

    fun setData(painting: PaintingDownloadData) {
//        tvHeader.text = painting.model.name
        tvStarsCount.text = painting.model.stars.toString()

        // start loading preview
        if(painting.previewURL != Uri.EMPTY) {
            Glide.with(itemView.context)
                .load(painting.previewURL)
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean) = false

                    override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                        setAverageColorAsAccentCardBackground(resource)
                        return false
                    }
                })
                .into(ivCardCover)
        } else {
            setAverageColorAsAccentCardBackground(ivCardCover.drawable)
        }
    }

    private fun setAverageColorAsAccentCardBackground(drawable: Drawable?) {
        val bitmap = drawableToBitmap(drawable)
        bitmap?.let {
            val color = averageColor(bitmap)
            cardViewAccent.backgroundTintList = ColorStateList.valueOf(color)
        }
    }

    // https://stackoverflow.com/questions/3035692/how-to-convert-a-drawable-to-a-bitmap

    private fun drawableToBitmap(drawable: Drawable?): Bitmap? {
        if(drawable == null) {
            return null
        }

        var bitmap: Bitmap? = null
        if (drawable is BitmapDrawable) {
            val bitmapDrawable = drawable
            if (bitmapDrawable.bitmap != null) {
                return bitmapDrawable.bitmap
            }
        }
        bitmap = if (drawable.intrinsicWidth <= 0 || drawable.intrinsicHeight <= 0) {
            Bitmap.createBitmap(
                1,
                1,
                Bitmap.Config.ARGB_8888
            ) // Single color bitmap will be created of 1x1 pixel
        } else {
            Bitmap.createBitmap(
                drawable.intrinsicWidth,
                drawable.intrinsicHeight,
                Bitmap.Config.ARGB_8888
            )
        }
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight())
        drawable.draw(canvas)
        return bitmap
    }

    // https://stackoverflow.com/questions/12408431/how-can-i-get-the-average-colour-of-an-image

    private fun averageColor(bitmap: Bitmap) : Int {
        var redBucket: Long = 0
        var greenBucket: Long = 0
        var blueBucket: Long = 0
        var pixelCount: Long = 0

        for (y in 0 until bitmap.height) {
            for (x in 0 until bitmap.width) {
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

    private val paintings: MutableList<PaintingDownloadData> = mutableListOf()

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
            activity.openPaintingViewFragment(paintings[position])
        }
    }

    override fun getItemCount() = paintings.size

    override fun getItemViewType(position: Int): Int {
        return if(paintings[position].model.stars > 10) {
            TYPE_BIG
        } else {
            TYPE_SMALL
        }
    }

    fun addData(paintingDownloadData: PaintingDownloadData) {
        paintings.add(paintingDownloadData)
        notifyItemInserted(paintings.size - 1)
    }
}