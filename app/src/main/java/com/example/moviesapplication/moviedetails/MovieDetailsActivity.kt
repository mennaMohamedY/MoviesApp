package com.example.moviesapplication.moviedetails

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebViewClient
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
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MovieDetailsActivity : AppCompatActivity() {
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

        movieDetailsVM = ViewModelProvider(this).get(MovieDetailsViewModel::class.java)
        movieDetailsBinding.playtrailerimg
        if (movie == null) {
            Log.e("first","Apparently movie is not equal null")

            Glide.with(this).load(movie1?.posterPath).into(movieDetailsBinding.playtrailerimg)
            Glide.with(this).load(movie1?.posterPath).into(movieDetailsBinding.poster)
            //movieDetailsBinding.movienameHeader.text= movie?.title
            with(movieDetailsBinding) {
                movienameHeader.setText(movie?.title ?: "sfsdfsdfs")
                movienameHeader.text = movie1?.title
                moviename.text = movie1?.title
                //movie gener search it don't forget
                movieDescription.text = movie1?.overview
                rate.text = movie1?.voteAverage.toString()
                movieDuration.text = movie1?.releaseDate

            }
        }
        if (movie != null) {
            Log.e("second","Apparently movie is not equal null")
            Glide.with(this).load("https://image.tmdb.org/t/p/original/" + movie?.posterPath)
                .into(movieDetailsBinding.playtrailerimg)
            Glide.with(this).load("https://image.tmdb.org/t/p/original/" + movie?.posterPath)
                .into(movieDetailsBinding.poster)

            //movieDetailsBinding.movienameHeader.text= movie?.title
            with(movieDetailsBinding) {
                movienameHeader.setText(movie?.title ?: "sfsdfsdfs")
                movienameHeader.text = movie?.title
                moviename.text = movie?.title
                //movie gener search it don't forget
                movieDescription.text = movie?.overview
                rate.text = movie?.voteAverage.toString()
                movieDuration.text = movie?.releaseDate
            }
        }
        if (Movie3 != null){
            Log.e("third","watchlist movies are now playing")
            Glide.with(this).load("https://image.tmdb.org/t/p/original/" + Movie3?.posterPath)
                .into(movieDetailsBinding.playtrailerimg)
            Glide.with(this).load("https://image.tmdb.org/t/p/original/" + Movie3?.posterPath)
                .into(movieDetailsBinding.poster)

            //movieDetailsBinding.movienameHeader.text= movie?.title
            with(movieDetailsBinding) {
                movienameHeader.setText(Movie3?.title ?: "sfsdfsdfs")
                movienameHeader.text = Movie3?.title
                moviename.text = Movie3?.title
                //movie gener search it don't forget
                movieDescription.text = Movie3?.overview
                rate.text = Movie3?.voteCount.toString()
                movieDuration.text = Movie3?.releaseDate
            }
        }

        movieDetailsBinding.movienameHeader.setOnClickListener({
            finish()
        })
        getkeyFromApi()
        Log.e("key","${KeyProvider.key}")
        Log.e("key","${Key}")
        OnPlayVideoClickListener(Key?:"PLl99DlL6b4")
        getRelatedMovies()

//        similarMoviesAdapter.onMovieClickListener=object :MoviesAdapter.OnMovieClickListener{
//            override fun OnMovieClicked(movie: ResultsItem, position: Int) {
//                val intent= Intent(this@MovieDetailsActivity,MovieDetailsActivity.getInstance(movie)::class.java)
//                startActivity(intent)
//            }
//        }



    }
//    val webview = homefragmentBinding.weboview
//    val  n = "<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/g1-lYoI0hjw?si=xxGDD7AGpCC0X4IO\" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share\" allowfullscreen></iframe>"
//    val frm ="<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/uYPbbksJxIg\" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share\" allowfullscreen></iframe>"
//    val yt="<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/-nlQtxwdfR4\" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share\" allowfullscreen></iframe>"
//    //val video= "<iframe width=\"560\" height=\"315\" src=\"https://www.youtube.com/embed/UdFeVo0cODs\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share\" allowfullscreen></iframe>"
//    webview.loadData(frm,"text/html" , "utf-8")
//    webview.settings.javaScriptEnabled = true
//    //webview.webViewClient = WebViewClient()
//    webview.webChromeClient= WebChromeClient()


//    fun displayYoutubeVids() {
//        val url = "https://www.youtube.com/watch?v=UdFeVo0cODs"
//        val frameVideo =
//            "<html><body>Video From YouTube<br><iframe width=\"420\" height=\"315\" src=\"${url}\" frameborder=\"0\" allowfullscreen></iframe></body></html>";

//        val webview=homefragmentBinding.weboview
//        webview.webViewClient = WebViewClient()
//
//        //WebSettings webSettings = displayYoutubeVideo . getSettings ();
//        webview.settings.javaScriptEnabled = true
//        webview.loadData(frameVideo, "text/html", "utf-8");}
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
    fun getkeyFromApi(){
        var apikey:String?="UdFeVo0cODs"
        var id:Int?=null
        if (movie == null){
            id = 238
        }
        else{
            id = movie?.id
        }
        KeyProvider.key= null

        ApiManager.getServices().getMovieDetails(KeyProvider.id?:238,"videos",Constants.apiKey)
            .enqueue(object :Callback<SingleMovieResponse>{
                override fun onResponse(
                    call: Call<SingleMovieResponse>,
                    response: Response<SingleMovieResponse>
                ) {
                    val responseLeng=response.body()?.videos?.results
                    if(responseLeng?.size!! <=1){
                        Key= response.body()?.videos?.results?.get(0)?.key
                    }else{
                        Key= response.body()?.videos?.results?.get(1)?.key
                    }
                    for (i in 0..response.body()?.videos?.results?.size!!-1){
                        if(KeyProvider.key==null){
                            KeyProvider.key=response.body()?.videos?.results?.get(i)?.key
                        }
                        else if (KeyProvider.key == "UdFeVo0cODs"){
                            KeyProvider.key=response.body()?.videos?.results?.get(i+2)?.key

                        }else if(KeyProvider.key == "wA6iB3OZDus"){
                            KeyProvider.key=response.body()?.videos?.results?.get(i+1)?.key
                        }else
                            return
                    }

                }

                override fun onFailure(call: Call<SingleMovieResponse>, t: Throwable) {
                    val progressDialog:ProgressDialog
                    progressDialog= ProgressDialog(this@MovieDetailsActivity)
                progressDialog.setMessage("failed ${t.localizedMessage}")
                Log.e("Failure","Failed ${t.localizedMessage}")
                progressDialog.show()
                }


            } )
    }

    fun getRelatedMovies(){
        val keyy:Int=KeyProvider.id!!
        ApiManager.getServices().getSimilarMovies(keyy,Constants.apiKey)
            .enqueue(object:Callback<TMDBResponse>{
                override fun onResponse(
                    call: Call<TMDBResponse>,
                    response: Response<TMDBResponse>
                ) {
                    similarMoviesAdapter.updateData(response.body()?.results)
                    movieDetailsBinding.similarMoviesRv.adapter=similarMoviesAdapter
                }

                override fun onFailure(call: Call<TMDBResponse>, t: Throwable) {
                    val progressDialog:ProgressDialog
                    progressDialog=ProgressDialog(this@MovieDetailsActivity)
                    progressDialog.setMessage("failed ${t.localizedMessage}")
                    Log.e("Failure","Failed ${t.localizedMessage}")
                    progressDialog.show()
                }

            })

    }



}
