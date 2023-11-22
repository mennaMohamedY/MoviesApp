package com.example.moviesapplication.homefragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.example.moviesapplication.R
import com.example.moviesapplication.browsefragment.GenreListProvider
import com.example.moviesapplication.databinding.SinglervmovieDesignBinding
import com.example.moviesapplication.module.ResultsItem
import com.example.moviesapplication.watchlist.WatchListProvider

class MoviesAdapter(var MoviesData:List<ResultsItem?>?) :Adapter<MoviesAdapter.MyMoviesHolder>(){
    var onMovieClickListener:OnMovieClickListener?=null
    var onAddMovieClickListener:OnAddMovieClickListener?=null
    var count:Int?=0
    var onRemoveMovieClickListener:OnRemoveMovieClickListener?=null
    var showToast:ShowToast?=null

    fun updateData(moviesDAta: List<ResultsItem?>?){
        MoviesData=moviesDAta
        notifyDataSetChanged()
    }
    class MyMoviesHolder(val itemBinding :SinglervmovieDesignBinding):ViewHolder(itemBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyMoviesHolder {
        val itemBinding=SinglervmovieDesignBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return MyMoviesHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: MyMoviesHolder, position: Int) {
        val currentItem=MoviesData?.get(position)
        //holder.itemBinding.movieImg.setImageResource(R.drawable.m2)
       // https://img.freepik.com/free-photo/painting-mountain-lake-with-mountain-background_188544-9126.jpg
        //"https://api.themoviedb.org/t/p/w500"+currentItem?.posterPath
        //https://image.tmdb.org/t/p/original/bOGkgRGdhrBYJSLpXaxhXVstddV.jpg
        Glide.with(holder.itemView).load("https://image.tmdb.org/t/p/original/" + currentItem?.posterPath).into(holder.itemBinding.movieImg)
        holder.itemBinding.rate.text=currentItem?.voteAverage.toString()
        holder.itemBinding.movieName.text = currentItem?.title
        holder.itemBinding.movieLength.text= currentItem?.releaseDate
        holder.itemBinding.movieImg.setOnClickListener({
            onMovieClickListener?.OnMovieClicked(currentItem!!,position)
        })
        if(checkifInWatchList(currentItem!!)){
            with(holder){
                with(itemBinding){
                    addMovieBookmark.visibility=View.INVISIBLE
                    movieAddedBookmark.visibility=View.VISIBLE
                }
            }
        }else{
            with(holder){
                with(itemBinding){
                    addMovieBookmark.visibility=View.VISIBLE
                    movieAddedBookmark.visibility=View.INVISIBLE
                }
            }
        }

        holder.itemBinding.addMovieBookmark.setOnClickListener({
            count =count!!+1

            //in case i want to save movie in watch list then i'm saving it for the first time so the fun checkifinwatchlist will return false
            if (!checkifInWatchList(currentItem!!)){
                onAddMovieClickListener?.OnAddMovieClick(currentItem!!,position)
                with(holder){
                    with(itemBinding){
                        addMovieBookmark.visibility=View.INVISIBLE
                        movieAddedBookmark.visibility=View.VISIBLE
                    }
                }
            }else{
                showToast?.showToast()
            }
        })
        holder.itemBinding.movieAddedBookmark.setOnClickListener({
            onRemoveMovieClickListener?.OnRemoveClickL(currentItem,position)

            if (checkifInWatchList(currentItem)){
                with(holder){
                    with(itemBinding){
//                        addMovieBookmark.visibility=View.VISIBLE
//                        movieAddedBookmark.visibility=View.INVISIBLE
//                        count =count!!+1
                    }
                }
            }
        })
    }
    interface ShowToast{
        fun showToast()
    }

    fun checkifInWatchList(currentItem:ResultsItem):Boolean{
        var valid=false
        if (WatchListProvider.watchlist.isNullOrEmpty()){
            return valid
        }else{
            for (i in 0..WatchListProvider.watchlist?.size!!-1){
                if (currentItem.id == WatchListProvider.watchlist!![i].id){
                    valid = true
                }
            }
        }
        return valid
    }
    interface OnRemoveMovieClickListener{
        fun OnRemoveClickL(movie: ResultsItem,position: Int)
    }

    interface OnAddMovieClickListener{
        fun OnAddMovieClick(movie: ResultsItem,position: Int)
    }
    interface OnMovieClickListener{
        fun OnMovieClicked(movie:ResultsItem,position: Int)
    }

    override fun getItemCount(): Int {
        return MoviesData?.size?:0
    }


}