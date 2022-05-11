package com.bignerdranch.android.bestmovies.models

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.bignerdranch.android.bestmovies.models.api.FilmsApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val BASE_URL = "https://s3-eu-west-1.amazonaws.com/sequeniatesttask/"
private const val TAG = "DataFetcher"

class DataFetcher {
    private val filmsApi: FilmsApi

    init {
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        filmsApi = retrofit.create(FilmsApi::class.java)
    }

    fun fetchFilms(): LiveData<List<Film>> {
        val responseLiveData: MutableLiveData<List<Film>> = MutableLiveData()
        filmsApi.getFilms().enqueue(object: Callback<FilmsModel>{
            override fun onResponse(call: Call<FilmsModel>, response: Response<FilmsModel>) {
                if (response.isSuccessful){
                    val filmsModel = response.body()
                    responseLiveData.value = filmsModel?.films ?: mutableListOf()
//                    filmsModel?.let {
//                        for(film in it.films)
//                            Log.d(TAG,"Response received: $filmsModel")
//                    }
                } else {
                    Log.d(TAG,"ERROR! ${response.code()}")
                }
            }

            override fun onFailure(call: Call<FilmsModel>, t: Throwable) {
                Log.d(TAG,"ERROR! ${t.message}")
            }
        })
        return responseLiveData
    }
}