package com.bignerdranch.android.bestmovies.movielist.model

import androidx.recyclerview.widget.DiffUtil

class RecyclerDataDiffUtill(val oldList: List<RecyclerDataModel>, val newList: List<RecyclerDataModel>) :
    DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    //сначала вызывается метод areItemsTheSame, чтобы определить, надо ли в принципе сравнивать
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem: RecyclerDataModel = oldList[oldItemPosition]
        val newItem: RecyclerDataModel = newList[newItemPosition]
        if ((oldItem is Movie) && (newItem is Movie))
            return oldItem.film.id == newItem.film.id
        else if ((oldItem is Genre) && (newItem is Genre))
            return oldItem.title == newItem.title
        else if((oldItem is Title) && (newItem is Title))
            return oldItem.text == newItem.text
        else
            return false
    }

    // сравниваем содержимое
    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem: RecyclerDataModel = oldList[oldItemPosition]
        val newItem: RecyclerDataModel = newList[newItemPosition]
        if ((oldItem is Movie) && (newItem is Movie))
            return oldItem==newItem
        else if ((oldItem is Genre) && (newItem is Genre))
            return oldItem.isChecked == newItem.isChecked
        else if((oldItem is Title) && (newItem is Title))
            return oldItem.text == newItem.text
        else
            return false
    }
}