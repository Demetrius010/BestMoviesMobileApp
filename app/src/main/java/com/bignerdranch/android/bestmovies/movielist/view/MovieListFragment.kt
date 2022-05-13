package com.bignerdranch.android.bestmovies.movielist.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.GridLayoutManager
import com.bignerdranch.android.bestmovies.R
import com.bignerdranch.android.bestmovies.databinding.FragmentMovieListBinding
import com.bignerdranch.android.bestmovies.movielist.model.RecyclerDataDiffUtill
import com.bignerdranch.android.bestmovies.movielist.presenter.MovieListFragmentPresenter
import com.bignerdranch.android.bestmovies.movielist.model.RecyclerDataModel
import moxy.MvpAppCompatFragment
import moxy.presenter.InjectPresenter

class MovieListFragment : MvpAppCompatFragment(), IMovieListFragmentView {
    private var _binding: FragmentMovieListBinding? = null //используется view binding для ссылки на layout
    private val binding //This property is only valid between onCreateView and onDestroyView.
        get() = _binding!!

    @InjectPresenter//Инжектим Presenter через Moxy
    lateinit var presenter: MovieListFragmentPresenter

    private val onGenreItemClickListener = object : OnGenreItemClickListener {// callback из RecyclerView
        override fun onClick(string: String) { // вызывается по нажатию на жанр в списке жанров
            updateRecyclerData(string)
        }
    }
    private val adapter = MovieRecyclerViewAdapter(onGenreItemClickListener)


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMovieListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.movie_list_frigment_title) // меняем title т.к. в MovieDetailsFragment он менялся на название фильма
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun startDataFetching() { // показываем ProgressBar пока получаем данных
        showProgressBar(true)
    }

    override fun onFailure(message: String) { // в случае неудачи (например: нет интернет соединения) показываем AlertDialog
        showProgressBar(false)
        AlertDialog.Builder(requireContext())
            .setTitle("Ошибка!")
            .setMessage(message)
            .setNegativeButton("Ok", null)
            .show()
    }

    override fun onSuccess(firsDataPart: List<RecyclerDataModel>, secondDataPart: List<RecyclerDataModel>) { // как только получили данные скрываем ProgressBar и инициализируем RecyclerView
        showProgressBar(false)
        val layoutManager = GridLayoutManager(context, 2)// LayoutManager в 2 столбца
        layoutManager.setSpanSizeLookup(object : GridLayoutManager.SpanSizeLookup(){
            override fun getSpanSize(position: Int): Int {//  для названий жанров и заголовков объединяем две ечейки: в 1 строке 1 ячейка
                if (position > (firsDataPart.size-1))//firsDataPart содержит названия жанров и 2 заголовка (отнимаем 1 т.к. ведем счет с 0)
                    return 1 // 1 ячейка
                else
                    return 2 // 2 ячейка
            }
        })
        adapter.setData(firsDataPart+secondDataPart)
        binding.movieRecyclerView.layoutManager = layoutManager
        binding.movieRecyclerView.adapter = adapter
    }

    fun updateRecyclerData(genre: String){// получаем новые данные для выбранного жанра
        val newData =  mutableListOf<RecyclerDataModel>()
        newData.addAll(presenter.getFilterDataPart(genre)) // получаем обновленный фильтр жанров
        newData.addAll(presenter.getMoviesByGenreDataPart(genre)) // получаем фильмы выбранного жанра
        val myDiffUtill = RecyclerDataDiffUtill(adapter.getData(), newData)// старые и новых данные сравниваются в DiffUtill
        val diffResult = DiffUtil.calculateDiff(myDiffUtill)//выполняем сравнение двух списков, имеет смысл вызывать этот метод асинхронно.
        adapter.setData(newData)// передаем в адаптер новые данные
        binding.movieRecyclerView.post {
            //if (!binding.movieRecyclerView.isComputingLayout() && binding.movieRecyclerView.scrollState == SCROLL_STATE_IDLE) {
            diffResult.dispatchUpdatesTo(adapter)// просим diffResult обновить RecyclerView с учетом изменений.
        }
    }

    fun showProgressBar(boolean: Boolean){
        if(boolean){
            binding.progressBar.visibility = View.VISIBLE
            binding.movieRecyclerView.visibility = View.INVISIBLE
        }
        else{
            binding.progressBar.visibility = View.INVISIBLE
            binding.movieRecyclerView.visibility = View.VISIBLE
        }
    }
}
