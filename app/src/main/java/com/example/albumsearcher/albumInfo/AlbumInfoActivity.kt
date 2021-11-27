package com.example.albumsearcher.albumInfo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.albumsearcher.databinding.ActivityAlbumInfoBinding
import com.example.albumsearcher.util.viewModelsExt

class AlbumInfoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAlbumInfoBinding

    private val viewModel by viewModelsExt {
        AlbumInfoViewModel()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAlbumInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)


    }
}