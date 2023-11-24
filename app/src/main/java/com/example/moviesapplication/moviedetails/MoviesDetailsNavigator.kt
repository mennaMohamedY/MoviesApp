package com.example.moviesapplication.moviedetails

import androidx.lifecycle.ViewModel

interface MoviesDetailsNavigator{

    fun showProgressDialog(msg:String)
    fun onPlayVideoClick()
}