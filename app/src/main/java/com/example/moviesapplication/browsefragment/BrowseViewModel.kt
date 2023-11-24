package com.example.moviesapplication.browsefragment

import android.app.ProgressDialog
import android.content.Context
import android.util.Log
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import com.example.moviesapplication.Constants
import com.example.moviesapplication.apimanager.ApiManager
import com.example.moviesapplication.module.MovieGenresItem
import com.example.moviesapplication.module.ResultsItem
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class BrowseViewModel :ViewModel() {
    var Movies=MutableLiveData<MutableList<MovieGenresItem?>?>()
    var navigator:BrowseNavigator?=null
    var Movies2=MutableLiveData<List<ResultsItem?>?>()




    fun getMoviesGenre(context: Context){
        viewModelScope.launch {
            try {
                val response=ApiManager.getServices().getMoviesGenre(Constants.apiKey)
                Movies.value=(response.genres as MutableList<MovieGenresItem?>?)
                //BrowseAdapter.UpdateList(response.genres as MutableList<MovieGenresItem?>?)
                GenreListProvider.GenreList=response.genres
               // BrowseBinding.moviesGenreRv.adapter=BrowseAdapter
                GenreListProvider.whichAdapter=1
            }catch (e:Exception){
                navigator?.showProgressDialog(e.localizedMessage)
            }
        }
    }


    fun getMoviesByID(genre:MovieGenresItem){
        viewModelScope.launch{
            try {
                val response= ApiManager.getServices().getMoviesByGenreID(Constants.apiKey, genre.id!!)
                Movies2.value=response.results

                //scdBrowseAdapter.updateData(response.results)
                //BrowseBinding.moviesGenreRv.adapter=scdBrowseAdapter
                GenreListProvider.whichAdapter=2
                GenreListProvider.SelectedGenreMovies=response.results
            }catch (e:Exception){
                navigator?.showProgressDialog(e.localizedMessage)
            }
        }

    }





}