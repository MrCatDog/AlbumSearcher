package com.example.albumsearcher.albumInfo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.albumsearcher.Shared
import com.example.albumsearcher.databinding.ActivityAlbumInfoBinding
import com.example.albumsearcher.util.viewModelsExt

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

        viewModel.album.observe(this) {
            binding.album = it
        }
    }
}