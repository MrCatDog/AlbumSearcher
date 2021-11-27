package com.example.albumsearcher.main

import java.util.ArrayList

class MainModel {

    data class BaseAlbumInfo(val albumName:String, val artistName:String, val coverURL: String, val albumId:Double)

    val items = ArrayList<BaseAlbumInfo>()
}