package com.bignerdranch.android.bestmovies.models

import android.util.Log

class Repository private constructor(){
    companion object{
        private var instance: Repository? = null

        fun get(): Repository{
            if(instance==null)
                instance = Repository()
            return instance ?: throw IllegalStateException("Repository must be initialized!")
        }
    }

    fun fetchData() = DataFetcher().fetchFilms()

}