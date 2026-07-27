package com.breakingchains.app.di

import android.content.Context
import com.breakingchains.app.network.ApiClient
import retrofit2.Retrofit

interface AppContainer {
    val retrofitClient: Retrofit
}

class DefaultAppContainer(private val context: Context) : AppContainer {
    override val retrofitClient: Retrofit by lazy {
        ApiClient.instance
    }
}
