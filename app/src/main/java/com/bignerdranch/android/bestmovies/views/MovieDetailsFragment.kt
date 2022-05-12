package com.bignerdranch.android.bestmovies.views

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bignerdranch.android.bestmovies.R
import com.bignerdranch.android.bestmovies.databinding.FragmentMovieDetailsBinding
import com.bumptech.glide.Glide

// Данный фрагмент не имеет Presenter, поскольку здесь просто отображаются данные модели, с которыми пользователь никак не взаимодействует
class MovieDetailsFragment : Fragment() {
    private var _binding: FragmentMovieDetailsBinding? = null
    private val binding// This property is only valid between onCreateView and onDestroyView.
    get() = _binding!!

    private var actionBar: androidx.appcompat.app.ActionBar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        actionBar = (activity as AppCompatActivity).supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true) // отображаем кнопку назад в ActionBar
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            android.R.id.home ->{
                findNavController().navigateUp()//нажав кнопку назад в ActionBar возращаемся к списку фильмов
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMovieDetailsBinding.inflate(inflater, container, false)

        arguments?.let {
            val args = MovieDetailsFragmentArgs.fromBundle(it)
            val movieData = args.movieData// получаем выбранный пользователем фильм из аргументов фрагмента
            binding.run {
                filmName.text = movieData.name
                filmDate.text = getString(R.string.year, movieData.year.toString())
                filmRating.text = getString(R.string.rating, movieData.rating.toString())
                filmDesc.text = movieData.description
                actionBar?.title = movieData.localized_name // менем заголовок actionBar на название фильма
                Glide.with(requireContext())// загружаем обложку фильма из кеша
                    .load(movieData.image_url)
                    .placeholder(R.drawable.ic_image_not_supported)
                    .error(R.drawable.ic_broken_image)
                    .onlyRetrieveFromCache(true)
                    .into(filmImage)
            }
        }
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onDestroy() {
        super.onDestroy()
        actionBar?.setDisplayHomeAsUpEnabled(false)//скрываем кнопку назад в ActionBar
    }

}