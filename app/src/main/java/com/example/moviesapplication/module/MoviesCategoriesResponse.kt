package com.example.moviesapplication.module

import com.google.gson.annotations.SerializedName

data class MoviesCategoriesResponse(

	@field:SerializedName("genres")
	val genres: List<MovieGenresItem?>? = null
)

data class MovieGenresItem(

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("id")
	val id: Int? = null
)
