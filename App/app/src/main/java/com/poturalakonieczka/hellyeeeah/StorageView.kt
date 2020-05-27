package com.poturalakonieczka.hellyeeeah

import android.content.Intent
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.poturalakonieczka.hellyeeeah.storage.StorageItem



class StorageView : ViewModel() {
    private val _MAX_SIZE: Long = 1024 * 1024
    private var _mapItems : MutableMap<String, MutableList<StorageItem?>> = mutableMapOf()
    private var _currentTimestamp :String =""
    private var mStorageRef = FirebaseStorage.getInstance().getReference()
    private val _TAG: String = "My-log storageView"
    var needToRefresh : MutableLiveData<Boolean> = MutableLiveData(false)
    private var _isImageSet : Boolean = false
    private var _isCommenting : Boolean = false
    private var _imageToAdd : Intent? = null


    @ExperimentalStdlibApi
    fun getFiles(pathToFolder : String){
        _currentTimestamp = pathToFolder
        Log.d(_TAG, "Start ") //just in case needed
        if(_mapItems.contains(pathToFolder)){ //w zalozeniu ze jak cos sie doda to nas powiadomi i zawola sie inna funkcje
            return
        }
        var list : MutableList<StorageItem?> = mutableListOf()
        _mapItems[pathToFolder] = list
        val listRef = mStorageRef.child(pathToFolder)
        listRef.listAll()
            .addOnSuccessListener { listResult ->
                listResult.prefixes.forEach { prefix ->
                    Log.d(_TAG, "pref") //just in case needed
                }
                listResult.items.forEach { item ->
                    var storageItem = getStorageItem(item)
                    list.add(storageItem)

                }
            }
            .addOnFailureListener {
                Log.d(_TAG, "fail " + it.toString())
            }


    }

    @ExperimentalStdlibApi
    private fun getStorageItem(item : StorageReference) : StorageItem?{
        var storageItem = StorageItem()
        item.metadata.addOnSuccessListener { sM ->
            storageItem.setMetadata(sM)
            if(sM.contentType!!.contains("text/")){
                item.getBytes(_MAX_SIZE).addOnSuccessListener {
                    var text = it!!.decodeToString()
                    Log.d(_TAG, "we downloaded! dec "+text)
                    storageItem.setText(text)
                    needToRefresh.value = true
                }.addOnFailureListener{
                    Log.d(_TAG, "fail to download")
                }
            }else{
                item.downloadUrl.addOnSuccessListener {
                    Log.d(_TAG, "we downloaded!")
                    storageItem.setUri(it)
                    needToRefresh.value = true
                }.addOnFailureListener{
                    Log.d(_TAG, "fail to download")
                }
            }
        }.addOnFailureListener {
            Log.d(_TAG, "fail to get Metadata")
        }
        return storageItem
    }

    fun getCurrentList():MutableList<StorageItem?>{
        return _mapItems[_currentTimestamp]!!
    }
    fun getLastSelectedClassTime():String{
        return _currentTimestamp
    }

    fun getIsImageSet():Boolean{
        return _isImageSet
    }

    fun setIsImageSet(v : Boolean){
        _isImageSet = v
    }

    fun getIsCommenting():Boolean{
        return _isCommenting
    }

    fun setIsCommenting(v : Boolean){
        _isCommenting = v
    }

    fun setImageToAdd(data:Intent?){
        _imageToAdd = data
    }

    fun getImageToAdd():Intent?{
        return _imageToAdd
    }

    fun sendMessage(){

    }
}