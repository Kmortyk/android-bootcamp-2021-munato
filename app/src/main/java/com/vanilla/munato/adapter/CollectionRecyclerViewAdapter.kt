package com.vanilla.munato.adapter;

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.vanilla.munato.R
import com.vanilla.munato.model.PaintingModel

class CollectionItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val tvHeader: TextView = itemView.findViewById(R.id.collection_tv_painting_name)
    val tvStarsCount: TextView = itemView.findViewById(R.id.collection_itm_stars_text)
    val btnEdit: Button = itemView.findViewById(R.id.collection_btn_edit)

    fun setData(model: PaintingModel) {
        tvHeader.text = model.name
        tvStarsCount.text = model.stars.toString()
    }
}

class CollectionRecyclerViewAdapter(private val paintings: List<PaintingModel>) :
    RecyclerView.Adapter<CollectionItemViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CollectionItemViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.itm_my_painting, parent, false)
        return CollectionItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: CollectionItemViewHolder, position: Int) {
        holder.setData(paintings[position])
    }

    override fun getItemCount() = paintings.size
}

