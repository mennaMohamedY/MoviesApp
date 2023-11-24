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
import com.example.moviesapplication.moviedetails.MoviesDetailsNavigator
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


class SearchFragment : Fragment(),MoviesDetailsNavigator {
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
        searchBinding=DataBindingUtil.inflate(inflater,R.layout.fragment_search,container,false)
        return searchBinding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        searchVM.nav=this
        searchVM.getMoviesByID(1)
        pagesadapter.onPageNumClickListener= object :PagesAdapter.OnPageNumClickListener{
            override fun OnPageNumClick(position: Int) {
                searchVM.getMoviesByID(position)
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

        searchBinding.moviesGenreRv.adapter=searchAdapter
        searchBinding.pagesRV.adapter=pagesadapter

        subscribeToLiveData()

    }
    fun subscribeToLiveData(){
        searchVM.moviesDAta.observe(viewLifecycleOwner){
            searchAdapter.updateData(it)
        }
        searchVM.pagesNum.observe(viewLifecycleOwner){
            pagesadapter.updateNum(it)

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

    override fun showProgressDialog(msg: String) {
        val progressDialog:ProgressDialog
        progressDialog=ProgressDialog(requireContext())
        progressDialog.setMessage("failed ${msg}")
        Log.e("Failure","Failed ${msg}")
        progressDialog.show()
    }

    override fun onPlayVideoClick() {
        TODO("Not yet implemented")
    }


}