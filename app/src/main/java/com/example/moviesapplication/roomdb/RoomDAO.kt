package com.example.moviesapplication.roomdb

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface RoomDAO {
    @Insert
    fun addToFavourits(movie:WatchList)

    @Query("SELECT * FROM Movie")
    fun showAllFromFavourits():List<WatchList>

    @Query("DELETE FROM Movie")
    fun deleteAllFromDataBase()

    @Delete
    fun deleteItemFromFav(movie:WatchList)

}