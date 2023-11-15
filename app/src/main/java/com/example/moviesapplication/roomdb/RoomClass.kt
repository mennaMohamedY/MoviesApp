package com.example.moviesapplication.roomdb

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [WatchList::class], version = 1)
abstract class RoomClass :RoomDatabase() {

        abstract fun MovieDAO():RoomDAO
        //singleton
        companion object{
            private var Instance:RoomClass?=null

            fun getInstance(context: Context):RoomClass{
                if(Instance !=null){
                    return Instance as RoomClass
                }
                //if it's called from 3 or more different places then only single operation will be completed after it another will and so on
                synchronized(this){
                    val instance= Room.databaseBuilder(
                        //context.applicationContext beacause it's shared all over the application
                        context.applicationContext,RoomClass::class.java,"App_local_dataClass")
                        .build()
                    Instance=instance
                    return Instance as RoomClass
                }
            }
        }
    }
