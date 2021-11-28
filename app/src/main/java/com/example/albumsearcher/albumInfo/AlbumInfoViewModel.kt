package com.example.albumsearcher.albumInfo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.albumsearcher.main.*
import com.example.albumsearcher.util.MutableLiveEvent
import okhttp3.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException

//https://itunes.apple.com/lookup?id=545398133&entity=song&attribute=albumTerm

const val URL_BASE = "https://itunes.apple.com/lookup?id="
const val URL_PARAMETERS = "&entity=song&attribute=albumTerm"

class AlbumInfoViewModel(stringExtra: String?) : ViewModel() {

    private val client = OkHttpClient()
    private var call: Call? = null

    private val _album = MutableLiveData<AdditionalAlbumInfo>()
    val album: LiveData<AdditionalAlbumInfo>
        get() = _album

    private val _err = MutableLiveEvent<String>()
    val err: LiveData<String>
        get() = _err

    init {
        if (stringExtra != null) {
            findMore(stringExtra)
        } else {
            _err.setValue("Альбом не найден!")
        }
    }

    private fun findMore(text: String) {
        call = client.newCall(
            Request.Builder()
                .url(URL_BASE + text + URL_PARAMETERS)
                .build()
        ).apply {
            enqueue(
                object : Callback {
                    override fun onResponse(call: Call, response: Response) {
                        // todo: продумать как не запихивать весь ответ в память, а использовать потоком или тип того
                        if (response.isSuccessful) {
                            val answer = JSONObject(response.body?.string())
                            val count = answer.get(RESULT_COUNT_TAG) as Int
                            if (count != 0) {
                                val jArray = answer.get(RESULT_TAG) as JSONArray

                                jArray.getJSONObject(0).apply {
                                    _album.postValue(
                                        AdditionalAlbumInfo(
                                            albumName = this.getString("collectionName"),
                                            artistName = this.getString("artistName"),
                                            coverURL = this.getString("artworkUrl100"),
                                            price = this.getString("collectionPrice"),
                                            songCount = this.getString("trackCount"),
                                            country = this.getString("country"),
                                            currency = this.getString("currency"),
                                            release = this.getString("releaseDate"),
                                            copyright = "88888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888",//this.getString("copyright"),
                                            genre = this.getString("primaryGenreName")
                                        )
                                    )
                                }


//                                val albums = ArrayList<BaseAlbumInfo>()
//                                var album: JSONObject
//                                for (i in 0 until count) {
//                                    album = jArray.getJSONObject(i)
//                                    albums.add(
//                                        BaseAlbumInfo(
//                                            //todo константы
//                                            album.getString("collectionName"),
//                                            album.getString("artistName"),
//                                            album.getString("artworkUrl100"),
//                                            album.getString("collectionId")
//                                        )
//                                    )
//                                }
//                                albums.sortBy { it.albumName.lowercase() }
                                //_albums.postValue(albums)
                            } else {
                                //todo в ресурсы
                                //_errMsg.postValue("No results")
                            }
                        } else {
                            //_errMsg.postValue("Connection troubles: " + response.code.toString())
                        }
                    }

                    override fun onFailure(call: Call, e: IOException) {
                        if (!call.isCanceled()) {
                            //_errMsg.postValue("Connection troubles: " + e.localizedMessage )
                        }
                    }
                }
            )
        }
    }
}