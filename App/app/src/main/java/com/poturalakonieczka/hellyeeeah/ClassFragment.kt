package com.poturalakonieczka.hellyeeeah

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import androidx.fragment.app.Fragment
import com.google.firebase.Timestamp
import com.poturalakonieczka.hellyeeeah.layoutClasses.ClassesAdapter
import com.poturalakonieczka.hellyeeeah.layoutClasses.ResourcesAdapter
import kotlinx.android.synthetic.main.user_classes_fragment.*
import java.util.*


class ClassFragment : Fragment(){
    private var classesAdapter: ClassesAdapter? = null
    private var zajecia: MutableList<Timestamp?> = mutableListOf()
    private val _TAG: String = "My-log ClassFragment"

    companion object {
        fun newInstance() = ClassFragment()
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.user_classes_fragment, container, false)
    }

    @ExperimentalStdlibApi
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        zajecia.add(Timestamp(Date(5000))) //argumentem sa milisekundt
        zajecia.add(Timestamp(Date(500000)))


        classesAdapter = ClassesAdapter(activity!!.applicationContext , zajecia);
        list.adapter = classesAdapter
        list.onItemClickListener = OnItemClickListener { listView, _, itemPosition, _ ->

            var timestamp: Timestamp? = listView.getItemAtPosition(itemPosition) as Timestamp?
            if(timestamp != null){
                //UserActivity.storageView.setSelectedClassesTime(timestamp!!.seconds.toString())
                UserActivity.storageView.getFiles(timestamp!!.toDate().time.toString())
                activity!!.supportFragmentManager.beginTransaction().replace(
                    R.id.container, ClassResourcesFragment.newInstance()
                ).addToBackStack("classList").commit()
            }
        }

    }
}

