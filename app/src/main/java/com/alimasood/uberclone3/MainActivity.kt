package com.alimasood.uberclone3


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    var phonesignin: Button? = null
    var phonenumber: EditText? = null
    var googlesignin: Button? = null

    private var storedVerificationId: String? = ""
    private lateinit var resendToken: PhoneAuthProvider.ForceResendingToken
    private lateinit var callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    private val REQ_ONE_TAP = 2  // Can be any integer unique to the Activity
    private var showOneTapUI = true




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        phonesignin = findViewById(R.id.phonesignin)
        auth = Firebase.auth
        phonenumber=findViewById(R.id.phonenumber)
        googlesignin=findViewById(R.id.googlesignin)

        val logoutornot=intent.getStringExtra("log").toString()
        val phoneloggedinornot=intent.getStringExtra("ph").toString()
        if(logoutornot=="yes")
        {
            auth.signOut()

            // Google sign out

            // Google sign out

            if(phoneloggedinornot=="no")
            {googleSignInClient.signOut().addOnCompleteListener(this,
                OnCompleteListener<Void?> {  })

            }
            else{


            FirebaseAuth.getInstance().signOut()

        }}
        else{

        }



        // ...


        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        if(logoutornot=="yes")
        {
            auth.signOut()

            // Google sign out

            // Google sign out
            googleSignInClient.signOut().addOnCompleteListener(this,
                OnCompleteListener<Void?> { updateUI(null) })
            FirebaseAuth.getInstance().signOut()

            Toast.makeText(this@MainActivity,"successfully logged out from all",Toast.LENGTH_SHORT).show()



        }
        else{

        }


        callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            override fun onVerificationCompleted(credential: PhoneAuthCredential) {

signInWithPhoneAuthCredential(credential)

            }

            override fun onVerificationFailed(e: FirebaseException) {

                Log.w(TAG, "onVerificationFailed", e)

                if (e is FirebaseAuthInvalidCredentialsException) {

                } else if (e is FirebaseTooManyRequestsException) {

                }

                // Show a message and update the UI
            }

            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken
            ) {

                Log.d(TAG, "onCodeSent:$verificationId")

                // Save verification ID and resending token so we can use them later

                storedVerificationId = verificationId
                resendToken = token

                val intent=Intent(this@MainActivity,otpactivity::class.java)
                intent.putExtra("OTP",storedVerificationId)
               // intent.putExtra("token", resendToken)
                startActivity(intent)
                Log.d("otp", storedVerificationId!!)

            }
        }

    }


    override fun onStart() {
        super.onStart()

        val currentUser = auth.currentUser
        Toast.makeText(this@MainActivity,"success",Toast.LENGTH_SHORT).show()

    }
    fun verifysignin(view: android.view.View) {

        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phonenumber!!.text.toString())       // Phone number to verify
            .setTimeout(60L, java.util.concurrent.TimeUnit.SECONDS) // Timeout and unit
            .setActivity(this)                 // Activity (for callback binding)
            .setCallbacks(callbacks)          // OnVerificationStateChangedCallbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
        // [END start_phone_auth]
    }

    fun verifyPhoneNumberWithCode(verificationId: String?, code: String) {
        // [START verify_with_code]
        val credential = PhoneAuthProvider.getCredential(verificationId!!, code)
        // [END verify_with_code]
    }

    // [START resend_verification]
  fun resendVerificationCode(
        phoneNumber: String,
        token: PhoneAuthProvider.ForceResendingToken?
    ) {
        val optionsBuilder = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phonenumber!!.text.toString())
            .setTimeout(60L, java.util.concurrent.TimeUnit.SECONDS)
            .setActivity(this)
            .setCallbacks(callbacks)
            .build()
        PhoneAuthProvider.verifyPhoneNumber(optionsBuilder)

    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithCredential:success")

                    val intent=Intent(this@MainActivity,otpactivity::class.java)
                    intent.putExtra("OTP",storedVerificationId)

                    startActivity(intent)
                    Log.d("otp", storedVerificationId!!)

                    val user = task.result?.user
                } else {
                    // Sign in failed, display a message and update the UI
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        // The verification code entered was invalid
                    }
                    // Update UI
                }
            }
    }


    private fun updateUI(user: FirebaseUser? = auth.currentUser) {

    }

    fun googlesignin(idToken: String) {

        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithCredential:success")
                    val user = auth.currentUser
                    Log.d("info", user.toString())

                    val intent=Intent(this@MainActivity,afterlogin::class.java)
                    intent.putExtra("phoneloggedin","no")

                    startActivity(intent)
                    Log.d("otp", storedVerificationId!!)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    Toast.makeText(this@MainActivity,"success",Toast.LENGTH_SHORT).show()
                    updateUI(null)
                }
            }
    }

    companion object {
        private const val TAG = "PhoneAuthActivity"
        private const val RC_SIGN_IN = 9001
    }




    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)!!
                Log.d("info", account.toString())
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.id)
                firebaseAuthWithGoogle(account.idToken!!)
                val intent=Intent(this@MainActivity,afterlogin::class.java)
                intent.putExtra("phoneloggedin","no")

                startActivity(intent)
                Log.d("otp", storedVerificationId!!)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e)
            }
        }
    }




    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithCredential:success")
                    val user = auth.currentUser
                    Log.d("info", user.toString())
                    Toast.makeText(this@MainActivity,"success",Toast.LENGTH_SHORT).show()

                    val intent=Intent(this@MainActivity,afterlogin::class.java)

                    intent.putExtra("phoneloggedin","no")

                    startActivity(intent)
                    Log.d("otp", storedVerificationId!!)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    Toast.makeText(this@MainActivity,"success",Toast.LENGTH_SHORT).show()
                    updateUI(null)
                }
            }
    }

     fun signIn(view: android.view.View) {

         auth.signOut()

         // Google sign out

         // Google sign out
         googleSignInClient.signOut().addOnCompleteListener(this,
             OnCompleteListener<Void?> { updateUI(null) })
                val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }




}







