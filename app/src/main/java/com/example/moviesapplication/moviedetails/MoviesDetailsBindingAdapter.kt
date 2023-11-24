package com.example.moviesapplication.moviedetails

import android.webkit.WebChromeClient
import android.webkit.WebView
import androidx.databinding.BindingAdapter
import com.example.moviesapplication.apimanager.KeyProvider

@BindingAdapter("PlayTrailer")
fun PlayMovieTrailer(wview:WebView,key:String){
    val frm =
        "<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/wA6iB3OZDus\" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share\" allowfullscreen></iframe>"
    wview.loadData(frm, "text/html", "utf-8")
    wview.settings.javaScriptEnabled = true
    wview.webChromeClient = WebChromeClient()
}