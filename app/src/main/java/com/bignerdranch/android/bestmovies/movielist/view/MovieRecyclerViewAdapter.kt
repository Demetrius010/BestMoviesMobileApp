package com.bignerdranch.android.bestmovies.movielist.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.navigation.Navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bignerdranch.android.bestmovies.R
import com.bignerdranch.android.bestmovies.movielist.model.Genre
import com.bignerdranch.android.bestmovies.movielist.model.Movie
import com.bignerdranch.android.bestmovies.movielist.model.RecyclerDataModel
import com.bignerdranch.android.bestmovies.movielist.model.Title
import com.bumptech.glide.Glide
import java.util.*

interface OnGenreItemClickListener { // Callback - вызываем в как только будет выбран жанр филма из фильтра
    fun onClick(string: String)
}

class MovieRecyclerViewAdapter(val onGenreItemClickListener: OnGenreItemClickListener)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val mDataList: MutableList<RecyclerDataModel> = LinkedList()

    fun getData() = mDataList
    fun setData(newData: List<RecyclerDataModel>){
        mDataList.clear()
        mDataList.addAll(newData)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when(viewType){ // на каждый viewType свой layout и ViewHolder
            TYPE_TITLE -> TitleViewHolder(inflater.inflate(R.layout.item_title, parent, false))
            TYPE_GENRE -> GenreViewHolder(inflater.inflate(R.layout.item_genre, parent, false), onGenreItemClickListener)
            TYPE_MOVIE -> MovieViewHolder(inflater.inflate(R.layout.item_film, parent, false))
            else -> throw IllegalStateException("Invalid type")
        }
    }

    override fun getItemCount(): Int = mDataList.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder){
            is TitleViewHolder -> holder.bind(mDataList[position] as Title)
            is GenreViewHolder -> holder.bind(mDataList[position] as Genre)
            is MovieViewHolder -> holder.bind(mDataList[position] as Movie)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (mDataList[position]){
            is Movie -> TYPE_MOVIE
            is Genre -> TYPE_GENRE
            is Title -> TYPE_TITLE
        }
    }

    companion object {
        private const val TYPE_TITLE = 0
        private const val TYPE_GENRE = 1
        private const val TYPE_MOVIE = 2
    }
}

class MovieViewHolder(val view: View): RecyclerView.ViewHolder(view){ // ViewHolder для фильмов
    private val filmImage: ImageView = view.findViewById(R.id.filmImgView)
    private val filmTitle: TextView = view.findViewById(R.id.filmTextView)

    fun bind(item: Movie){
        //заполняем layout (item_film) полученными данными
        filmTitle.text = item.film.localized_name
        Glide.with(view)// загружаем изображение с помощью Glide
            .load(item.film.image_url)
            .placeholder(R.drawable.ic_image_not_supported) // показываем в ImageView placeholder пока загружаем
            .error(R.drawable.ic_broken_image) // отображается в случае ошибки (например если запрошенный URL = null)
            .centerCrop() // вырезаем центр
            .into(filmImage) // помещаем в ImageView

        view.setOnClickListener{view ->
            // по нажатию на конкретный фильм открываем MovieDetailsFragment
            val action = MovieListFragmentDirections.actionMovieListFragmentToMovieDetailsFragment(item.film)//получаем action на MovieDetailsFragment + передаем аргумент Film  используя Safe Args Gradle plugin
            findNavController(view).navigate(action)// открываем MovieDetailsFragment
        }
    }
}

class GenreViewHolder(view: View, val onGenreItemClickListener: OnGenreItemClickListener): RecyclerView.ViewHolder(view) {//ViewHolder для жанра
    private val genreBtn: RadioButton = view.findViewById(R.id.genreBtn) // Почему RadioButton, а не Button - на случай если понадобится отобразить чекбокс

    fun bind(data: Genre){
        genreBtn.text = data.title.replaceFirstChar { c -> c.uppercase() }
        genreBtn.isChecked = data.isChecked
        genreBtn.setOnClickListener{
            onGenreItemClickListener.onClick(data.title.lowercase())//отправляем выбранный жанр в MovieListFragment
        }
    }
}

class TitleViewHolder(view: View): RecyclerView.ViewHolder(view) {//ViewHolder для заголовка
    private val title: TextView = view.findViewById(R.id.titleTV)

    fun bind(data: Title){
        title.text = data.text
    }
}