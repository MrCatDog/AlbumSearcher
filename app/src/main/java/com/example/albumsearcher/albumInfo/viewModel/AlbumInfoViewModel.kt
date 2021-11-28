package com.example.albumsearcher.albumInfo.viewModel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.albumsearcher.R
import com.example.albumsearcher.albumInfo.model.AdditionalAlbumInfo
import com.example.albumsearcher.albumInfo.model.SongInfo
import com.example.albumsearcher.util.DataReceiver
import com.example.albumsearcher.util.MutableLiveEvent
import okhttp3.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException

//https://itunes.apple.com/lookup?id=545398133&entity=song&attribute=albumTerm

const val URL_BASE = "https://itunes.apple.com/lookup?id="
const val URL_PARAMETERS = "&entity=song&attribute=albumTerm&limit=200"

const val RESULT_COUNT_TAG = "resultCount"
const val RESULT_TAG = "results"
const val COLLECTION_NAME = "collectionName"
const val ARTIST_NAME = "artistName"
const val ARTWORK = "artworkUrl100"
const val PRICE = "collectionPrice"
const val TRACK_COUNT = "trackCount"
const val COUNTRY = "country"
const val CURRENCY = "currency"
const val RELEASE = "releaseDate"
const val COPYRIGHT = "copyright"
const val GENRE = "primaryGenreName"
const val EXPLICITNESS = "collectionExplicitness"

const val SONG_NAME = "trackName"

class AlbumInfoViewModel(stringExtra: String?) : ViewModel() {

    private val dataReceiver = DataReceiver()

    private val _album = MutableLiveData<AdditionalAlbumInfo>()
    val album: LiveData<AdditionalAlbumInfo>
        get() = _album

    private val _songs = MutableLiveData<List<SongInfo>>()
    val songs: LiveData<List<SongInfo>>
        get() = _songs

    private val _err = MutableLiveEvent<Exception>()
    val err: LiveData<java.lang.Exception>
        get() = _err

    init {
        if (stringExtra != null) {
            findMore(stringExtra)
        } else {
            _err.setValue(IOException("Альбом не найден!"))
        }
    }

    private fun findMore(text: String) {
        dataReceiver.requestData(
            URL_BASE + text + URL_PARAMETERS,
            this::onResponse,
            this::onFailure
        )
    }

    private fun onResponse(response: Response) {
        if (!response.isSuccessful) {
            _err.postValue(IOException(response.message + response.code.toString()))
        } else {
            val answer = JSONObject(response.body?.string())
            val count = answer.get(RESULT_COUNT_TAG) as Int
            if (count == 0) {
                _err.postValue(IOException("Album not found!"))
            } else {
                val jArray = answer.get(RESULT_TAG) as JSONArray
                jArray.getJSONObject(0).apply {
                    _album.postValue(
                        AdditionalAlbumInfo(
                            albumName = this.getString(COLLECTION_NAME),
                            artistName = this.getString(ARTIST_NAME),
                            coverURL = this.getString(ARTWORK),
                            price = this.getString(PRICE),
                            songCount = this.getString(TRACK_COUNT),
                            country = this.getString(COUNTRY),
                            currency = this.getString(CURRENCY),
                            release = this.getString(RELEASE),
                            copyright = this.getString(COPYRIGHT),
                            genre = this.getString(GENRE),
                            explicit = this.getString(EXPLICITNESS)
                        )
                    )
                }
                val songs = ArrayList<SongInfo>()
                for (i in 1 until count) {
                    val song = jArray.getJSONObject(i)
                    songs.add(
                        SongInfo(
                            number = i.toString(),
                            name = song.getString(SONG_NAME)
                        )
                    )
                }
                _songs.postValue(songs)
            }
        }
    }

    private fun onFailure(call: Call, e: IOException) {
        if (!call.isCanceled()) {
            _err.postValue(e)
        }
    }
}