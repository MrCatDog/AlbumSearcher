package com.example.albumsearcher

import androidx.lifecycle.ViewModel
import okhttp3.*
import java.io.IOException

const val URL_BASE = "https://itunes.apple.com/search?term="
const val URL_PARAMETERS = "&media=music&entity=album&attribute=albumTerm"
//
//Поиск по ID альбома, первый результат - сам альбом (можно использовать для доп информации)
//https://itunes.apple.com/lookup?id=545398133&entity=song&attribute=albumTerm

class MainViewModel : ViewModel() {

    private val client = OkHttpClient()

    fun searchTextChanged(text: CharSequence) {
        if (text.trim().isNotEmpty()) {
            client.newCall(
                Request.Builder()
                    .url(URL_BASE + text + URL_PARAMETERS)
                    .build()
            ).apply {
                enqueue(
                    object : Callback {
                        override fun onResponse(call: Call, response: Response) {
                            TODO("Not yet implemented")
                        }

                        override fun onFailure(call: Call, e: IOException) {
                            TODO("Not yet implemented")
                        }
                    }
                )
            }
        }
    }
}