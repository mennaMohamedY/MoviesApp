package com.example.moviesapplication.roomdb

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "Movie")
data class WatchList(
    @PrimaryKey(autoGenerate = true)
    val roomid:Int?=null,
    val overview: String? = null,
    val originalTitle: String? = null,
    val title: String? = null,
    val posterPath: String? = null,
    val releaseDate: String? = null,
    val id: Int? = null,
    val adult: Boolean? = null,
    val voteCount: Int? = null


)
