package com.alimasood.uberclone3

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.firebase.auth.FirebaseAuth

class afterlogin : AppCompatActivity() {

    private lateinit var signout: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_afterlogin)
        signout=findViewById(R.id.button)


        val phoneloggedinornot=intent.getStringExtra("phoneloggedin")



        signout.setOnClickListener {
            val intent=Intent(this@afterlogin,MainActivity::class.java)
            finish()
            intent.putExtra("log","yes")
            intent.putExtra("ph",phoneloggedinornot)
            // intent.putExtra("token", resendToken)
            startActivity(intent)

        }







    }
}