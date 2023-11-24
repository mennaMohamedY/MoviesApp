package com.example.moviesapplication.watchlist

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviesapplication.moviedetails.MoviesDetailsNavigator
import com.example.moviesapplication.roomdb.RoomClass
import com.example.moviesapplication.roomdb.WatchList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class WatchListViewModel():ViewModel() {
    var watchlistNavigator:MoviesDetailsNavigator?=null

    var movieslivedata=MutableLiveData<List<WatchList>>()

    fun getWatchListMoviesFromDB(context:Context){

        GlobalScope.launch {
            WatchListProvider.watchlist=RoomClass.getInstance(context).MovieDAO().showAllFromFavourits()
        }
        movieslivedata.value=WatchListProvider.watchlist
    }

}

