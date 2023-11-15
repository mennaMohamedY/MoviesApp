package com.example.moviesapplication.homefragment

import android.view.View
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel

class HomeFragmentViewModel :ViewModel() {
    var MovieImg=ObservableField<Int>()
    var MovieName=ObservableField<String>()
    var MovieDuration=ObservableField<String>()
}