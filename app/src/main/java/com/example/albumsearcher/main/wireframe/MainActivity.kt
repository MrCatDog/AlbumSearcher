package com.example.albumsearcher.main.wireframe

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.albumsearcher.databinding.ActivityMainBinding
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.albumsearcher.Shared
import com.example.albumsearcher.albumInfo.wireframe.AlbumInfoActivity
import com.example.albumsearcher.main.adapters.RecyclerAdapter
import com.example.albumsearcher.main.viewModel.MainViewModel

import com.example.albumsearcher.util.viewModelsExt
import com.google.android.material.snackbar.Snackbar

const val LOG_TAG = "AlbumSearcher"

class MainActivity : AppCompatActivity(), RecyclerAdapter.OnItemClickListener {

    private lateinit var binding: ActivityMainBinding

    private val viewModel by viewModelsExt {
        MainViewModel()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.searchResults.layoutManager = LinearLayoutManager(baseContext)
        val adapter = RecyclerAdapter(this)
        binding.searchResults.adapter = adapter

        binding.searchBar.addTextChangedListener { s -> viewModel.searchTextChanged(s.toString()) }

        viewModel.albums.observe(this) {
            adapter.setData(it)
        }

        viewModel.err.observe(this) {
            Log.e(LOG_TAG, it.stackTraceToString())
            showSnack(it.message ?: "", Snackbar.LENGTH_LONG)//R.string.unkown_error
        }

        viewModel.noResultEvent.observe(this) {
            showSnack("No results", Snackbar.LENGTH_SHORT)
        }
    }

    private fun showSnack(text: String, length : Int) {
        Snackbar.make(binding.root, text, length).show()
    }

    override fun onItemClick(position: Int) {
        startActivity(Intent(this, AlbumInfoActivity::class.java).apply {
            putExtra(Shared.CLICKED_ITEM_ID, viewModel.getItemIDByPosition(position))
        })

    }
}
