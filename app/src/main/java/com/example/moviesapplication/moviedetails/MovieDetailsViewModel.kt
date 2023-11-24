package com.example.moviesapplication.moviedetails

import android.app.ProgressDialog
import android.util.Log
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebView
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviesapplication.Constants
import com.example.moviesapplication.apimanager.ApiManager
import com.example.moviesapplication.apimanager.ChosenMovie
import com.example.moviesapplication.apimanager.KeyProvider
import com.example.moviesapplication.module.ResultsItem
import com.example.moviesapplication.module.TMDBResponse
import com.example.moviesapplication.roomdb.WatchList
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MovieDetailsViewModel : ViewModel() {
    var navigator:MoviesDetailsNavigator?=null
    var Key:String?=null
    var Key2=MutableLiveData<String?>()
    var MovieName=ObservableField<String>()
    var MoviesDate=ObservableField<String>()
    var MovieDescription=ObservableField<String>()
    var MovieRate=ObservableField<String>()
    var similarmovies=MutableLiveData<List<ResultsItem?>?>()

    fun subscribeToLiveData1(movie: ChosenMovie?){
        MovieName.set(movie?.title)
        MovieDescription.set(movie?.overview)
        MoviesDate.set(movie?.releaseDate)
        MovieRate.set(movie?.voteAverage.toString())
    }
    fun subscribeToLiveData2(movie: ResultsItem?){
        MovieName.set(movie?.title)
        MovieDescription.set(movie?.overview)
        MoviesDate.set(movie?.releaseDate)
        MovieRate.set(movie?.voteAverage.toString())

    }
    fun subscribeToLiveData3(movie: WatchList?){
        MovieName.set(movie?.title)
        MovieDescription.set(movie?.overview)
        MoviesDate.set(movie?.releaseDate)
        MovieRate.set(movie?.voteCount.toString())
    }


    fun getkeyFromApi(){
        //these if condition becouse som movies trailer key give an error so
        //if this trailer give an error try another trailer with different key

        var apikey:String?="UdFeVo0cODs"
        var id:Int?=null
        if (MovieDetailsActivity.movie == null){
            id = 238
        }
        else{
            id = MovieDetailsActivity.movie?.id
        }
        KeyProvider.key= null

        viewModelScope.launch {
            try {
                val response=
                    ApiManager.getServices().getMovieDetails(KeyProvider.id?:238,"videos", Constants.apiKey)
                val responseLeng=response.videos?.results
                if(responseLeng?.size!! <=1){
                    //if the videos results only returned one trailer then get the only key of it which will be in position 1
                   // Key=responseLeng.get(0)?.key
                    Key2.value=responseLeng.get(0)?.key

                    //Key= response.body()?.videos?.results?.get(0)?.key
                }else{
                    //else if there are multiple video trailers then key the first key
                    //key 0 gave error in many movies so get 1 and the videos results could be only 2 trailers that's why i choose 1
                    //Key=responseLeng.get(1)?.key
                    Key2.value=responseLeng.get(1)?.key
                    // Key= response.body()?.videos?.results?.get(1)?.key
                }
                for (i in 0..responseLeng.size!!-1){
                    if(KeyProvider.key==null){
                        KeyProvider.key=responseLeng.get(i)?.key
                    }
                    else if (KeyProvider.key == "UdFeVo0cODs"){
                        KeyProvider.key=responseLeng.get(i+2)?.key

                    }else if(KeyProvider.key == "wA6iB3OZDus"){
                        KeyProvider.key=responseLeng.get(i+1)?.key
                    }else{
                    }
                }

            }catch (e:Exception){
                navigator?.showProgressDialog(e.localizedMessage)

            }
        }
    }


    fun getRelatedMovies(){
        val keyy:Int=KeyProvider.id!!
        viewModelScope.launch {
            try {
                val response=ApiManager.getServices().getSimilarMovies(keyy,Constants.apiKey)
                similarmovies.value=response.results

            }catch (e:Exception){
                navigator?.showProgressDialog(e.localizedMessage)

            }
        }}

}