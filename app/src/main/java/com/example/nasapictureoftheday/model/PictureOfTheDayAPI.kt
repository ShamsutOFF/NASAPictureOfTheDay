package com.example.nasapictureoftheday.model

import com.example.nasapictureoftheday.BuildConfig
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PictureOfTheDayAPI {
    @GET("planetary/apod?&api_key=${BuildConfig.NASA_API_KEY}")
    fun getPictureOfTheDay(
        @Query("date")date : String
    ):
            Call<PODServerResponseData>
}