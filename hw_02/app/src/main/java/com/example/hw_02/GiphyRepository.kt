package com.example.hw_02

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface GiphyApi {
    @GET("v1/gifs/trending")
    suspend fun getTrendingGifs(
        @Query("api_key") apiKey: String,
        @Query("limit") limit: Int,
        @Query("offset") offset: Int
    ): GiphyResponse
}

class GiphyRepository(private val api: GiphyApi) {
    private val cache = mutableListOf<GifData>()
    private val apiKey = "lPd0QCgnef9ylXtkps19Eg7wXcBDi5bc"

    suspend fun getTrendingGifs(offset: Int, limit: Int): List<GifData> {
        val response = api.getTrendingGifs(apiKey, limit, offset)
        cache.addAll(response.data)
        return cache
    }
}

object RepositoryProvider {
    fun provideGiphyRepository(): GiphyRepository {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.giphy.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val api = retrofit.create(GiphyApi::class.java)
        return GiphyRepository(api)
    }
}
