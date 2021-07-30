package com.vanilla.munato.adapter;

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.vanilla.munato.R
import com.vanilla.munato.model.PaintingModel
import com.vanilla.munato.model.PaintingPublishData

class MyPaintingsItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val tvHeader = itemView.findViewById<TextView>(R.id.collection_tv_painting_name)
    private val ivPreview = itemView.findViewById<ImageView>(R.id.itm_my_painting_preview)
//    val btnEdit: Button = itemView.findViewById(R.id.collection_btn_edit)

    fun setData(model: PaintingPublishData) {
        tvHeader.text = model.model.name
        ivPreview.setImageBitmap(model.preview)
    }
}

class MyPaintingsRecyclerViewAdapter() : RecyclerView.Adapter<MyPaintingsItemViewHolder>() {
    private val paintings = mutableListOf<PaintingPublishData>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyPaintingsItemViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.itm_my_painting, parent, false)
        return MyPaintingsItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyPaintingsItemViewHolder, position: Int) {
        holder.setData(paintings[position])
    }

    override fun getItemCount() = paintings.size

    fun setData(list: List<PaintingPublishData>) {
        paintings.clear()
        paintings.addAll(list)

        notifyDataSetChanged()
    }
}

