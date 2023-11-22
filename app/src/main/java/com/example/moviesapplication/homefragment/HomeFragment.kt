package com.example.moviesapplication.homefragment

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebSettings.PluginState
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import com.bumptech.glide.Glide
import com.example.moviesapplication.Constants
import com.example.moviesapplication.R
import com.example.moviesapplication.apimanager.ApiManager
import com.example.moviesapplication.apimanager.ChosenMovie
import com.example.moviesapplication.browsefragment.GenreListProvider
import com.example.moviesapplication.databinding.FragmentHomeBinding
import com.example.moviesapplication.module.ResultsItem
import com.example.moviesapplication.module.TMDBResponse
import com.example.moviesapplication.moviedetails.MovieDetailsActivity
import com.example.moviesapplication.roomdb.RoomClass
import com.example.moviesapplication.roomdb.WatchList
import com.example.moviesapplication.watchlist.WatchListAdapter
import com.example.moviesapplication.watchlist.WatchListProvider
import com.example.moviesapplication.watchlist.WatchListViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import okhttp3.internal.wait
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit


class HomeFragment : Fragment() {
    lateinit var homefragmentBinding:FragmentHomeBinding
    lateinit var homefragmentViewModel:HomeFragmentViewModel
    var retrofit: Retrofit?=null
    var moviesAdapter=MoviesAdapter(listOf())
    var scdmoviesAdapter=MoviesAdapter(listOf())
    var currentChosenMovie:ResultsItem?=null
    lateinit var chosenMovieByDefault:ChosenMovie
    lateinit var WatchListVM:WatchListViewModel





    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        homefragmentViewModel=ViewModelProvider(this).get(HomeFragmentViewModel::class.java)
        WatchListVM=ViewModelProvider(this).get(WatchListViewModel::class.java)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_home, container, false)
        homefragmentBinding=DataBindingUtil.inflate(inflater,R.layout.fragment_home,container,false)
        return homefragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

       /* val videoview=homefragmentBinding.videoview
        val mediaController=MediaController(requireContext())
        mediaController.setAnchorView(videoview)
        videoview.setMediaController(mediaController)
        //homefragmentBinding.videoview.setVideoURI(Uri.parse("https://v3.cdnpk.net/videvo_files/video/premium/video0234/large_watermarked/NO%20MR_STOCK%20FOOTAGE%20NO%20MR%20%28290%29_preview.mp4"))
        homefragmentBinding.videoview.setVideoURI(Uri.parse("https://www.youtube.com/watch?v=LniY5Q2hvU4&ab_channel=MovieCoverage"))
        //homefragmentBinding.videoview.setVideoURI(Uri.parse("android.source://${Activity.packageName}/${R.raw.video2.mp}"))
        videoview.start()
        //homefragmentBinding.firstrv.adapter=moviesAdapter

        */
        Glide.with(this@HomeFragment).load("https://image.tmdb.org/t/p/original/3bhkrj58Vtu7enYsRolD1fZdja1.jpg" ).into(homefragmentBinding.movieposter)
        homefragmentBinding.movieNameTxt.text="The Godfather"
        homefragmentBinding.movieDescriptionTxt.text="1972-03-14"



        //webview.loadUrl("https://www.youtube.com/watch?v=UdFeVo0cODs")
        //+ "&autoplay=0&vq=small"
        //webview.settings.javaScriptEnabled = true
        //webview.settings.setSupportZoom(true)


       // displayYoutubeVids()

        //webview.settings.pluginState = PluginState.ON
        //webview.webChromeClient = WebChromeClient()



//        retrofit=Retrofit.Builder().baseUrl("https://api.themoviedb.org/3/")
//            .addConverterFactory(GsonConverterFactory.create()).build()
//        val interfaceImpl=retrofit?.create(WebServices::class.java)
//        //
 //       val retroCall=interfaceImpl?.getTrendingMovies("50af916e00b881ab41b2c9bde4a47218")
//        val callresu=retroCall?.enqueue(object :Callback<TMDBResponse>{
//            override fun onResponse(call: Call<TMDBResponse>, response: Response<TMDBResponse>) {
//                val data= response.body()?.results
//                moviesAdapter.updateData(data)
//                homefragmentBinding.firstrv.adapter=moviesAdapter
//                homefragmentBinding.progressbar.visibility=View.GONE
//            }
//
//            override fun onFailure(call: Call<TMDBResponse>, t: Throwable) {
//                val progressDialog:ProgressDialog
//                progressDialog=ProgressDialog(requireContext())
//                progressDialog.setMessage("failed ${t.localizedMessage}")
//                Log.e("Failure","Failed ${t.localizedMessage}")
//                progressDialog.show()
//            }
//
//        })
        initializeDefaultMovie()
        getMovies()
        getTopRatedMovies()
        bindMovieOnClickListener()

        homefragmentBinding.playerIcon.setOnClickListener({
            if(currentChosenMovie==null){
                val chosenmovie=initializeDefaultMovie()
                val intent=Intent(requireContext(),MovieDetailsActivity.getInstance2(chosenmovie)::class.java)
                startActivity(intent)
            }
            else{

                val intent=Intent(requireContext(),MovieDetailsActivity.getInstance(currentChosenMovie!!)::class.java)
                startActivity(intent)
            }



        })
        moviesAdapter.showToast=object :MoviesAdapter.ShowToast{
            override fun showToast() {
                Toast.makeText(requireContext(),"Movie is already added to the watchlist",Toast.LENGTH_LONG).show()
            }
        }
        scdmoviesAdapter.showToast=object :MoviesAdapter.ShowToast{
            override fun showToast() {
                Toast.makeText(requireContext(),"Movie is already added to the watchlist",Toast.LENGTH_LONG).show()
            }
        }

        moviesAdapter.onAddMovieClickListener=object :MoviesAdapter.OnAddMovieClickListener{
            override fun OnAddMovieClick(movie: ResultsItem, position: Int) {
                CreateMovieWatchListDataClass(movie)
                //RoomClass.getInstance(requireContext()).MovieDAO().addToFavourits(mo)
            }
        }
        scdmoviesAdapter.onAddMovieClickListener=object :MoviesAdapter.OnAddMovieClickListener{
            override fun OnAddMovieClick(movie: ResultsItem, position: Int) {
                CreateMovieWatchListDataClass(movie)
                //RoomClass.getInstance(requireContext()).MovieDAO().addToFavourits(mo)
            }
        }
        //android.resource://${activity?.packageName}/${R.raw.video}
        moviesAdapter.onRemoveMovieClickListener=object :MoviesAdapter.OnRemoveMovieClickListener{
            override fun OnRemoveClickL(movie: ResultsItem, position: Int) {
               // RemoveFromDB(movie)
                Toast.makeText(requireContext(),"Movie already Added To WatchList",Toast.LENGTH_LONG).show()
            }
        }
    }


    fun RemoveFromDB(movie: ResultsItem){
        val moviee=WatchList(
            overview = movie.overview,
            originalTitle = movie.originalTitle,
            title = movie.title,
            posterPath = movie.posterPath,
            releaseDate = movie.releaseDate,
            id = movie.id,
            adult = movie.adult,
            voteCount = movie.voteCount
        )

//        val watchlist= GlobalScope.async {
//            RoomClass.getInstance(requireContext()).MovieDAO().deleteItemFromFav(moviee)
//            Log.d("1","deleted")
//        }
//        val updatedlist=GlobalScope.async {
//             val n=watchlist.await()
//            if(n == watchlist.await()){
//                RoomClass.getInstance(requireContext()).MovieDAO().showAllFromFavourits()
//            }
//        }
//       val d= GlobalScope.launch {  if (updatedlist.isCompleted ){
//            WatchListProvider.watchlist=RoomClass.getInstance(requireContext()).MovieDAO().showAllFromFavourits()
//        } }
//
//        if(d.isCompleted){WatchListVM.movieslivedata.value=WatchListProvider.watchlist}
//
//        Toast.makeText(requireContext(),"Movie with name ${moviee.title} is removed from favourits",Toast.LENGTH_LONG).show()

    }


    fun checkIfMovieAlreadyExcist(movie: ResultsItem):Boolean {
        GlobalScope.launch(Dispatchers.IO) {
            val favMovies =
                RoomClass.getInstance(requireContext()).MovieDAO().showAllFromFavourits()
            GenreListProvider.FavMovies=favMovies
            for (i in 0..favMovies.size - 1) {
                if (movie.id == favMovies[i].id) {
                    GenreListProvider.MovieExcistsInFavs = 1
                }
            }
        }
        if (GenreListProvider.MovieExcistsInFavs==1){
            return true
        }else{
            return false
        }

    }
    fun CreateMovieWatchListDataClass(movie:ResultsItem):WatchList{
        val moviee=WatchList(
            overview = movie.overview,
            originalTitle = movie.originalTitle,
            title = movie.title,
            posterPath = movie.posterPath,
            releaseDate = movie.releaseDate,
            id = movie.id,
            adult = movie.adult,
            voteCount = movie.voteCount
        )
        GlobalScope.launch(Dispatchers.IO) {
            RoomClass.getInstance(requireContext()).MovieDAO().addToFavourits(moviee)
        }
        GlobalScope.launch(Dispatchers.IO) {
            WatchListProvider.watchlist=RoomClass.getInstance(requireContext()).MovieDAO().showAllFromFavourits()
        }
        //WatchListVM.movieslivedata.value=WatchListProvider.watchlist
        WatchListVM.getWatchListMoviesFromDB(requireContext())

        return moviee
    }
    fun getMovies(){
        val callresult= ApiManager.getServices().getTrendingMovies(Constants.apiKey)
            .enqueue(object :Callback<TMDBResponse>{
                override fun onResponse(
                    call: Call<TMDBResponse>,
                    response: Response<TMDBResponse>
                ) {
                    val data= response.body()?.results
                    moviesAdapter.updateData(data)
                    homefragmentBinding.firstrv.adapter=moviesAdapter
                    homefragmentBinding.progressbar1.visibility=View.GONE
                }

                override fun onFailure(call: Call<TMDBResponse>, t: Throwable) {
                    val progressDialog:ProgressDialog
                    progressDialog=ProgressDialog(requireContext())
                    progressDialog.setMessage("failed ${t.localizedMessage}")
                    Log.e("Failure","Failed ${t.localizedMessage}")
                    progressDialog.show()
                }

            })
    }
    fun getTopRatedMovies(){
        val callresult= ApiManager.getServices().TopRatedMovies(Constants.apiKey)
            .enqueue(object :Callback<TMDBResponse>{
                override fun onResponse(
                    call: Call<TMDBResponse>,
                    response: Response<TMDBResponse>
                ) {
                    val data= response.body()?.results
                    scdmoviesAdapter.updateData(data)
                    homefragmentBinding.scndrv.adapter=scdmoviesAdapter
                    homefragmentBinding.progressbar2.visibility=View.GONE
                }

                override fun onFailure(call: Call<TMDBResponse>, t: Throwable) {
                    val progressDialog:ProgressDialog
                    progressDialog=ProgressDialog(requireContext())
                    progressDialog.setMessage("failed ${t.localizedMessage}")
                    Log.e("Failure","Failed ${t.localizedMessage}")
                    progressDialog.show()
                }

            })

    }



    /*fun getMovies(){
        val callresult=ApiManager.getServices().getTrendingMovies(Constants.apiKey)?.enqueue(object :Callback<MOviesResponse>{
            override fun onResponse(
                call: Call<MOviesResponse>,
                response: Response<MOviesResponse>
            ) {
                val data= response.body()?.results
                moviesAdapter.updateData(data)
                homefragmentBinding.firstrv.adapter=moviesAdapter
                homefragmentBinding.progressbar.visibility=View.GONE

            }

            override fun onFailure(call: Call<MOviesResponse>, t: Throwable) {
                val progressDialog:ProgressDialog
                progressDialog=ProgressDialog(requireContext())
                progressDialog.setMessage("success ${t.localizedMessage}")
                progressDialog.show()
            }

        })

    }*/


    fun bindMovieOnClickListener(){
        moviesAdapter.onMovieClickListener=object :MoviesAdapter.OnMovieClickListener{
            override fun OnMovieClicked(movie: ResultsItem, position: Int) {
                Glide.with(this@HomeFragment).load("https://image.tmdb.org/t/p/original/" + movie.posterPath).into(homefragmentBinding.movieposter)
                homefragmentBinding.movieNameTxt.text=movie.title
                homefragmentBinding.movieDescriptionTxt.text=movie.releaseDate
                currentChosenMovie=movie
            }
        }
        scdmoviesAdapter.onMovieClickListener=object :MoviesAdapter.OnMovieClickListener{
            override fun OnMovieClicked(movie: ResultsItem, position: Int) {
                Glide.with(this@HomeFragment).load("https://image.tmdb.org/t/p/original/" + movie.posterPath).into(homefragmentBinding.movieposter)
                homefragmentBinding.movieNameTxt.text=movie.title
                homefragmentBinding.movieDescriptionTxt.text=movie.releaseDate
                currentChosenMovie=movie
            }

        }
    }

    fun initializeDefaultMovie():ChosenMovie{
        chosenMovieByDefault= ChosenMovie(
            posterPath  = "https://image.tmdb.org/t/p/original/3bhkrj58Vtu7enYsRolD1fZdja1.jpg",
            id = 238,
            title = "The Godfather",
            overview  = "Spanning the years 1945 to 1955, a chronicle of the fictional Italian-American Corleone crime family. When organized crime family patriarch, Vito Corleone barely survives an attempt on his life, his youngest son, Michael steps in to take care of the would-be killers, launching a campaign of bloody revenge",
            releaseDate =  "1972-03-14",
            voteAverage = "8.71"
        )
        return chosenMovieByDefault
    }



}