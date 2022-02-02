package com.example.nasapictureoftheday.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.nasapictureoftheday.BuildConfig
import com.example.nasapictureoftheday.model.PODServerResponseData
import com.example.nasapictureoftheday.model.PictureOfTheDayAPI
import com.example.nasapictureoftheday.model.PictureOfTheDayData
import retrofit2.*

class PictureOfTheDayViewModel (
    private val liveDataForViewToObserve: MutableLiveData<PictureOfTheDayData> =
        MutableLiveData()
    ) :
    ViewModel() {
        fun getPictureOfTheDay(retrofit: Retrofit): LiveData<PictureOfTheDayData> {
            sendServerRequest(retrofit)
            return liveDataForViewToObserve
        }
        private fun sendServerRequest(retrofit: Retrofit) {
            liveDataForViewToObserve.value = PictureOfTheDayData.Loading(null)
            val apiKey: String = BuildConfig.NASA_API_KEY
            if (apiKey.isBlank()) {
                PictureOfTheDayData.Error(Throwable("You need API key"))
            } else {
                retrofit.create(PictureOfTheDayAPI::class.java).getPictureOfTheDay(apiKey).enqueue(object :
                    Callback<PODServerResponseData> {
                    override fun onResponse(
                        call: Call<PODServerResponseData>,
                        response: Response<PODServerResponseData>
                    ) {
                        if (response.isSuccessful && response.body() != null) {
                            liveDataForViewToObserve.value =
                                PictureOfTheDayData.Success(response.body()!!)
                        } else {
                            val message = response.message()
                            if (message.isNullOrEmpty()) {
                                liveDataForViewToObserve.value =
                                    PictureOfTheDayData.Error(Throwable("Unidentified error"))
                            } else {
                                liveDataForViewToObserve.value =
                                    PictureOfTheDayData.Error(Throwable(message))
                            }
                        }
                    }
                    override fun onFailure(call: Call<PODServerResponseData>, t:
                    Throwable) {
                        liveDataForViewToObserve.value = PictureOfTheDayData.Error(t)
                    }
                })
            }
        }
    }


