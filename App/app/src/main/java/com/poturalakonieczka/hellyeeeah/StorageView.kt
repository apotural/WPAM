package com.poturalakonieczka.hellyeeeah

import android.app.Application
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storageMetadata
import com.poturalakonieczka.hellyeeeah.storage.StorageItem
import id.zelory.compressor.Compressor
import kotlinx.coroutines.launch
import java.io.File


class StorageView (application: Application): AndroidViewModel(application) {
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
        if(_mapItems.contains(pathToFolder)){ //w zalozeniu ze jak cos sie doda to nas powiadomi i zawola sie inna funkcje
            return
        }
        val list : MutableList<StorageItem?> = mutableListOf()
        _mapItems[pathToFolder] = list
        val listRef = mStorageRef.child(pathToFolder)
        listRef.listAll()
            .addOnSuccessListener { listResult ->
                listResult.prefixes.forEach { prefix ->
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

    private fun downloadItems(list : MutableList<StorageItem?>){

    }

    @ExperimentalStdlibApi
    private fun getStorageItem(item : StorageReference) : StorageItem?{
        val storageItem = StorageItem()
        item.metadata.addOnSuccessListener { sM ->
            storageItem.setMetadata(sM)
            if(sM.contentType!!.contains("text/")){
                item.getBytes(_MAX_SIZE).addOnSuccessListener {
                    var text = it!!.decodeToString()
                    storageItem.setText(text)
                    needToRefresh.value = true
                }.addOnFailureListener{
                    Log.d(_TAG, "fail to download")
                }
            }else{
                item.downloadUrl.addOnSuccessListener {
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
    fun getRealPathFromUri(context: Context, contentUri: Uri): String? {
        var cursor: Cursor? = null
        return try {
            val proj =
                arrayOf(MediaStore.Images.Media.DATA)
            cursor = context.contentResolver.query(contentUri, proj, null, null, null)
            val column_index: Int? = cursor?.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            cursor?.moveToFirst()
            cursor?.getString(column_index!!)
        } finally {
            if (cursor != null) {
                cursor.close()
            }
        }
    }
    @ExperimentalStdlibApi
    fun update(string: String){
        Log.d(_TAG, "update")
        val dir = string.substringBefore('/')
        if(_mapItems.contains(dir)){
            val ref = mStorageRef.child(string)
            _mapItems[dir]!!.add(getStorageItem(ref))
        }
    }

    @ExperimentalStdlibApi
    fun sendContent(comment: String){
        val userName = UserActivity.viewModel.getParticipantName()
        val tsLong = System.currentTimeMillis() / 1000
        val fileName = userName.replace(" ", "")+tsLong.toString()
        Log.d(_TAG, fileName)

        if(!comment.isBlank()){
            val fNameT = fileName+".txt"
            val data = comment.toByteArray()
            var metadata = storageMetadata {
                contentType = "text/plain"
                setCustomMetadata("userName", userName)
            }
            val ref = mStorageRef.child(_currentTimestamp+"/"+fNameT)
            var uploadTask = ref.putBytes(data, metadata)
            uploadTask.addOnSuccessListener {
                Log.d(_TAG, "file uploaded!")
            }.addOnFailureListener{
                Log.d(_TAG, "error while uploading file!")
            }
        }
        if(_isImageSet){
            val fNameI = fileName+".jpg"
            val uri = _imageToAdd!!.data!!
            val path =  getRealPathFromUri(getApplication(),uri)
            Log.d(_TAG, path)
            val file = File(path)
            var metadata = storageMetadata {
                contentType = "image/jpeg"
                setCustomMetadata("userName", userName)
            }

            viewModelScope.launch{
                val compressedImageFile = Compressor.compress(getApplication(),file )
                val ref = mStorageRef.child(_currentTimestamp+"/"+fNameI)
                val imageURI = Uri.fromFile(compressedImageFile)
                var uploadTask = ref.putFile(imageURI, metadata)
                uploadTask.addOnSuccessListener {
                    Log.d(_TAG, "file uploaded!")
                }.addOnFailureListener{
                    Log.d(_TAG, "error while uploading file!")
                }
            }



        }
    }


}