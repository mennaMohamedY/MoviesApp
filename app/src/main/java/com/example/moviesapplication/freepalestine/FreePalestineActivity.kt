package com.example.moviesapplication.freepalestine

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.example.moviesapplication.databinding.ActivityFreePalestineBinding
import com.example.moviesapplication.main.MainActivity

class FreePalestineActivity : AppCompatActivity() {
    lateinit var freepalestineBinding: ActivityFreePalestineBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       // setContentView(R.layout.activity_free_palestine)
        freepalestineBinding=ActivityFreePalestineBinding.inflate(layoutInflater)
        setContentView(freepalestineBinding.root)

        Handler(Looper.getMainLooper()).postDelayed({
            //checkUser()
            goToMainActivity()
            // goToSignInActivity()

        },1000)


    }
    fun goToMainActivity(){
        val intent=Intent(this,MainActivity::class.java)
        startActivity(intent)
    }
}