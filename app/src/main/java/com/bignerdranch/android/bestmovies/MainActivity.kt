package com.bignerdranch.android.bestmovies

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

//  MainActivity существует только в качестве контейнера для фрагментов.
//  Навигация по фрагментам осуществляется через Jetpack Navigation
//  стартовым фрагментом является MovieListFragment
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}