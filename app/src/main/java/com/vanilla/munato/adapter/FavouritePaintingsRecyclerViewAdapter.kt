package com.vanilla.munato.adapter;

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.vanilla.munato.R
import com.vanilla.munato.model.PaintingDownloadData
import com.vanilla.munato.model.PaintingPreviewMethods

class FavouritePaintingsItemViewHolder(itemView: View, private val openEditorFun: (String) -> Unit) : RecyclerView.ViewHolder(itemView) {
    private val tvHeader: TextView = itemView.findViewById(R.id.collection_tv_painting_name)
    private val tvStarsCount: TextView = itemView.findViewById(R.id.collection_itm_stars_text)
    private val btnFork: Button = itemView.findViewById(R.id.collection_btn_fork)
    private val imPreview: ImageView = itemView.findViewById(R.id.itm_my_painting_preview)

    fun setData(model: PaintingDownloadData) {
        tvHeader.text = model.model.name
        tvStarsCount.text = model.model.stars.toString()

        btnFork.setOnClickListener {
            openEditorFun(model.model.code ?: "")
        }

        // start loading preview
        if(model.previewURL != Uri.EMPTY) {
            PaintingPreviewMethods.downloadPainting(itemView.context, model.previewURL, imPreview)
            { _, _, _, _, _ -> }
        }
    }
}

class FavouritePaintingsRecyclerViewAdapter(private val openEditorFun: (String) -> Unit) :
    RecyclerView.Adapter<FavouritePaintingsItemViewHolder>() {
    private val paintings = mutableListOf<PaintingDownloadData>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavouritePaintingsItemViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.itm_favourite_painting, parent, false)
        return FavouritePaintingsItemViewHolder(view, openEditorFun)
    }

    override fun onBindViewHolder(holder: FavouritePaintingsItemViewHolder, position: Int) {
        holder.setData(paintings[position])
    }

    override fun getItemCount() = paintings.size

    fun addData(it: PaintingDownloadData) {
        paintings.add(it)
        notifyItemInserted(paintings.size - 1)
    }
}

