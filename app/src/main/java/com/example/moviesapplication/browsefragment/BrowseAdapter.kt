package com.example.moviesapplication.browsefragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.moviesapplication.databinding.SinglecategoryDesignBinding
import com.example.moviesapplication.module.MovieGenresItem

class BrowseAdapter(var GenreList:MutableList<MovieGenresItem?>?) :Adapter<BrowseAdapter.BrowseMovieViewHolder>(){
    var onGenreClickListener:OnGenreClickListener?=null
    class BrowseMovieViewHolder(val singleGenreDesign:SinglecategoryDesignBinding):ViewHolder(singleGenreDesign.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BrowseMovieViewHolder {
        val singleGenreDesignBinding=SinglecategoryDesignBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return BrowseMovieViewHolder(singleGenreDesignBinding)
    }

    override fun onBindViewHolder(holder: BrowseMovieViewHolder, position: Int) {
        val currentItem=GenreList?.get(position)
        holder.singleGenreDesign.movieGenre.text=currentItem?.name
        holder.singleGenreDesign.movieGenre.setOnClickListener({
            onGenreClickListener?.OnGenreClick(currentItem!!,position)
        })
    }

    override fun getItemCount(): Int {
        return GenreList?.size?:0
    }

    fun UpdateList(genreList: MutableList<MovieGenresItem?>?){
        this.GenreList=genreList
        notifyDataSetChanged()
    }
    interface OnGenreClickListener{
        fun OnGenreClick(genre:MovieGenresItem,position: Int)
    }

}