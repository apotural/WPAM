package com.poturalakonieczka.hellyeeeah

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.poturalakonieczka.hellyeeeah.layoutClasses.ResourcesAdapter
import com.poturalakonieczka.hellyeeeah.storage.StorageItem
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.class_resources_fragment.*
import java.util.*


class ClassResourcesFragment : Fragment(){
    private var resourcesAdapter: ResourcesAdapter? = null
    private val _TAG: String = "My-log ClassResourcesFragment"


    companion object {
        fun newInstance() = ClassResourcesFragment()
        const val IMAGE_PICK_CODE = 1000
    }



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.class_resources_fragment, container, false)

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Log.d(_TAG, "Create Activity")

        val dateFormat = SimpleDateFormat("EEE, MM-dd-yyyy HH:mm")
        val date = Date(UserActivity.storageView.getLastSelectedClassTime().toLong())
        class_date_text.text = "Materiały z zajęć  "+dateFormat.format(date)
        val mutableList:MutableList<StorageItem?> = UserActivity.storageView.getCurrentList()
        resourcesAdapter = ResourcesAdapter(activity!!.applicationContext, mutableList)
        //mutableList.
        listResources.adapter = resourcesAdapter
        UserActivity.storageView.needToRefresh.observe(activity!!, Observer { newValue: Boolean ->
            Log.d(_TAG, "refresh $newValue")
            refreshContent()
        })
        setStateComment(UserActivity.storageView.getIsCommenting())

        fab_add.setOnClickListener {
            setStateComment(true)
        }
        button_back.setOnClickListener {
            setStateComment(false)
        }
        button_send.setOnClickListener {
            UserActivity.storageView.sendMessage()
            clearComment()
        }
        button_photo.setOnClickListener {
            if(activity!!.checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED){
                val permission = arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                activity!!.requestPermissions(permission, IMAGE_PICK_CODE)
            }
            else{
                pickImageFromGallery()
            }
        }
        but_cancel_photo.setOnClickListener {
            UserActivity.storageView.setIsImageSet(false)
            lay_thumb.visibility = View.GONE
        }

    }

    private fun pickImageFromGallery(){
        val intent = Intent(Intent.ACTION_PICK )
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_PICK_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        //super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK && requestCode == IMAGE_PICK_CODE){
            lay_thumb.visibility = View.VISIBLE
            //thumb_photo.setImageURI(data?.data)
            UserActivity.storageView.setIsImageSet(true)
            UserActivity.storageView.setImageToAdd(data)
            Picasso.get().load(data?.data).resize(50,50).into(thumb_photo)

        }
    }

    private fun setStateComment(isComment: Boolean){
        var com = View.GONE
        var but = View.GONE
        if(isComment){
            com = View.VISIBLE
        }
        else{
            but = View.VISIBLE
        }
        comment_pole.visibility = com
        lay_buttons.visibility = com
        fab_add.visibility = but
        lay_thumb.visibility = View.GONE
        UserActivity.storageView.setIsCommenting(isComment)
        if(UserActivity.storageView.getIsImageSet() && isComment){
            lay_thumb.visibility = View.VISIBLE
            val data = UserActivity.storageView.getImageToAdd()
            Picasso.get().load(data?.data).resize(50,50).into(thumb_photo)
        }
    }

    private fun refreshContent(){
        resourcesAdapter!!.notifyDataSetChanged()
    }


    private fun clearComment(){
        if(text_comment.text != null){
            text_comment.text!!.clear()
        }
        UserActivity.storageView.setIsImageSet(false)
        setStateComment(true) // mozliwe ze do zmiany w zaleznosci co bedziemy chcialy!

    }


}