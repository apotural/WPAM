package com.poturalakonieczka.hellyeeeah

import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.poturalakonieczka.hellyeeeah.layoutClasses.ResourcesAdapter
import com.poturalakonieczka.hellyeeeah.storage.StorageItem
import kotlinx.android.synthetic.main.class_resources_fragment.*
import java.util.*

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
        }

        )

    }

    private fun refreshContent(){
        resourcesAdapter!!.notifyDataSetChanged()
    }


}