package com.example.albumsearcher

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.albumsearcher.databinding.SearchItemBinding
import com.squareup.picasso.Picasso
import java.util.ArrayList

class RecyclerAdapter : RecyclerView.Adapter<RecyclerAdapter.VH>() {

    private var items: List<BaseAlbumInfo> = ArrayList()

    class VH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding: SearchItemBinding = SearchItemBinding.bind(itemView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        return VH(LayoutInflater.from(parent.context).inflate(R.layout.search_item, parent, false))
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val item = items[position]
        holder.binding.albumNameHolder.text = item.albumName
        holder.binding.artistNameHolder.text = item.artistName
        //TODO вот это мало того что логика, так ещё и загрузка в основном потоке
        //проверить не поможет ли в этом вопросе dataBinding
        Picasso.get().load(item.coverURL).into(holder.binding.imageHolder)
    }

    override fun getItemCount() = items.size

    fun setData(items: List<BaseAlbumInfo>, itemCount: Int) {
        this.items = items
        notifyDataSetChanged() //todo
        //notifyItemRangeInserted(items.lastIndex, itemCount)
    }
}
