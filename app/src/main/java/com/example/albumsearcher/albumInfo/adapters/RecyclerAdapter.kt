package com.example.albumsearcher.albumInfo.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.albumsearcher.R
import com.example.albumsearcher.albumInfo.model.SongInfo
import com.example.albumsearcher.databinding.SongItemBinding

class RecyclerAdapter : RecyclerView.Adapter<RecyclerAdapter.SongHolder>() {

    private var items: List<SongInfo> = ArrayList()

    class SongHolder(private val binding: SongItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(songInfo: SongInfo) {
            binding.song = songInfo
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<SongItemBinding>(
            inflater,
            R.layout.song_item,
            parent,
            false
        )
        return SongHolder(binding)
    }

    override fun onBindViewHolder(holder: SongHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount() = items.size

    @SuppressLint("NotifyDataSetChanged") //Where is no "some items" changed, it's all of them
    fun setData(items: List<SongInfo>) {
        this.items = items
        notifyDataSetChanged()
    }
}