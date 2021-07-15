package com.example.munato.adapter;

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.munato.R
import com.example.munato.model.PaintingModel

class CollectionItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    fun setData(model: PaintingModel) {

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

