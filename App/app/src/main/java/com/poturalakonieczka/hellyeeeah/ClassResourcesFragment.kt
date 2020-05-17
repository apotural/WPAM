package com.poturalakonieczka.hellyeeeah

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.poturalakonieczka.hellyeeeah.layoutClasses.ResourcesAdapter
import com.poturalakonieczka.hellyeeeah.storage.StorageItem
import kotlinx.android.synthetic.main.class_resources_fragment.*

class ClassResourcesFragment : Fragment(){
    private var resourcesAdapter: ResourcesAdapter? = null
    private val _TAG: String = "My-log ClassResourcesFragment"


    companion object {
        fun newInstance() = ClassResourcesFragment()
    }



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.class_resources_fragment, container, false)

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Log.d(_TAG, "Create Activity")
        textDate.text = UserActivity.storageView.getLastSelectedClassTime()
        var mutableList:MutableList<StorageItem?> = UserActivity.storageView.getCurrentList()
        resourcesAdapter = ResourcesAdapter(activity!!.applicationContext, mutableList)
        //mutableList.
        listResources.adapter = resourcesAdapter
        UserActivity.storageView.setCurrentFragment(this)
    }

    fun refreshContent(){
        resourcesAdapter!!.notifyDataSetChanged()
    }


}