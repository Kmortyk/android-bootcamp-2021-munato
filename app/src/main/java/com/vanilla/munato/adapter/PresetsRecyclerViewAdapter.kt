package com.vanilla.munato.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.vanilla.munato.R
import com.vanilla.munato.model.Preset

class PresetsRecyclerViewHolder(itemView: View, val returnToEditor: (String) -> Unit) : RecyclerView.ViewHolder(itemView) {
    private val ivIcon: ImageView = itemView.findViewById(R.id.iv_preset_icon)
    private val tvName: TextView = itemView.findViewById(R.id.tv_preset_name)

    fun setData(preset: Preset) {
        ivIcon.setImageResource(preset.darawableResource)
        tvName.setText(preset.name)

        val onClick = View.OnClickListener { returnToEditor(preset.codeFragment) }

        ivIcon.setOnClickListener(onClick)
        tvName.setOnClickListener(onClick)
    }
}

class PresetsRecyclerViewAdapter(private val returnToEditor: (String) -> Unit) : RecyclerView.Adapter<PresetsRecyclerViewHolder>() {
    private val presets = mutableListOf<Preset>(

    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PresetsRecyclerViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.itm_preset, parent, false)

        return PresetsRecyclerViewHolder(view, returnToEditor)
    }

    override fun onBindViewHolder(holder: PresetsRecyclerViewHolder, position: Int) {
        holder.setData(presets[position])
    }

    override fun getItemCount() = presets.size
}