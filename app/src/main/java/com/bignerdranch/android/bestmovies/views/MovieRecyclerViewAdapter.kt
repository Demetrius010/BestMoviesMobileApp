package com.bignerdranch.android.bestmovies.views

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.Navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bignerdranch.android.bestmovies.R
import com.bignerdranch.android.bestmovies.models.Film
import com.bumptech.glide.Glide

class MovieRecyclerViewAdapter(var filmsList: List<Film>) :
    RecyclerView.Adapter<MovieViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_film, parent, false)
        return MovieViewHolder(view)
    }

    override fun getItemCount(): Int = filmsList.size

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bind(filmsList[position])
    }
}

class MovieViewHolder(val view: View): RecyclerView.ViewHolder(view), View.OnClickListener{
    private val filmImage: ImageView = view.findViewById(R.id.filmImgView)
    private val filmTitle: TextView = view.findViewById(R.id.filmTextView)
    private lateinit var filmData: Film

    init {
        view.setOnClickListener(this)// MovieViewHolder сам реализует View.OnClickListener
    }

    fun bind(filmData: Film){//заполняем layout (item_film) полученными данными
        this.filmData = filmData // сохраняем данные т.к. они пригодятся в методе onClick
        filmTitle.text = filmData.localized_name
        Glide.with(view)// загружаем изображение с помощью Glide
            .load(filmData.image_url)
            .placeholder(R.drawable.ic_image_not_supported) // показываем в ImageView placeholder пока загружаем
            .error(R.drawable.ic_broken_image) // отображается в случае ошибки (например если запрошенный URL = null)
            .centerCrop() // вырезаем центр
            .into(filmImage) // помещаем в ImageView
    }

    override fun onClick(p0: View?) {// по нажатию на конкретный фильм открываем MovieDetailsFragment
        val action = MovieListFragmentDirections.actionMovieListFragmentToMovieDetailsFragment(filmData)//получаем action на MovieDetailsFragment + передаем аргумент Film  используя Safe Args Gradle plugin
        findNavController(p0!!).navigate(action)// открываем MovieDetailsFragment
    }
}