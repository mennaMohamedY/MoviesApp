package com.example.moviesapplication.searchfragment

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.moviesapplication.Constants
import com.example.moviesapplication.R
import com.example.moviesapplication.apimanager.ApiManager
import com.example.moviesapplication.browsefragment.GenreListProvider
import com.example.moviesapplication.browsefragment.OnSelectedGenreAdapter
import com.example.moviesapplication.databinding.FragmentSearchBinding
import com.example.moviesapplication.homefragment.MoviesAdapter
import com.example.moviesapplication.module.MovieGenresItem
import com.example.moviesapplication.module.ResultsItem
import com.example.moviesapplication.module.TMDBResponse
import com.example.moviesapplication.moviedetails.MovieDetailsActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


class SearchFragment : Fragment() {
    lateinit var searchBinding:FragmentSearchBinding
    lateinit var searchVM:SearchViewModel
    var searchAdapter=OnSelectedGenreAdapter(mutableListOf())
    var pagesadapter=PagesAdapter(1)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        searchVM=ViewModelProvider(this).get(SearchViewModel::class.java)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_search, container, false)
        searchBinding=DataBindingUtil.inflate(inflater,R.layout.fragment_search,container,false)
        return searchBinding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getMoviesByID(1)
        pagesadapter.onPageNumClickListener= object :PagesAdapter.OnPageNumClickListener{
            override fun OnPageNumClick(position: Int) {
                getMoviesByID(position)
            }
        }
        searchBinding.search.setOnQueryTextListener(object:androidx.appcompat.widget.SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                //implement later
                return false
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                filterList(newText!!)
                return true
            }
        })
        searchAdapter.onMovieClickListener=object :OnSelectedGenreAdapter.OnMovieClickListener{
            override fun OnMovieClicked(movie: ResultsItem, position: Int) {
                val intent= Intent(requireContext(),MovieDetailsActivity.getInstance(movie)::class.java)
                startActivity(intent)
            }

        }
    }

    fun filterList(Query:String){
        if (Query !=null){

            val filteredList= mutableListOf<ResultsItem?>()
            for (i in PagesNumProvider.moviesList!!){
                if(i?.title?.toLowerCase(Locale.ROOT)!!.contains(Query)){
                    filteredList.add(i)
                }
            }
            if(filteredList == null){
                searchBinding.nomatchTxt.visibility=View.VISIBLE
            }else{
                searchAdapter.updateData(filteredList)
                searchBinding.moviesGenreRv.adapter=searchAdapter
            }
        }
    }


    fun getMoviesByID(pageNum:Int){
        var numm:Int
        if (pageNum==0){
            numm =1
        }else{
            numm = pageNum
        }
        lifecycleScope.launchWhenCreated {
            try {
                val response=ApiManager.getServices().getPopularMovies(Constants.apiKey,numm)
                searchAdapter.updateData(response.results as MutableList<ResultsItem?>?)
               // searchAdapter.updateData(response.body()?.results as MutableList<ResultsItem?>?)
                searchBinding.moviesGenreRv.adapter=searchAdapter
                val num=response.totalPages
                //val num=response.body()?.totalPages
                PagesNumProvider.pagesNum=num
                pagesadapter.updateNum(PagesNumProvider.pagesNum)
                searchBinding.pagesRV.adapter=pagesadapter
                PagesNumProvider.moviesList=response.results
                //PagesNumProvider.moviesList=response.body()?.results

            }catch (e:Exception){
                val progressDialog: ProgressDialog
                progressDialog= ProgressDialog(requireContext())
                progressDialog.setMessage("failed ${e.localizedMessage}")
                Log.e("Failure","Failed ${e.localizedMessage}")
                progressDialog.show()

            }
        }

//        ApiManager.getServices().getPopularMovies(Constants.apiKey,numm)
//            .enqueue(object :Callback<TMDBResponse>{
//                override fun onResponse(
//                    call: Call<TMDBResponse>,
//                    response: Response<TMDBResponse>
//                ) {
//                    searchAdapter.updateData(response.body()?.results as MutableList<ResultsItem?>?)
//                    searchBinding.moviesGenreRv.adapter=searchAdapter
//                    val num=response.body()?.totalPages
//                    PagesNumProvider.pagesNum=num
//                    pagesadapter.updateNum(PagesNumProvider.pagesNum)
//                    searchBinding.pagesRV.adapter=pagesadapter
//                    PagesNumProvider.moviesList=response.body()?.results
//
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



}