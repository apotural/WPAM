package com.poturalakonieczka.hellyeeeah.storage

import android.net.Uri
import com.google.firebase.storage.StorageMetadata

class StorageItem() {
    private var metadata : StorageMetadata ? = null

    private var text : String? = null

    private var uri : Uri? = null

    fun setMetadata( met : StorageMetadata){
        metadata = met
    }
    fun setText(string : String){
        text=string
    }
    fun setUri(ur : Uri){
        uri = ur
    }
    fun getMetadata(): StorageMetadata?{
        return metadata
    }
    fun getText(): String?{
        return text
    }
    fun getUri():Uri?{
        return uri
    }
}