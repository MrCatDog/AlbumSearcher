package com.example.albumsearcher.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.albumsearcher.util.MutableLiveEvent
import okhttp3.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import java.util.*

const val URL_BASE = "https://itunes.apple.com/search?term="
const val URL_PARAMETERS = "&media=music&entity=album&attribute=albumTerm"
const val RESULT_COUNT_TAG = "resultCount"
const val RESULT_TAG = "results"
//
//Поиск по ID альбома, первый результат - сам альбом (можно использовать для доп информации)
//https://itunes.apple.com/lookup?id=545398133&entity=song&attribute=albumTerm

class MainViewModel : ViewModel() {

    private val client = OkHttpClient()
    private var call: Call? = null

    private val _albums = MutableLiveData<List<BaseAlbumInfo>>()
    val albums: LiveData<List<BaseAlbumInfo>>
        get() = _albums

    private val _errMsg = MutableLiveEvent<String>()
    val errMsg: LiveData<String>
        get() = _errMsg

    fun getItemIDByPosition(position: Int) : String? = _albums.value?.get(position)?.albumId

    fun searchTextChanged(text: String) {
        if (text.trim().isNotEmpty()) {
            call?.cancel() //todo вот это скорее всего потоконебезопасно
            findMore(text) //или безопасно?
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
                            if(count != 0) {
                                val jArray = answer.get(RESULT_TAG) as JSONArray
                                val albums = ArrayList<BaseAlbumInfo>()
                                var album: JSONObject
                                for (i in 0 until count) {
                                    album = jArray.getJSONObject(i)
                                    albums.add(
                                        BaseAlbumInfo(
                                            //todo константы
                                            album.getString("collectionName"),
                                            album.getString("artistName"),
                                            album.getString("artworkUrl100"),
                                            album.getString("collectionId")
                                        )
                                    )
                                }
                                albums.sortBy { it.albumName.lowercase() }
                                _albums.postValue(albums)
                            } else {
                                //todo в ресурсы
                                _errMsg.postValue("No results")
                            }
                        } else {
                            _errMsg.postValue("Connection troubles: " + response.code.toString())
                        }
                    }

                    override fun onFailure(call: Call, e: IOException) {
                        if(!call.isCanceled()) {
                            _errMsg.postValue("Connection troubles: " + e.localizedMessage )
                        }
                    }
                }
            )
        }
    }
}