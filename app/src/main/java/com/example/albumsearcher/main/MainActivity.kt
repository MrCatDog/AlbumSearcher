package com.example.albumsearcher.main

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.albumsearcher.databinding.ActivityMainBinding
import androidx.core.widget.addTextChangedListener
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.albumsearcher.util.viewModelsExt
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso

@BindingAdapter("app:url")
fun loadImage(view: ImageView?, url: String?) {
    Picasso.get().load(url).into(view)
}

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val viewModel by viewModelsExt {
        MainViewModel()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.searchResults.layoutManager = LinearLayoutManager(baseContext)
        val adapter = RecyclerAdapter()
        binding.searchResults.adapter = adapter

        binding.searchBar.addTextChangedListener { s -> viewModel.searchTextChanged(s.toString()) }

        viewModel.albums.observe(this) {
            adapter.setData(it, 50)
        }

        viewModel.errMsg.observe(this) {
            showSnack(it)
        }
    }

    private fun showSnack(text : String) {
        Snackbar.make(binding.root, text, Snackbar.LENGTH_LONG).show()

    }
}
