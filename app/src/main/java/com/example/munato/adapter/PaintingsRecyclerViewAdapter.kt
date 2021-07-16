package com.example.munato.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.munato.HomeActivity
import com.example.munato.R
import com.example.munato.model.PaintingModel

class PaintingCardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val tvHeader: TextView = itemView.findViewById(R.id.itm_card_header_text)
    val ivCardCover: ImageView = itemView.findViewById(R.id.itm_card_cover_img)
    val tvStarsCount: TextView = itemView.findViewById(R.id.itm_stars_text)

    fun setData(model: PaintingModel) {
        tvHeader.text = model.name
        // TODO load or create cover
        // ivCardCover.drawable = loadImage(model.coverServerPath)
        tvStarsCount.text = model.stars.toString()
    }
}

class PaintingsRecyclerViewAdapter(private val activity: HomeActivity, private val paintings: List<PaintingModel>) :
    RecyclerView.Adapter<PaintingCardViewHolder>() {
    companion object {
        const val TYPE_BIG = 1
        const val TYPE_SMALL = 2
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PaintingCardViewHolder {
        return if(viewType == TYPE_BIG) {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.itm_card_layout_big, parent, false)
            PaintingCardViewHolder(view)
        } else {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.itm_card_layout_small, parent, false)
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
        return if(paintings[position].stars > 10) {
            TYPE_BIG
        } else {
            TYPE_SMALL
        }
    }
}