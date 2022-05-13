package com.bignerdranch.android.bestmovies.utils

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

// Модель приложения
data class FilmsModel (
    val films: List<Film>
)

@Parcelize // используем @Parcelize чтобы передать Film в качестве аргумента MovieDetailsFragment
data class Film(
    val id : Int = 0,
    @SerializedName("localized_name") private val _localized_name : String?,// ответ содержит string поля со значениями "null", чтобы избежать NPE дополнительно переопределяем поле
    @SerializedName("name") private val _name : String?,
    val year : Int = 0,
    val rating : Double = .0,
    @SerializedName("description") private val _description : String?,
    val genres : List<String> = listOf(),
    @SerializedName("image_url") private val _image_url : String?
): Parcelable {
    val localized_name
        get() = _localized_name ?: ""
    val name
        get() = _name ?: ""
    val image_url
        get() = _image_url ?: ""// обрабатываем NUll, если null, то возвращаем пустую строку
    val description
        get() = _description ?: ""
}