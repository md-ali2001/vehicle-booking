package com.alimasood.uberclone3

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.google.firebase.auth.FirebaseAuth

class afterlogin : AppCompatActivity() {

    private lateinit var signout: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_afterlogin)

        signout=findViewById(R.id.button)





    }
}