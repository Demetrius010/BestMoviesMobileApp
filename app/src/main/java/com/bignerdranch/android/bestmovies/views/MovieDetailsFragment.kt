package com.bignerdranch.android.bestmovies.views

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bignerdranch.android.bestmovies.MainActivity
import com.bignerdranch.android.bestmovies.R
import com.bignerdranch.android.bestmovies.databinding.FragmentMovieDetailsBinding
import com.bignerdranch.android.bestmovies.databinding.FragmentMovieListBinding
import com.bumptech.glide.Glide

class MovieDetailsFragment : Fragment() {
    private var _binding: FragmentMovieDetailsBinding? = null
    private val binding// This property is only valid between onCreateView and onDestroyView.
    get() = _binding!!

    private var actionBar: androidx.appcompat.app.ActionBar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        actionBar = (activity as AppCompatActivity).supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            android.R.id.home ->{
                findNavController().navigateUp()
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
            val movieData = args.movieData
            //Log.d("DETAILS", movieData.name + " " + movieData.year + " " + movieData.rating)
            binding.run {
                filmName.text = movieData.name
                filmDate.text = movieData.year.toString()
                filmRating.text = movieData.rating.toString()
                filmDesc.text = movieData.description
                actionBar?.title = movieData.localized_name
                Glide.with(requireContext())
                    .load(movieData.image_url)
                    .placeholder(R.color.white)
                    .onlyRetrieveFromCache(true)
                    .into(filmImage)
            }
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onDestroy() {
        super.onDestroy()
        actionBar?.setDisplayHomeAsUpEnabled(false)
    }

}