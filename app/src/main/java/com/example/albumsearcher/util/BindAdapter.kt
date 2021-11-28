package com.example.albumsearcher.util

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.example.albumsearcher.R
import com.squareup.picasso.Picasso

@BindingAdapter("app:url")
fun loadImage(view: ImageView?, url: String?) {
    Picasso.get()
        .load(url)
        .placeholder(R.drawable.album_default)
        .error(R.drawable.album_default)
        .into(view)
}