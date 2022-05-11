package com.bignerdranch.android.bestmovies.views

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bignerdranch.android.bestmovies.R
import com.bignerdranch.android.bestmovies.models.Film
import com.bumptech.glide.Glide

class MovieRecyclerViewAdapter(var filmsList: List<Film>) :
    RecyclerView.Adapter<MyViewItemHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewItemHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_film, parent, false)
        return MyViewItemHolder(view)
    }

    override fun getItemCount(): Int = filmsList.size

    override fun onBindViewHolder(holder: MyViewItemHolder, position: Int) {
        Log.d("onBindViewHolder", "$position")
        val dataItem = filmsList[position]
        holder.bind(dataItem)
    }
}

class MyViewItemHolder(val view: View): RecyclerView.ViewHolder(view), View.OnClickListener{
    private val filmImage: ImageView = view.findViewById(R.id.filmImgView)
    private val filmTitle: TextView = view.findViewById(R.id.filmTextView)
    private lateinit var filmData: Film

    init {
        view.setOnClickListener(this)
    }

    fun bind(filmData: Film){
        this.filmData = filmData
        filmTitle.text = filmData.name
        Glide.with(view)
            .load(filmData.image_url)
            .placeholder(R.color.white)
            .centerCrop()
            .into(filmImage)
    }

    override fun onClick(p0: View?) {
////Type-safe Студия может сгенерировать нам классы и методы для удобной передачи аргументов (эти классы являются оберткой над Bundle). Для этого нам понадобится safeargs плагин. см  build.gradle файл проекта.
        val action = MovieListFragmentDirections.actionMovieListFragmentToMovieDetailsFragment(filmData)//Отправитель Для fragment1 будет сгенерирован класс Fragment1Directions. Т.е. в качестве имени взято ID и добавлено слово Directions. В этом классе будет метод actionToFragment2(), который даст нам action ActionToFragment2.
        //action.setMovieData(data)//ActionToFragment2 - сгенерированный класс для action actionToFragment2. У этого класса есть методы, соответствующие аргументам этого action
        findNavController(p0!!).navigate(action)//R.id.action_scrollingFragment_to_detailFragment) //Получаем ActionToFragment2 из Fragment1Directions, задаем значения аргументов и передаем этот action в метод navigate.
        //findNavController().navigate(R.id.movieDetailsFragment)
    }
}