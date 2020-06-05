package com.poturalakonieczka.hellyeeeah

import android.R.attr.data
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.facebook.*
import com.facebook.login.LoginResult
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuth.AuthStateListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.functions.FirebaseFunctions
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_main.*


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
                if (task.isSuccessful) {
                    var fullName = fbAuth!!.currentUser?.displayName
                    var listName = fullName?.split(" ")
                    var name = ""
                    var surname = ""
                    if (listName != null) {
                        if(listName.isNotEmpty()){
                            name = listName[0];
                        }
                        if(listName.size > 1){
                            surname = listName[1];
                        }
                    }
                    val data = hashMapOf(
                        "email" to fbAuth!!.currentUser?.email,
                        "name" to name,
                        "surname" to surname
                    )
                    FirebaseFunctions.getInstance()
                        .getHttpsCallable("loginApp")
                        .call(data)
                        .continueWith { task ->
                            val result = task.result?.data as String
                            result
                        }
                }
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        FacebookSdk.sdkInitialize(this)

        super.onCreate(savedInstanceState)


        setContentView(R.layout.activity_main)
        callbackManager = CallbackManager.Factory.create();

        fbAuth = FirebaseAuth.getInstance()

        authListener = AuthStateListener { firebaseAuth ->
            val user = firebaseAuth.currentUser
            if (user != null) {
                finish()
                val intent = Intent(this@MainActivity, UserActivity::class.java)
                startActivity(intent)
            } else {


            }
        }

        loginButton.setReadPermissions("email", "public_profile");

        loginButton.registerCallback(callbackManager,
            object : FacebookCallback<LoginResult> {
                override fun onSuccess(loginResult: LoginResult) {

                    Log.d(TAG, "onSuccess: $loginResult")
                    exchangeAccessToken(loginResult.accessToken)
                }

                override fun onCancel() {
                    Log.d(TAG, "onCancel: User cancelled sign-in")
                }

                override fun onError(error: FacebookException) {
                    Log.d(TAG, "onError: $error")
                }
            })

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
