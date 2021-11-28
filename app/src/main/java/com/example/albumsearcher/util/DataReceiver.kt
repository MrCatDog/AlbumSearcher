package com.example.albumsearcher.util

import okhttp3.*
import java.io.IOException
import kotlin.reflect.KFunction1
import kotlin.reflect.KFunction2

class DataReceiver {

    private val client = OkHttpClient()
    private var call: Call? = null

    fun requestData(
        url: String,
        onResponse: KFunction1<Response, Unit>,
        onFail: KFunction2<Call, IOException, Unit>
    ) {
        call = client.newCall(
            Request.Builder()
                .url(url)
                .build()
        ).apply {
            enqueue(object : Callback {

                override fun onFailure(call: Call, e: IOException) {
                    onFail(call, e)
                }

                override fun onResponse(call: Call, response: Response) {
                    onResponse(response)
                }

            })
        }
    }

    fun cancel() {
        call?.cancel()
    }
}