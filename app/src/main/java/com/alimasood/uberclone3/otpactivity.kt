package com.alimasood.uberclone3

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

@Suppress("DEPRECATION")
class otpactivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    lateinit var verifybutton: Button
    lateinit var otptext: EditText

    private  lateinit var otp:String
    private lateinit var  token: PhoneAuthProvider.ForceResendingToken
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_otpactivity)




        otp=intent.getStringExtra("OTP").toString()
       // token=intent.getParcelableExtra("resendtoken")!!
        auth=Firebase.auth

        otptext=findViewById(R.id.otptext)
        verifybutton=findViewById(R.id.verify)

        verifybutton.setOnClickListener{
            val credential: PhoneAuthCredential = PhoneAuthProvider.getCredential(
                otp, otptext.text.toString()
            )

            signInWithPhoneAuthCredential(credential)
        }




    }



    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Toast.makeText(this,"access granted",Toast.LENGTH_LONG).show()


                    val user = task.result?.user
                } else {
                    // Sign in failed, display a message and update the UI
                    Toast.makeText(this,"access stopped",Toast.LENGTH_LONG).show()
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        Toast.makeText(this,"access stopped",Toast.LENGTH_LONG).show()
                    }
                    // Update UI
                }
            }
    }
}