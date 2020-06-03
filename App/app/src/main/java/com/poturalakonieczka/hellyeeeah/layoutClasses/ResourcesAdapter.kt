package com.poturalakonieczka.hellyeeeah.layoutClasses

import android.content.Context
import android.icu.text.SimpleDateFormat
import android.media.MediaPlayer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.poturalakonieczka.hellyeeeah.R
import com.poturalakonieczka.hellyeeeah.storage.StorageItem
import com.squareup.picasso.Picasso
import java.util.*


class ResourcesAdapter(var context: Context, var mutableList: MutableList<StorageItem?> ): BaseAdapter() {
    val _TAG :String = "My-log ResourcesAdapter"
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var convertView = convertView
        if(convertView == null){
            val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            convertView = inflater.inflate(R.layout.card_view_resources, null)
        }
        val item = mutableList[position]
        if (item != null){
            val metadata = item.getMetadata()
            if (metadata != null){
                val type = metadata.contentType
                val comment: TextView = convertView!!.findViewById(R.id.comment)
                val image: ImageView = convertView!!.findViewById(R.id.image_resource)
                var video : VideoView = convertView!!.findViewById(R.id.video_resource)
                val userNameText :TextView = convertView!!.findViewById(R.id.resource_adder)
                val timeOfElement :TextView = convertView!!.findViewById(R.id.time_of_element)
                //need to clear all of the elements as there was a problem when the list changed and different type of content was in the same view
                comment.text = ""
                image.setImageResource(0)
                //video
                userNameText.text = ""
                timeOfElement.text = ""
                when {
                    type!!.contains("text/") -> {
                        comment.text = item.getText()
                    }
                    type.contains("image/") -> {
                        Log.d(_TAG, "image start")
                        //image.setImageURI(item.getUri())
                        Picasso.get().load(item.getUri())
                            .into(image)
                    }
                    type.contains("video/") -> {
                        video.setVideoURI(item.getUri())
                        video.setOnPreparedListener {
                            Log.d(_TAG, "Video Prepared")
                            if(it!= null){
                                Log.d(_TAG, "Video height "+ it!!.videoHeight+ " and width "+it!!.videoWidth)
                            }
                        }
                        video.setOnErrorListener(MediaPlayer.OnErrorListener { mediaPlayer, i, i1 ->
                            Log.d(_TAG, "Error")
                            false })
                        Log.d(_TAG, "Video start")
                    }
                }
                val userName = metadata.getCustomMetadata("userName")
                if (userName != null){
                    userNameText.text = userName
                }
                val dateFormat = SimpleDateFormat("MM-dd-yyyy HH:mm:ss")
                val date = Date(metadata!!.creationTimeMillis)
                timeOfElement.text = dateFormat.format(date)

            }
        }
        return convertView!!
    }

    override fun getItem(position: Int): Any {
        return mutableList[position]!!
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return mutableList.size
    }

    fun sortList(){
        mutableList.sortBy { storageItem -> storageItem?.getMetadata()?.updatedTimeMillis }
    }
}