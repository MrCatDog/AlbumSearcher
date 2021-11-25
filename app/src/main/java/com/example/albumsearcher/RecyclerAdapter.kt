package com.example.albumsearcher

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.albumsearcher.databinding.SearchItemBinding

class RecyclerAdapter : RecyclerView.Adapter<RecyclerAdapter.VH>() {

    class VH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding: SearchItemBinding = SearchItemBinding.bind(itemView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        return VH(LayoutInflater.from(parent.context).inflate(R.layout.search_item, parent, false))
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val item = items[position]
        holder.binding.jokeID.text = holder.itemView.resources.getString(R.string.joke_id_symbol, item.id)
        holder.binding.categories.text = item.categories
        holder.binding.jokeText.text = item.text
    }

    override fun getItemCount() = items.size
}
