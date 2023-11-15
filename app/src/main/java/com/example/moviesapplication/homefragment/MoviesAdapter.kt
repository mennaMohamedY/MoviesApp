package com.example.moviesapplication.homefragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.example.moviesapplication.R
import com.example.moviesapplication.databinding.SinglervmovieDesignBinding
import com.example.moviesapplication.module.ResultsItem

class MoviesAdapter(var MoviesData:List<ResultsItem?>?) :Adapter<MoviesAdapter.MyMoviesHolder>(){
    var onMovieClickListener:OnMovieClickListener?=null
    var onAddMovieClickListener:OnAddMovieClickListener?=null

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

        holder.itemBinding.addMovieBookmark.setOnClickListener({

            with(holder){
                with(itemBinding){
                    addMovieBookmark.visibility=View.INVISIBLE
                    movieAddedBookmark.visibility=View.VISIBLE
                }
            }
            onAddMovieClickListener?.OnAddMovieClick(currentItem!!,position)
        })
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