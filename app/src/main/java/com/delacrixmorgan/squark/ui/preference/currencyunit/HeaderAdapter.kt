package com.delacrixmorgan.squark.ui.preference.currencyunit

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.delacrixmorgan.squark.databinding.ItemHeaderBinding

class HeaderAdapter : RecyclerView.Adapter<HeaderAdapter.HeaderViewHolder>() {
    var text: String = ""
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = HeaderViewHolder(
        ItemHeaderBinding.inflate(LayoutInflater.from(parent.context), false)
    )

    override fun onBindViewHolder(holder: HeaderViewHolder, position: Int) {
        holder.bind(text)
    }

    override fun getItemCount(): Int = 1

    inner class HeaderViewHolder(private val binding: ItemHeaderBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(text: String) = with(binding) {
            headerTextView.text = text
        }
    }
}