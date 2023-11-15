package com.example.moviesapplication.browsefragment

import com.example.moviesapplication.module.MovieGenresItem
import com.example.moviesapplication.module.ResultsItem

object GenreListProvider {
    var GenreList:List<MovieGenresItem?>?=null
    var whichAdapter:Int?=null
    //note incase of Movies genres Adpate
    //whichAdapter will be set to 1
    //note incase of genre is selected and now movies are shown
    //whichAdapter will be set to 2

    var SelectedGenreMovies:List<ResultsItem?>? = null

}