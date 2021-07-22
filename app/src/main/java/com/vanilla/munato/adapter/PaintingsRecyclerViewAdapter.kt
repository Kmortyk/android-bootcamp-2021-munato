package com.vanilla.munato.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.vanilla.munato.R
import com.vanilla.munato.activity.HomeActivity
import com.vanilla.munato.model.PaintingDownloadData

const val PAINTINGS_DIFFERENCE = true

class PaintingCardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val tvHeader: TextView = itemView.findViewById(R.id.itm_card_header_text)
    private val ivCardCover: ImageView = itemView.findViewById(R.id.itm_card_cover_img)
    private val tvStarsCount: TextView = itemView.findViewById(R.id.itm_stars_text)

    fun setData(painting: PaintingDownloadData) {
        tvHeader.text = painting.model.name
        tvStarsCount.text = painting.model.stars.toString()

        // start loading preview
        if(painting.previewURL != Uri.EMPTY) {
            Glide.with(itemView.context)
                .load(painting.previewURL)
                .into(ivCardCover)
        }
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