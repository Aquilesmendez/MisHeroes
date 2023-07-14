package com.example.misheroes

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface APIService {

    @GET("search/{name}")
    fun searchCharacterByName(@Path("name") name: String): Call<SearchResponse>


}
