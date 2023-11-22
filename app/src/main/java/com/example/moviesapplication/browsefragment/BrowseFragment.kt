package com.example.moviesapplication.browsefragment

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView.OnQueryTextListener
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.lifecycle.lifecycleScope
import com.example.moviesapplication.Constants
import com.example.moviesapplication.R
import com.example.moviesapplication.apimanager.ApiManager
import com.example.moviesapplication.databinding.FragmentBrowseBinding
import com.example.moviesapplication.homefragment.MoviesAdapter
import com.example.moviesapplication.module.MovieGenresItem
import com.example.moviesapplication.module.MoviesCategoriesResponse
import com.example.moviesapplication.module.ResultsItem
import com.example.moviesapplication.module.TMDBResponse
import com.example.moviesapplication.moviedetails.MovieDetailsActivity
import com.example.moviesapplication.searchfragment.SearchFragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


class BrowseFragment : Fragment() {
    lateinit var BrowseBinding:FragmentBrowseBinding
    lateinit var BrowseVM:BrowseViewModel
    var BrowseAdapter=BrowseAdapter(mutableListOf())
    //var onClickonTheClickListener:OnClickonTheClickListener?=null
    var scdBrowseAdapter=MoviesAdapter(listOf())


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        BrowseVM=ViewModelProvider(this).get(BrowseViewModel::class.java)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_browse, container, false)
        BrowseBinding=DataBindingUtil.inflate(inflater,R.layout.fragment_browse,container,false)
        return BrowseBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //call api and bind movie genre adapter
        getMoviesGenre()

        BrowseBinding.search.setOnQueryTextListener(object:androidx.appcompat.widget.SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                //implement later
                return false
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                filterList(newText!!)
                return true
            }
        })

        BrowseAdapter.onGenreClickListener=object :BrowseAdapter.OnGenreClickListener{
            override fun OnGenreClick(genre: MovieGenresItem, position: Int) {
                //onClickonTheClickListener?.OnClickonClick(genre,position)
                Toast.makeText(requireContext(),"Click Happened",Toast.LENGTH_LONG).show()
                Log.e("click","Click Happened")
                getMoviesByID(genre)
            }
        }

        scdBrowseAdapter.onMovieClickListener=object :MoviesAdapter.OnMovieClickListener{
            override fun OnMovieClicked(movie: ResultsItem, position: Int) {
                val intent=Intent(requireContext(),MovieDetailsActivity.getInstance(movie)::class.java)
                startActivity(intent)
            }

        }

    }
//    interface OnClickonTheClickListener{
//        fun OnClickonClick(genre:MovieGenresItem,position:Int)
//    }

    fun getMoviesByID(genre:MovieGenresItem){
        lifecycleScope.launchWhenCreated {
            try {
                val response= ApiManager.getServices().getMoviesByGenreID(Constants.apiKey, genre.id!!)
                scdBrowseAdapter.updateData(response.results)
                BrowseBinding.moviesGenreRv.adapter=scdBrowseAdapter
                GenreListProvider.whichAdapter=2
                GenreListProvider.SelectedGenreMovies=response.results
            }catch (e:Exception){
                val progressDialog: ProgressDialog
                progressDialog= ProgressDialog(requireContext())
                progressDialog.setMessage("failed ${e.localizedMessage}")
                Log.e("Failure","Failed ${e.localizedMessage}")
                progressDialog.show()
            }
        }
//        ApiManager.getServices().getMoviesByGenreID(Constants.apiKey, genre.id!!)
//            .enqueue(object :Callback<TMDBResponse>{
//                override fun onResponse(
//                    call: Call<TMDBResponse>,
//                    response: Response<TMDBResponse>
//                ) {
//                    scdBrowseAdapter.updateData(response.body()?.results)
//                    BrowseBinding.moviesGenreRv.adapter=scdBrowseAdapter
//                    GenreListProvider.whichAdapter=2
//                    GenreListProvider.SelectedGenreMovies=response.body()?.results
//                }
//
//                override fun onFailure(call: Call<TMDBResponse>, t: Throwable) {
//                    val progressDialog: ProgressDialog
//                    progressDialog= ProgressDialog(requireContext())
//                    progressDialog.setMessage("failed ${t.localizedMessage}")
//                    Log.e("Failure","Failed ${t.localizedMessage}")
//                    progressDialog.show()
//                }
//
//            })

    }


    fun pushFragment(fragment:Fragment){

    }
    fun filterList(Query:String){
        if (Query !=null){
            //incase you are searching the movies genre
            if(GenreListProvider.whichAdapter == 1){
                val filteredList= mutableListOf<MovieGenresItem?>()
                for(i in GenreListProvider.GenreList!!){
                    if (i?.name?.toLowerCase(Locale.ROOT)!!.contains(Query)){
                        filteredList.add(i)
                    }
                }
                if (filteredList == null){
                    BrowseBinding.nomatchTxt.visibility=View.VISIBLE
                }else{
                    BrowseAdapter.UpdateList(filteredList)
                    BrowseBinding.moviesGenreRv.adapter=BrowseAdapter
                    GenreListProvider.whichAdapter=1
                }
            }

            //incase we are searching the genreMovies List
            else if(GenreListProvider.whichAdapter ==2){

                val filteredList= mutableListOf<ResultsItem>()
                for (i in GenreListProvider.SelectedGenreMovies!!){
                    if(i?.title?.toLowerCase(Locale.ROOT)!!.contains(Query)){
                        filteredList.add(i)
                    }
                }
                if(filteredList == null){
                    BrowseBinding.nomatchTxt.visibility=View.VISIBLE
                }else{
                    scdBrowseAdapter.updateData(filteredList)
                    BrowseBinding.moviesGenreRv.adapter=scdBrowseAdapter
                    GenreListProvider.whichAdapter=2
                }
            }
        }
    }


    fun getMoviesGenre(){
        lifecycleScope.launchWhenCreated {
            try {
                val response=ApiManager.getServices().getMoviesGenre(Constants.apiKey)
                BrowseAdapter.UpdateList(response.genres as MutableList<MovieGenresItem?>?)
                GenreListProvider.GenreList=response.genres
                BrowseBinding.moviesGenreRv.adapter=BrowseAdapter
                GenreListProvider.whichAdapter=1
            }catch (e:Exception){
                val progressDialog: ProgressDialog
                progressDialog= ProgressDialog(requireContext())
                progressDialog.setMessage("failed ${e.localizedMessage}")
                Log.e("Failure","Failed ${e.localizedMessage}")
                progressDialog.show()
            }
        }
//        ApiManager.getServices().getMoviesGenre(Constants.apiKey)
//            .enqueue(object :Callback<MoviesCategoriesResponse>{
//                override fun onResponse(
//                    call: Call<MoviesCategoriesResponse>,
//                    response: Response<MoviesCategoriesResponse>
//                ) {
//                    BrowseAdapter.UpdateList(response.body()?.genres as MutableList<MovieGenresItem?>?)
//                    GenreListProvider.GenreList=response.body()?.genres
//                    BrowseBinding.moviesGenreRv.adapter=BrowseAdapter
//                    GenreListProvider.whichAdapter=1
//                }
//
//                override fun onFailure(call: Call<MoviesCategoriesResponse>, t: Throwable) {
//                    val progressDialog: ProgressDialog
//                    progressDialog= ProgressDialog(requireContext())
//                    progressDialog.setMessage("failed ${t.localizedMessage}")
//                    Log.e("Failure","Failed ${t.localizedMessage}")
//                    progressDialog.show()
//                }
//
//            })
    }



}