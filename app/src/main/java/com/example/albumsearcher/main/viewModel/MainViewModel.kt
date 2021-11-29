package com.example.albumsearcher.main.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.albumsearcher.main.model.BaseAlbumInfo
import com.example.albumsearcher.util.DataReceiver
import com.example.albumsearcher.util.MutableLiveEvent
import okhttp3.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import java.util.*

const val URL_BASE = "https://itunes.apple.com/search?term="
const val URL_PARAMETERS = "&media=music&entity=album&attribute=albumTerm&limit=200"
const val RESULT_COUNT_TAG = "resultCount"
const val RESULT_TAG = "results"

const val COLLECTION_NAME = "collectionName"
const val ARTIST_NAME = "artistName"
const val ARTWORK = "artworkUrl100"
const val ID = "collectionId"

class MainViewModel : ViewModel() {

    private val dataReceiver = DataReceiver()

    private val _albums = MutableLiveData<List<BaseAlbumInfo>>()
    val albums: LiveData<List<BaseAlbumInfo>>
        get() = _albums

    private val _err = MutableLiveEvent<Exception>()
    val err: LiveData<Exception>
        get() = _err

    private val _noResultEvent = MutableLiveEvent<Unit>()
    val noResultEvent : LiveData<Unit>
            get() = _noResultEvent

    fun getItemIDByPosition(position: Int): String? = _albums.value?.get(position)?.albumId

    fun searchTextChanged(text: String) {
        if (text.trim().isNotEmpty()) {
            dataReceiver.cancel()
            try {
                findMore(text)
            } catch (ex : Exception) {
                _err.setValue(ex)
            }
        } else {
            _albums.value = ArrayList<BaseAlbumInfo>()
        }
    }

    private fun findMore(text: String) {
        dataReceiver.requestData(
            URL_BASE + text + URL_PARAMETERS,
            this::onResponse,
            this::onFailure
        )
    }

    private fun onFailure(call: Call, e: IOException) {
        if (!call.isCanceled()) {
            _err.postValue(e)
        }
    }

    private fun onResponse(response: Response) {
        if (!response.isSuccessful) {
            _err.postValue(IOException(response.message + response.code.toString()))
        } else {
            val answer = JSONObject(response.body?.string() ?: throw IOException())
            val count = answer.get(RESULT_COUNT_TAG) as Int
            if (count == 0) {
                _noResultEvent.postValue(Unit)
                return
            } else {
                val jArray = answer.get(RESULT_TAG) as JSONArray
                val albums = ArrayList<BaseAlbumInfo>()
                var album: JSONObject
                for (i in 0 until count) {
                    album = jArray.getJSONObject(i)
                    albums.add(
                        BaseAlbumInfo(
                            album.getString(COLLECTION_NAME),
                            album.getString(ARTIST_NAME),
                            album.getString(ARTWORK),
                            album.getString(ID)
                        )
                    )
                }
                albums.sortBy { it.albumName.lowercase() }
                _albums.postValue(albums)
            }
        }
    }

}