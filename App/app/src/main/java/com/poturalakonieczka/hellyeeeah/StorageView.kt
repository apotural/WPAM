package com.poturalakonieczka.hellyeeeah

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.zxing.common.BitArray
import com.poturalakonieczka.hellyeeeah.storage.StorageItem

class StorageView : ViewModel() {
    private val _MAX_SIZE: Long = 1024 * 1024
    private var _mapItems : MutableMap<String, MutableList<StorageItem?>> = mutableMapOf()
    //private var _items : MutableList<StorageItem?> = mutableListOf()

    //private var storage = Firebase.storage
    private var mStorageRef = FirebaseStorage.getInstance().getReference()
    private val _TAG: String = "My-log storageView"

    @ExperimentalStdlibApi
    fun getFiles(pathToFolder : String){
        Log.d(_TAG, "Start ") //just in case needed
        if(_mapItems.contains(pathToFolder)){ //w zalozeniu ze jak cos sie doda to nas powiadomi i zawola sie inna funkcje
            return
        }
        var list : MutableList<StorageItem?> = mutableListOf()
        val listRef = mStorageRef.child(pathToFolder)
        listRef.listAll()
            .addOnSuccessListener { listResult ->
                listResult.prefixes.forEach { prefix ->
                    Log.d(_TAG, "pref") //just in case needed
                }
                listResult.items.forEach { item ->
                    var storItem = getStorageItem(item)
                    list.add(storItem)

                }
            }
            .addOnFailureListener {
                Log.d(_TAG, "fail " + it.toString())
            }
        _mapItems.put(pathToFolder, list)
    }

    @ExperimentalStdlibApi
    private fun getStorageItem(item : StorageReference) : StorageItem?{
        var storageItem = StorageItem()
        item.metadata.addOnSuccessListener { sM ->
            storageItem.setMetadata(sM)
            if(sM.contentType!!.contains("text")){
                item.getBytes(_MAX_SIZE).addOnSuccessListener {
                    var text = it!!.decodeToString()
                    Log.d(_TAG, "we downloaded! dec "+text)
                    storageItem.setText(text)
                }.addOnFailureListener{
                    Log.d(_TAG, "fail to download")
                }
            }else{
                item.downloadUrl.addOnSuccessListener {
                    Log.d(_TAG, "we downloaded!")
                    storageItem.setUri(it)
                }.addOnFailureListener{
                    Log.d(_TAG, "fail to download")
                }
            }
        }.addOnFailureListener {
            Log.d(_TAG, "fail to get Metadata")
        }
        return storageItem
    }

}