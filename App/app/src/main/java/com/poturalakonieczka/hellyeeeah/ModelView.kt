package com.poturalakonieczka.hellyeeeah

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.poturalakonieczka.hellyeeeah.database.Grupa
import com.poturalakonieczka.hellyeeeah.database.Kursant
import org.json.JSONArray
import org.json.JSONObject


class ModelView : ViewModel() {
    private val TAG: String = "My-log viewModel"
    private var fbAuth: FirebaseAuth? = FirebaseAuth.getInstance()
    private var user: FirebaseUser? = fbAuth!!.currentUser
    private var db = FirebaseFirestore.getInstance()
    private var kursant: Kursant? = null
    private val grupy: MutableList<Grupa?> = mutableListOf<Grupa?>()

    fun updateUser(){
        fbAuth= FirebaseAuth.getInstance()
        user =  fbAuth!!.currentUser
    }

    fun getUser(): FirebaseUser? {
        return user
    }
    private fun listToJSONArray(list: List<Any>): JSONArray? {
        val arr = JSONArray()
        for (obj in list) {
            when (obj) {
                is Map<*, *> -> {
                    arr.put(mapToJSON(obj as Map<String, Any>))
                }
                is List<*> -> {
                    arr.put(listToJSONArray(obj as List<Any>))
                }
                else -> {
                    arr.put(obj)
                }
            }
        }
        return arr
    }
    
    private fun mapToJSON(map: Map<String, Any>): JSONObject? {
        val obj = JSONObject()
        for ((key, value) in map) {
            if (value is Map<*, *>) {
                val subMap =
                    value as Map<String, Any>
                obj.put(key, mapToJSON(subMap))
            } else if (value is List<*>) {
                obj.put(key, listToJSONArray(value as List<Any>))
            } else {
                obj.put(key, value)
            }
        }
        return obj
    }

    fun downloadKursant(): Kursant?{
        val docRef = user?.email?.let { db.collection("Kursanci").document(it) }
        docRef?.get()
            ?.addOnSuccessListener { document ->
                if (document != null) {
                    Log.d("My_log", "got userName")
                    kursant = document.toObject(Kursant::class.java)
                    downloadGrupy()
                    //val obj = mapToJSON(document.data!!)
                    
                } else {

                }
            }
            ?.addOnFailureListener { exception ->

            }
        return kursant
    }

    fun getKursant(): Kursant?{
        return kursant
    }

    private fun downloadGrupy(){
        Log.d(TAG, "pobieramy grupy")
        //zakladamy ze mamu kursanta -> wiec przejezdzamy forem po wszystkich jego z listy i tworzymy liste grup

        if(kursant != null){
            Log.d(TAG, "kursant jest")
            for(doc in kursant!!.grupy){
                Log.d(TAG, "dowloading groups")
                doc.get()
                    .addOnSuccessListener { result ->
                        //val grupa = mapToJSON(result.data!!)
                        val grupa  = result.toObject(Grupa::class.java)
                        grupy.add(grupa)
                    }
                    .addOnFailureListener { exception ->
                        Log.d(TAG, "Error getting documents: ", exception)
                    }
            }
        }
    }





}

