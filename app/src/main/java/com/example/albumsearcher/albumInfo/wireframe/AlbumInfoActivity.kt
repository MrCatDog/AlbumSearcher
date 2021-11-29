package com.example.albumsearcher.albumInfo.wireframe

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.albumsearcher.Shared
import com.example.albumsearcher.albumInfo.viewModel.AlbumInfoViewModel
import com.example.albumsearcher.albumInfo.adapters.RecyclerAdapter
import com.example.albumsearcher.databinding.ActivityAlbumInfoBinding
import com.example.albumsearcher.util.viewModelsExt
import android.content.Intent
import android.util.Log
import com.example.albumsearcher.R

class AlbumInfoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAlbumInfoBinding

    private val viewModel by viewModelsExt {
        AlbumInfoViewModel(intent.getStringExtra(Shared.CLICKED_ITEM_ID))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAlbumInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.albumInfoSongs.layoutManager = LinearLayoutManager(baseContext)
        val adapter = RecyclerAdapter()
        binding.albumInfoSongs.adapter = adapter

        viewModel.album.observe(this) {
            binding.album = it
        }

        viewModel.songs.observe(this) {
            adapter.setData(it)
        }

        viewModel.err.observe(this) {
            Log.e(Shared.LOG_TAG, it.stackTraceToString() )
            val intent = Intent().apply {
                putExtra(Shared.WAS_ERROR_FLAG, true)
                putExtra(Shared.ERR_ANSWER, it.message)
            }
            setResult(RESULT_CANCELED, intent)
            finish()
        }

    }
}