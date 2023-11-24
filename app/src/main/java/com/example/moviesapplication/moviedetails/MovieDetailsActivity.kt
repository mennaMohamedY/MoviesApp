package com.example.moviesapplication.moviedetails

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebViewClient
import android.widget.ImageView
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.moviesapplication.Constants
import com.example.moviesapplication.R
import com.example.moviesapplication.apimanager.ApiManager
import com.example.moviesapplication.apimanager.ChosenMovie
import com.example.moviesapplication.apimanager.KeyProvider
import com.example.moviesapplication.databinding.ActivityMovieDetailsBinding
import com.example.moviesapplication.homefragment.MoviesAdapter
import com.example.moviesapplication.module.ResultsItem
import com.example.moviesapplication.module.SingleMovieResponse
import com.example.moviesapplication.module.TMDBResponse
import com.example.moviesapplication.roomdb.WatchList
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MovieDetailsActivity : AppCompatActivity(),MoviesDetailsNavigator {
    lateinit var movieDetailsBinding: ActivityMovieDetailsBinding
    lateinit var movieDetailsVM: MovieDetailsViewModel
    var Key:String?=null
    var similarMoviesAdapter=MoviesAdapter(listOf())

    companion object {
        var movie: ResultsItem? = null
        var movie1: ChosenMovie? = null
        var Movie3:WatchList? =null

        fun getInstance(movie: ResultsItem): MovieDetailsActivity {
            this.movie = movie
            KeyProvider.id=movie.id
            return MovieDetailsActivity()
        }

        //is created for the purpose of if there wasn't any movie clicked on
        //and the deafult movie which is the godfather was clicked on
        fun getInstance2(movie: ChosenMovie): MovieDetailsActivity {
            this.movie1 = movie
            KeyProvider.id= movie1?.id
            return MovieDetailsActivity()
        }

        fun getInstance3(movie3:WatchList):MovieDetailsActivity{
            this.Movie3=movie3
            KeyProvider.id= Movie3?.id
            return MovieDetailsActivity()
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_movie_details)
        movieDetailsBinding = ActivityMovieDetailsBinding.inflate(layoutInflater)
        setContentView(movieDetailsBinding.root)
        val webview = movieDetailsBinding.weboview
        movieDetailsVM = ViewModelProvider(this).get(MovieDetailsViewModel::class.java)
        movieDetailsBinding.vm=movieDetailsVM
        movieDetailsBinding.playtrailerimg
        if (movie == null && Movie3 ==null) {
            Log.e("first","Apparently movie is not equal null")
            bindIm(this, movie1?.posterPath!!,movieDetailsBinding.playtrailerimg)
            bindIm(this, movie1?.posterPath!!,movieDetailsBinding.poster)
            movieDetailsVM.subscribeToLiveData1(movie1)
        }
        if (movie != null) {
            Log.e("second","Apparently movie is not equal null")
            bindIm(this, movie?.posterPath!!,movieDetailsBinding.playtrailerimg)
            bindIm(this, movie?.posterPath!!,movieDetailsBinding.poster)
            movieDetailsVM.subscribeToLiveData2(movie)
        }
        if (Movie3 != null){
            Log.e("third","watchlist movies are now playing")
            bindIm(this, Movie3?.posterPath!!,movieDetailsBinding.playtrailerimg)
            bindIm(this, Movie3?.posterPath!!,movieDetailsBinding.poster)
            movieDetailsVM.subscribeToLiveData3(Movie3)
        }

        movieDetailsBinding.movienameHeader.setOnClickListener({
            finish()
        })

        //movieDetailsVM.getkeyFromApi()
        Log.e("key","${KeyProvider.key}")
        Log.e("key","${Key}")

        OnPlayVideoClickListener(Key?:"PLl99DlL6b4")
        movieDetailsBinding.similarMoviesRv.adapter=similarMoviesAdapter
        movieDetailsVM.getkeyFromApi()
        subscribeToLiveData()
        movieDetailsVM.getRelatedMovies()
    }

    fun subscribeToLiveData(){
        movieDetailsVM.Key2.observe(this){
            Key=it
        }
        movieDetailsVM.similarmovies.observe(this){
            similarMoviesAdapter.updateData(it)
        }
    }
    fun bindIm(context:Context,imgPath:String,imgView:ImageView){
        Log.e("first","Apparently movie is not equal null")
        //val n=movieDetailsBinding.playtrailerimg
        Glide.with(context).load(Constants.ImgPath+imgPath).into(imgView)
        //Glide.with(this).load(movie1?.posterPath).into(movieDetailsBinding.poster)
    }


    override fun showProgressDialog(msg: String) {
        val progressDialog:ProgressDialog
        progressDialog=ProgressDialog(this@MovieDetailsActivity)
        progressDialog.setMessage("failed ${msg}")
        Log.e("Failure","Failed ${msg}")
        progressDialog.show()
    }
    fun OnPlayVideoClickListener(key:String){
        movieDetailsBinding.playerIcon.setOnClickListener({
            with(movieDetailsBinding){
                playerIcon.visibility=View.GONE
                playtrailerimg.visibility=View.GONE
            }
            //val key=getkeyFromApi()
            val webview = movieDetailsBinding.weboview
            val  n = "<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/\"${key}\"?si=xxGDD7AGpCC0X4IO\" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share\" allowfullscreen></iframe>"
            val frm ="<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/${KeyProvider.key}\" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share\" allowfullscreen></iframe>"
            val yt="<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/-nlQtxwdfR4\" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share\" allowfullscreen></iframe>"
            //val video= "<iframe width=\"560\" height=\"315\" src=\"https://www.youtube.com/embed/UdFeVo0cODs\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share\" allowfullscreen></iframe>"
            webview.loadData(frm,"text/html" , "utf-8")
            webview.settings.javaScriptEnabled = true
            //webview.webViewClient = WebViewClient()
            webview.webChromeClient= WebChromeClient()
        })
    }


    override fun onPlayVideoClick() {
        with(movieDetailsBinding){
            playerIcon.visibility= android.view.View.GONE
            playtrailerimg.visibility= android.view.View.GONE
        }
    }

}

