package com.example.albumsearcher.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.albumsearcher.BaseAlbumInfo
import com.example.albumsearcher.R
import com.example.albumsearcher.databinding.SearchItemBinding
import java.util.ArrayList

class RecyclerAdapter : RecyclerView.Adapter<RecyclerAdapter.AlbumHolder>() {

    private var items: List<BaseAlbumInfo> = ArrayList()

    class AlbumHolder(private val binding: SearchItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(albumInfo: BaseAlbumInfo?) {
            binding.album = albumInfo
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<SearchItemBinding>(
            inflater,
            R.layout.search_item,
            parent,
            false
        )
        return AlbumHolder(binding)
    }

    override fun onBindViewHolder(holder: AlbumHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount() = items.size

    fun setData(items: List<BaseAlbumInfo>, itemCount: Int) {
        this.items = items
        notifyDataSetChanged() //todo
        //notifyItemRangeInserted(items.lastIndex, itemCount)
    }
}
