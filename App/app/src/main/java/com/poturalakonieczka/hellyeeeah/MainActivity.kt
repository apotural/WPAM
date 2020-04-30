package com.poturalakonieczka.hellyeeeah

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.facebook.*
import com.facebook.login.LoginResult
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuth.AuthStateListener
import kotlinx.android.synthetic.main.activity_main.*
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

class MainActivity : AppCompatActivity() {
    private var callbackManager: CallbackManager? = null
    private var authListener: AuthStateListener? = null
    private val TAG = "FacebookAuth"
    private var fbAuth: FirebaseAuth? = null


    private fun exchangeAccessToken(token: AccessToken) {
        val credential = FacebookAuthProvider.getCredential(token.token)
        fbAuth!!.signInWithCredential(credential)
            .addOnCompleteListener(this
            ) { task ->
                if (!task.isSuccessful) {
                    Toast.makeText(
                        this@MainActivity,
                        "Authentication failed.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        FacebookSdk.sdkInitialize(this)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        callbackManager = CallbackManager.Factory.create();

        loginButton.setReadPermissions("email", "public_profile");

        loginButton.registerCallback(callbackManager,
            object : FacebookCallback<LoginResult> {
                override fun onSuccess(loginResult: LoginResult) {
                    Log.d(TAG, "onSuccess: $loginResult")
                    exchangeAccessToken(loginResult.accessToken)
                    //finish()
                    Log.d("My-deb", "Zalogowanie sie")
                    val intent = Intent(this@MainActivity, UserActivity::class.java)
                    startActivity(intent)
                }

                override fun onCancel() {
                    Log.d(TAG, "onCancel: User cancelled sign-in")
                }

                override fun onError(error: FacebookException) {
                    Log.d(TAG, "onError: $error")
                }
            })

        fbAuth = FirebaseAuth.getInstance()

        authListener = AuthStateListener { firebaseAuth ->
            val user = firebaseAuth.currentUser
            if (user != null) {
                emailText.text = user.email
                statusText.text = "Signed In"
            } else {
                emailText.text = ""
                statusText.text = "Signed Out"

            }
        }
//        if (savedInstanceState == null) {
//            supportFragmentManager.beginTransaction()
//                .replace(R.id.container, FacebookLoginFragment.newInstance())
//                .commitNow()
//        }
    }

    override fun onStart() {
        super.onStart()
        fbAuth!!.addAuthStateListener(authListener!!)
    }

    override fun onStop() {
        super.onStop()
        if (authListener != null) {
            fbAuth!!.removeAuthStateListener(authListener!!)
        }
    }
    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        callbackManager!!.onActivityResult(requestCode, resultCode, data)
    }
}
