package com.example.albumsearcher.main.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.albumsearcher.R
import com.example.albumsearcher.databinding.SearchItemBinding
import com.example.albumsearcher.main.model.BaseAlbumInfo

class RecyclerAdapter(private val listener: OnItemClickListener) : RecyclerView.Adapter<RecyclerAdapter.AlbumHolder>() {

    private var items: List<BaseAlbumInfo> = ArrayList()

    class AlbumHolder(private val binding: SearchItemBinding, private val listener : OnItemClickListener) :
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        fun bind(albumInfo: BaseAlbumInfo) {
            binding.album = albumInfo
            binding.root.setOnClickListener(this)
            binding.executePendingBindings()
        }

        override fun onClick(p0: View?) {
            listener.onItemClick(adapterPosition)
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
        return AlbumHolder(binding, listener)
    }

    override fun onBindViewHolder(holder: AlbumHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount() = items.size

    @SuppressLint("NotifyDataSetChanged") //Where is no "some items" changed, it's all of them
    fun setData(items: List<BaseAlbumInfo>) {
        this.items = items
        notifyDataSetChanged()
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

}
