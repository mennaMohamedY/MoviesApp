package com.example.moviesapplication.watchlist

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
import com.example.moviesapplication.R
import com.example.moviesapplication.browsefragment.GenreListProvider
import com.example.moviesapplication.databinding.FragmentWatchListBinding
import com.example.moviesapplication.module.MovieGenresItem
import com.example.moviesapplication.module.ResultsItem
import com.example.moviesapplication.moviedetails.MovieDetailsActivity
import com.example.moviesapplication.moviedetails.MoviesDetailsNavigator
import com.example.moviesapplication.roomdb.RoomClass
import com.example.moviesapplication.roomdb.WatchList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*


class WatchListFragment : Fragment(),MoviesDetailsNavigator {
    lateinit var WatchlistBinding:FragmentWatchListBinding
    lateinit var WatchListVM:WatchListViewModel
    var watchlistadapter=WatchListAdapter(mutableListOf())




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WatchListVM=ViewModelProvider(this).get(WatchListViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        WatchlistBinding=DataBindingUtil.inflate(inflater,R.layout.fragment_watch_list,container,false)
        return WatchlistBinding.root
        WatchlistBinding.moviesGenreRv.adapter=watchlistadapter
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //WatchlistBinding.empty.visibility=View.INVISIBLE
        WatchlistBinding.moviesGenreRv.adapter=watchlistadapter
        WatchListVM.getWatchListMoviesFromDB(requireContext())
        SubScribeToLiveData()
        //WatchlistBinding.moviesGenreRv.adapter=watchlistadapter
        //updateMovieslist()
        WatchlistBinding.search.setOnQueryTextListener(object:androidx.appcompat.widget.SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                //implement later
                return false
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                filterList(newText!!)
                return true
            }
        })
        watchlistadapter.onPosterClickListener=object :WatchListAdapter.OnPosterClickListener{
            override fun OnPosterClick(poster: WatchList, position: Int) {
                val intent= Intent(requireContext(),MovieDetailsActivity.getInstance3(poster)::class.java)
                startActivity(intent)
            }
        }

        watchlistadapter.onDeletClickListener=object :WatchListAdapter.OnDeleteCLickListener{
            override fun OnDeleteClick(poster: WatchList, postion: Int) {
                DeleteFromWatchList(poster)
            }
        }
    }

    override fun onStart() {
        super.onStart()
        SubScribeToLiveData()
    }

    override fun onResume() {
        super.onResume()
        SubScribeToLiveData()
    }
    fun SubScribeToLiveData(){
        //WatchListVM.getWatchListMoviesFromDB(requireContext())
        WatchListVM.movieslivedata.observe(viewLifecycleOwner){
            // watchlistadapter.updateList(null)
            //watchlistadapter.updateList(it)
            if (it.isNullOrEmpty()){
                WatchlistBinding.empty.visibility=View.VISIBLE
                watchlistadapter.updateList(it)
                WatchlistBinding.moviesGenreRv.adapter=watchlistadapter

            }
            else{
                WatchlistBinding.empty.visibility=View.INVISIBLE
                watchlistadapter.updateList(it)
                WatchlistBinding.moviesGenreRv.adapter=watchlistadapter

            }
            //watchlistadapter.notifyDataSetChanged()
            //watchlistadapter.updateList(WatchListProvider.watchlist)
            //WatchlistBinding.moviesGenreRv.adapter=watchlistadapter
            //watchlistadapter.notifyDataSetChanged()
        }
    }


//    fun SubScribeToLiveData(){
//        WatchListVM.movieslivedata.observe(viewLifecycleOwner){
//            watchlistadapter.updateList(it)
//            WatchlistBinding.empty.visibility=View.INVISIBLE
//
//
//            if (it.isNullOrEmpty()){
//                WatchlistBinding.empty.visibility=View.VISIBLE
//                WatchlistBinding.moviesGenreRv.adapter=watchlistadapter
//            }
//            else{
//                WatchlistBinding.empty.visibility=View.INVISIBLE
//                WatchlistBinding.moviesGenreRv.adapter=watchlistadapter
//            }
//        }
//    }
    fun DeleteFromWatchList(poster:WatchList){
        GlobalScope.launch(Dispatchers.IO) {
             RoomClass.getInstance(requireContext()).MovieDAO().deleteItemFromFav(poster)
        }
        WatchListVM.getWatchListMoviesFromDB(requireContext())
        SubScribeToLiveData()
    }

    fun filterList(Query:String){
        if (Query !=null){
            val filteredList= mutableListOf<WatchList>()
            for (i in WatchListProvider.watchlist!!){
                if(i?.title?.toLowerCase(Locale.ROOT)!!.contains(Query)){
                    filteredList.add(i)
                }
            }
            if(filteredList == null){
                WatchlistBinding.nomatchTxt.visibility=View.VISIBLE
            }else{
                watchlistadapter.updateList(filteredList)
                WatchlistBinding.moviesGenreRv.adapter=watchlistadapter
            }
        }

    }

    override fun showProgressDialog(msg: String) {
        val progressDialog: ProgressDialog
        progressDialog= ProgressDialog(requireContext())
        progressDialog.setMessage("failed ${msg}")
        Log.e("Failure","Failed ${msg}")
        progressDialog.show()
    }

    override fun onPlayVideoClick() {
        TODO("Not yet implemented")
    }

}