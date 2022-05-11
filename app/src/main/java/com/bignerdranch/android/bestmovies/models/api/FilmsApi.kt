package com.bignerdranch.android.bestmovies.models.api

import com.bignerdranch.android.bestmovies.models.FilmsModel
import retrofit2.Call
import retrofit2.http.GET

interface FilmsApi {
    @GET("films.json")
    fun getFilms(): Call<FilmsModel>
}