package com.poturalakonieczka.hellyeeeah

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class ModelView : ViewModel() {
    private var fbAuth: FirebaseAuth? = FirebaseAuth.getInstance()
    private var user: FirebaseUser? = fbAuth!!.currentUser


    fun getUser(): FirebaseUser? {
        return user
    }


}
