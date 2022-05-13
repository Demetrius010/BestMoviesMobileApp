package com.bignerdranch.android.bestmovies.movielist.model

import com.bignerdranch.android.bestmovies.utils.Film

sealed class RecyclerDataModel//Классы модели данных для RecyclerView
data class Title(val text: String): RecyclerDataModel() // Заголовок ("Жанры", "Фильмы")
data class Genre(val title: String, var isChecked: Boolean): RecyclerDataModel() // Название жанра
data class Movie(val film: Film): RecyclerDataModel() // Фильм