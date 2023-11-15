package com.example.moviesapplication.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.moviesapplication.R
import com.example.moviesapplication.browsefragment.BrowseFragment
import com.example.moviesapplication.databinding.ActivityMainBinding
import com.example.moviesapplication.homefragment.HomeFragment
import com.example.moviesapplication.module.MovieGenresItem
import com.example.moviesapplication.searchfragment.SearchFragment
import com.example.moviesapplication.watchlist.WatchListFragment

class MainActivity : AppCompatActivity() {
    lateinit var mainBinding:ActivityMainBinding
    lateinit var mainViewModel:MainViewModel
    lateinit var browseFragment:BrowseFragment
   // lateinit var searchFragment:SearchFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_main)
        mainBinding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)
        browseFragment= BrowseFragment()
       // searchFragment=SearchFragment()

//        browseFragment?.onClickonTheClickListener=object :BrowseFragment.OnClickonTheClickListener{
//            override fun OnClickonClick(genre: MovieGenresItem, position: Int) {
//                pushFragment(SearchFragment.getInstance(genre))
//            }
//
//        }

        mainViewModel=ViewModelProvider(this).get(MainViewModel::class.java)
        mainBinding.bottomNavigationView.selectedItemId = R.id.Home
        pushFragment(HomeFragment())


        mainBinding.bottomNavigationView.setOnItemSelectedListener {
            if(it.itemId == R.id.Home){
                pushFragment(HomeFragment())
            }else if(it.itemId == R.id.Search){
                pushFragment(SearchFragment())
            }else if(it.itemId == R.id.Browse){
                pushFragment(BrowseFragment())
            }else{
                pushFragment(WatchListFragment())
            }
            return@setOnItemSelectedListener true
        }
    }

    fun pushFragment(fragment:Fragment){
        supportFragmentManager.beginTransaction().replace(mainBinding.framlayout.id,fragment).commit()
    }
}