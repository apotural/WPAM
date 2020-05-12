package com.poturalakonieczka.hellyeeeah

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.poturalakonieczka.hellyeeeah.database.Grupa
import com.poturalakonieczka.hellyeeeah.database.Kursant


class ModelView : ViewModel() {
    private val _TAG: String = "My-log viewModel"

    private var _fbAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private var _db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private var _user: FirebaseUser? = _fbAuth.currentUser

    private var _participant: Kursant? = null
    private val _groups: MutableList<Grupa?> = mutableListOf()
    private var _previousGroupReference: MutableList<DocumentReference?> =  mutableListOf()
    private var _mapListener: MutableMap<DocumentReference?, ListenerRegistration> = mutableMapOf()
    private var _mapGroupRef: MutableMap<DocumentReference?, Grupa?> = mutableMapOf()

    private fun findGroupChange(){
        val added = _participant!!.grupy.toSet().minus(_previousGroupReference.toSet())
        val removed = _previousGroupReference.toSet().minus(_participant!!.grupy.toSet())
        for (add in added){
            downloadGroup(add)
        }
        for (remove in removed){
            removeGroup(remove)
        }
    }

    fun downloadParticipant() {
        if(_user != null) {
            _user!!.email?.let {_db.collection("Kursanci").document(it) }
                ?.addSnapshotListener { snapshot, e ->
                if (e != null) {
                    Log.w(_TAG, "Listen failed.", e)
                    return@addSnapshotListener
                }

                if (snapshot != null && snapshot.exists()) {
                    _participant = snapshot.toObject(Kursant::class.java)
                    findGroupChange()
                    _previousGroupReference = _participant!!.grupy
                }
            }
        }
    }

    private fun downloadGroup(doc: DocumentReference?) {
        val lisReg =  doc!!.addSnapshotListener { snapshot, e ->
            if (e != null) {
                Log.w(_TAG, "Listen failed.", e)
                return@addSnapshotListener
            }

            if (snapshot != null && snapshot.exists()) {
                val group = snapshot.toObject(Grupa::class.java)
                _groups.add(group)
                _mapGroupRef[doc] = group
            }
        }
        _mapListener[doc] = lisReg;
    }

    private fun removeGroup(doc: DocumentReference?) {
        _mapListener[doc]?.remove()
        _mapListener.remove(doc);
        _groups.remove(_mapGroupRef[doc])
        _mapGroupRef.remove(doc)
    }

//    private fun downloadGroups() {
//        for (doc in _participant!!.grupy) {
//            doc.addSnapshotListener { snapshot, e ->
//                if (e != null) {
//                    Log.w(_TAG, "Listen failed.", e)
//                    return@addSnapshotListener
//                }
//
//                if (snapshot != null && snapshot.exists()) {
//                    Log.d(_TAG, "display groups" + _groups.size )
//                    _groups.add(snapshot.toObject(Grupa::class.java))
//                }
//            }
//        }
//    }

    private fun downloadGroupHistory(){

    }

    private fun downloadCancelledClasses(){

    }

    private fun downloadAdditionalClasses(){

    }

    private fun downloadClasses(){
        downloadGroupHistory()
        downloadCancelledClasses()
        downloadAdditionalClasses()
    }


    //    private fun listToJSONArray(list: List<Any>): JSONArray? {
//        val arr = JSONArray()
//        for (obj in list) {
//            when (obj) {
//                is Map<*, *> -> {
//                    arr.put(mapToJSON(obj as Map<String, Any>))
//                }
//                is List<*> -> {
//                    arr.put(listToJSONArray(obj as List<Any>))
//                }
//                else -> {
//                    arr.put(obj)
//                }
//            }
//        }
//        return arr
//    }
//
//    private fun mapToJSON(map: Map<String, Any>): JSONObject? {
//        val obj = JSONObject()
//        for ((key, value) in map) {
//            if (value is Map<*, *>) {
//                val subMap =
//                    value as Map<String, Any>
//                obj.put(key, mapToJSON(subMap))
//            } else if (value is List<*>) {
//                obj.put(key, listToJSONArray(value as List<Any>))
//            } else {
//                obj.put(key, value)
//            }
//        }
//        return obj
//    }
//
//    fun downloadKursant(): Kursant?{
//        val docRef = user?.email?.let { db.collection("Kursanci").document(it) }
//        docRef?.get()
//            ?.addOnSuccessListener { document ->
//                if (document != null) {
//                    Log.d(TAG, "got userName")
//                    kursant = document.toObject(Kursant::class.java)
//                    downloadGrupy()
//                    downloadZajeciaDodatkowe()
//                    downloadZajeciaDoOdrobienia()
////                    val obj = mapToJSON(document.data!!)
////                    Log.d(TAG, obj.toString())
//                } else {
//
//                }
//            }
//            ?.addOnFailureListener { exception ->
//
//            }
//        return kursant
//    }
//
//    fun getKursant(): Kursant?{
//        return kursant
//    }
//
//    private fun downloadZajeciaDoOdrobienia(){
//        if(kursant != null){
//            user?.email?.let { db.collection("Kursanci").document(it) }
//
//            val docRef = user?.email?.let {
//                db.collection("Kursanci/$it/zajecia").document("nieobecnosc")
//            }
//            docRef?.get()?.addOnSuccessListener { document ->
//                if (document != null) {
//                    zajeciaDoOdrobienia = document.toObject(ZajecieDoOdrobienia::class.java)
//                }
//            }
//                ?.addOnFailureListener { exception ->
//
//                }
//
//        }
//    }
//
//    private fun downloadZajeciaDodatkowe(){
//        if(kursant != null){
//            user?.email?.let { db.collection("Kursanci").document(it) }
//
//            val docRef = user?.email?.let {
//                db.collection("Kursanci/$it/zajecia").document("dodatkowe")
//            }
//            docRef?.get()?.addOnSuccessListener { document ->
//                    if (document != null) {
//                        zajeciaDodatkowe = document.toObject(ZajeciaDodatkowe::class.java)
//                    }
//                }
//                ?.addOnFailureListener { exception ->
//
//                }
//
//        }
//    }
//
//    private fun downloadGrupy(){
//        Log.d(TAG, "pobieramy grupy")
//        //zakladamy ze mamu kursanta -> wiec przejezdzamy forem po wszystkich jego z listy i tworzymy liste grup
//
//        if(kursant != null){
//            Log.d(TAG, "kursant jest")
//            for(doc in kursant!!.grupy){
//                Log.d(TAG, "dowloading groups")
//                doc.get()
//                    .addOnSuccessListener { result ->
//                        val grupaJSON = mapToJSON(result.data!!)
//                        val grupa  = result.toObject(Grupa::class.java)
//                        grupy.add(grupa)
//                        Log.d(TAG, grupaJSON.toString())
//                    }
//                    .addOnFailureListener { exception ->
//                        Log.d(TAG, "Error getting documents: ", exception)
//                    }
//            }
//        }
//    }


}

