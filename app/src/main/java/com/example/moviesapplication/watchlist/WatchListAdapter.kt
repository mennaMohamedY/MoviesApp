package com.example.moviesapplication.watchlist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.example.moviesapplication.databinding.SinglewatchlistDesignBinding
import com.example.moviesapplication.roomdb.WatchList

class WatchListAdapter (var watchlistDAta:List<WatchList?>?):Adapter<WatchListAdapter.MyWatchListHolder>(){
    var onPosterClickListener:OnPosterClickListener?=null
    var onDeletClickListener:OnDeleteCLickListener?=null

    fun updateList(data:List<WatchList?>?){
        watchlistDAta=data
        notifyDataSetChanged()
    }

    class MyWatchListHolder(val watchListBinding:SinglewatchlistDesignBinding):ViewHolder(watchListBinding.root){
        fun bind(watchlistElement:WatchList){
            watchListBinding.watchList=watchlistElement
            watchListBinding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyWatchListHolder {
        val WatchListBinding=SinglewatchlistDesignBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return MyWatchListHolder(WatchListBinding)
    }

    override fun onBindViewHolder(holder: MyWatchListHolder, position: Int) {
        val currentItem=watchlistDAta?.get(position)
        holder.bind(currentItem!!)


        Glide.with(holder.itemView).load("https://image.tmdb.org/t/p/original/" + currentItem?.posterPath).into(holder.watchListBinding.movieImg)

        holder.watchListBinding.movieImg.setOnClickListener({
            onPosterClickListener?.OnPosterClick(currentItem!!,position)
        })
        holder.watchListBinding.deleteMovie.setOnClickListener({
            onDeletClickListener?.OnDeleteClick(currentItem!!,position)
        })
    }
    interface OnDeleteCLickListener{
        fun OnDeleteClick(poster: WatchList,postion:Int)
    }

    interface OnPosterClickListener{
        fun OnPosterClick(poster:WatchList,position: Int)
    }

    override fun getItemCount(): Int {
        return watchlistDAta?.size?:0
    }
}