package com.bignerdranch.android.bestmovies.views

import android.opengl.Visibility
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.marginLeft
import androidx.core.view.setPadding
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bignerdranch.android.bestmovies.R
import com.bignerdranch.android.bestmovies.databinding.FragmentMovieListBinding
import com.bignerdranch.android.bestmovies.models.DataFetcher
import com.bignerdranch.android.bestmovies.models.Film
import com.bignerdranch.android.bestmovies.presenters.MovieListFragmentPresenter
import moxy.MvpAppCompatFragment
import moxy.presenter.InjectPresenter

class MovieListFragment : MvpAppCompatFragment(), IMovieListFragmentView{
    private var _binding: FragmentMovieListBinding? = null
    private val binding// This property is only valid between onCreateView and onDestroyView.
        get() = _binding!!

    private lateinit var movieRecyclerView: RecyclerView

    @InjectPresenter
    lateinit var movieListFragmentPresenter: MovieListFragmentPresenter//Presenter будет жить пока есть View, в которой он содержится(+ пока происходит смена конфигурации)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMovieListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //            findNavController().navigate(R.id.action_movieListFragment_to_movieDetailsFragment)
    }

    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.movie_list_frigment_title)
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    //VIEW IMPLEMENTATION
    override fun startDataFetching() {
        binding.progressBar.visibility = View.VISIBLE
        binding.movieRecyclerView.visibility = View.INVISIBLE
    }

    override fun onError(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

    override fun onSuccess(films: List<Film>) {
        binding.progressBar.visibility = View.INVISIBLE
        binding.movieRecyclerView.visibility = View.VISIBLE
        setupRecyclerView(films)
        setupGenresFilter(films)
    }

    fun setupRecyclerView(films: List<Film>){
        movieRecyclerView = binding.movieRecyclerView
        movieRecyclerView.layoutManager = GridLayoutManager(context, 2)
        movieRecyclerView.adapter = MovieRecyclerViewAdapter(films)
    }

    fun setupGenresFilter(filmsList: List<Film>){
        val genresSet = mutableSetOf<String>()
        for (film in filmsList){
            genresSet.addAll(film.genres)
        }

        val radioGroup = RadioGroup(context)
        var index = 0
        for(genre in genresSet){
            val genreRadioBtn = layoutInflater.inflate(R.layout.genre_item, null, false) as RadioButton
            genreRadioBtn.text = genre.replaceFirstChar { c -> c.uppercase() }
            genreRadioBtn.id = View.generateViewId()
            radioGroup.addView(genreRadioBtn, index++)
        }
        binding.genresFilterContainer.addView(radioGroup)
    }
}
