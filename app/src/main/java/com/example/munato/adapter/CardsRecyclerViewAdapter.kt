package com.example.munato.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.munato.R
import com.example.munato.model.PaintingModel

class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
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

class CardsRecyclerViewAdapter(val paintings: List<PaintingModel>) :
    RecyclerView.Adapter<ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.itm_card_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setData(paintings[position])
    }

    override fun getItemCount() = paintings.size
}