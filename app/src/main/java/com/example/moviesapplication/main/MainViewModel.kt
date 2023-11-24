package com.example.moviesapplication.main

import android.app.ProgressDialog
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviesapplication.Constants
import com.example.moviesapplication.apimanager.ApiManager
import com.example.moviesapplication.apimanager.KeyProvider
import com.example.moviesapplication.module.SingleMovieResponse
import com.example.moviesapplication.moviedetails.MovieDetailsActivity
import com.example.moviesapplication.moviedetails.MoviesDetailsNavigator
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel :ViewModel() {

}