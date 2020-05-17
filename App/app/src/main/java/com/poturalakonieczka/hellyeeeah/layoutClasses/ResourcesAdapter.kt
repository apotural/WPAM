package com.poturalakonieczka.hellyeeeah.layoutClasses

import android.content.Context
import android.icu.text.SimpleDateFormat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import android.widget.VideoView

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
        Log.d(_TAG, "Starting conversion")
        val item = mutableList[position]
        if (item != null){
            val metadata = item.getMetadata()
            if (metadata != null){
                val type = metadata.contentType
                when {
                    type!!.contains("text/") -> {
                        val comment: TextView = convertView!!.findViewById(R.id.comment)
                        comment.text = item.getText()
                    }
                    type.contains("image/") -> {
                        val image: ImageView = convertView!!.findViewById(R.id.image_resource)
                        image.setImageURI(item.getUri())
                        Picasso.get().load(item.getUri()).into(image)
                    }
                    type.contains("video/") -> {
                        val video : VideoView = convertView!!.findViewById(R.id.video_resource)
                        video.setVideoURI(item.getUri())
                        video.start() //to delete, just checking
                        Log.d(_TAG, "Video start")
                    }
                }
                val userName = metadata.getCustomMetadata("userName")
                if (userName != null){
                    val userNameText :TextView = convertView!!.findViewById(R.id.resource_adder)
                    userNameText.text = userName
                }

                val dateFormat = SimpleDateFormat("MM-dd-yyyy HH:mm:ss")
                val date = Date(metadata!!.creationTimeMillis)
                val timeOfElement :TextView = convertView!!.findViewById(R.id.time_of_element)
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
}