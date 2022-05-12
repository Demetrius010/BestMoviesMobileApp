package com.bignerdranch.android.bestmovies.models

// Данный класс является синглтоном и организует доступ к данным.
// В данном приложении используется просто как посредник между DataFetcher и MovieListFragmentPresenter
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