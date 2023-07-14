package com.example.misheroes

import com.google.gson.annotations.SerializedName


data class CharacterResponse(
    @SerializedName("response") val response: String,
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String
)

data class SearchResponse(
    @SerializedName("response") val response: String,
    @SerializedName("results") val results: List<CharacterResponse>
)