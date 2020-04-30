package com.poturalakonieczka.hellyeeeah

import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.ContextMenu
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser


class UserActivity : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.d("My-deb", "Create new activity ws")
        setContentView(R.layout.user_main)
        Log.d("My-deb", "Create new activity")


        if (savedInstanceState == null) {
            Log.d("My-deb", "Loading fragment")
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, UserDataFragment.newInstance())
                .commitNow()

        }
        else{
            Log.d("My-deb", "No saved Instatne State")
        }



    }

}