package com.example.albumsearcher.main.wireframe

import android.content.Intent
import android.os.Bundle
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.albumsearcher.databinding.ActivityMainBinding
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.albumsearcher.R
import com.example.albumsearcher.Shared
import com.example.albumsearcher.albumInfo.wireframe.AlbumInfoActivity
import com.example.albumsearcher.main.adapters.RecyclerAdapter
import com.example.albumsearcher.main.viewModel.MainViewModel

import com.example.albumsearcher.util.viewModelsExt
import com.google.android.material.snackbar.Snackbar

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
            showSnack(it.message ?: getString(R.string.unknown_error), Snackbar.LENGTH_LONG)
        }

        viewModel.noResultEvent.observe(this) {
            showSnack(getString(R.string.no_results), Snackbar.LENGTH_SHORT)
        }
    }

    private fun showSnack(text: String, length: Int) {
        Snackbar.make(binding.root, text, length).show()
    }

    override fun onItemClick(position: Int) {
        val intent = Intent(this, AlbumInfoActivity::class.java).apply {
            putExtra(Shared.CLICKED_ITEM_ID, viewModel.getItemIDByPosition(position))
        }
        startForResult.launch(intent)
    }

    private val startForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == RESULT_CANCELED && result.data?.getBooleanExtra(
                    Shared.WAS_ERROR_FLAG,
                    false
                ) == true
            ) {
                val text = result.data?.getStringExtra(Shared.ERR_ANSWER)
                if (text != null) {
                    showSnack(text, Snackbar.LENGTH_LONG)
                } else {
                    showSnack(getString(R.string.album_not_found_err), Snackbar.LENGTH_SHORT)
                }
            }
            binding.searchBar.clearFocus() //почему этот элемент его вообще получает?
        }
}
