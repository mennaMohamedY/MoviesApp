package com.example.moviesapplication.searchfragment

import android.app.ProgressDialog
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import com.example.moviesapplication.Constants
import com.example.moviesapplication.apimanager.ApiManager
import com.example.moviesapplication.module.ResultsItem
import com.example.moviesapplication.moviedetails.MoviesDetailsNavigator
import kotlinx.coroutines.launch

class SearchViewModel:ViewModel() {
    var nav:MoviesDetailsNavigator?=null
    var moviesDAta=MutableLiveData<List<ResultsItem?>?>()
    var pagesNum=MutableLiveData<Int?>()


    fun getMoviesByID(pageNum:Int){
        var numm:Int
        if (pageNum==0){
            numm =1
        }else{
            numm = pageNum
        }
        viewModelScope.launch {
            try {
                val response=ApiManager.getServices().getPopularMovies(Constants.apiKey,numm)
                moviesDAta.value=response.results
                val num=response.totalPages
                PagesNumProvider.pagesNum=num
                pagesNum.value=num
                PagesNumProvider.moviesList=response.results
            }catch (e:Exception){
                nav?.showProgressDialog(e.localizedMessage)
            }
        }
    }

}