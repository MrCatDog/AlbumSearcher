package com.example.albumsearcher

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import okhttp3.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException

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

    fun searchTextChanged(text: CharSequence) {
        if (text.trim().isNotEmpty()) {
            call?.cancel() //todo вот это скорее всего потоконебезопасно
            findMore(text) //или безопасно?
        }
    }

    fun findMore(text: CharSequence) {
        call = client.newCall(
            Request.Builder()
                .url(URL_BASE + text + URL_PARAMETERS)
                .build()
        ).apply {
            enqueue(
                object : Callback {
                    override fun onResponse(call: Call, response: Response) {
                        // TODO: ответ может быть успешным, но с ошибкой обращения к API
                        // todo: продумать как не запихивать весь ответ в память, а использовать потоком или тип того
                        if (response.isSuccessful) {
                            val answer = JSONObject(response.body?.string())
                            val count = answer.get(RESULT_COUNT_TAG) as Int
                            val jArray = answer.get(RESULT_TAG) as JSONArray
                            val albums = ArrayList<BaseAlbumInfo>()
                            var album: JSONObject
                            for (i in 0 until count) {
                                album = jArray.getJSONObject(i)
                                albums.add(
                                    BaseAlbumInfo(
                                        album.getString("collectionName"),
                                        album.getString("artistName"),
                                        album.getString("artworkUrl100"),
                                        album.getDouble("collectionId")
                                    )
                                )
                            }
                            _albums.postValue(albums)
                        } else {
                            //TODO("some troubles maybe show a snack?")
                        }
                    }

                    override fun onFailure(call: Call, e: IOException) {
                        //TODO("Not yet implemented")
                    }
                }
            )
        }
    }
}