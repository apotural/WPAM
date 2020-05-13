package com.poturalakonieczka.hellyeeeah

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import android.widget.BaseAdapter
import androidx.fragment.app.Fragment
import com.google.firebase.Timestamp
import com.poturalakonieczka.hellyeeeah.layoutClasses.ClassesAdapters
import kotlinx.android.synthetic.main.user_classes_fragment.*
import java.util.*


class ClassFragment : Fragment(){
    private var classesAdapters: ClassesAdapters? = null
    private var zajecia: MutableList<Timestamp?> = mutableListOf()
    private val _TAG: String = "My-log ClassFragment"

    companion object {
        fun newInstance() = ClassFragment()
    }

    private lateinit var viewModel: ModelView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.user_classes_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        zajecia.add(Timestamp(Date(5000))) //temporary code to see if list works
        zajecia.add(Timestamp(Date(500000)))

        classesAdapters = ClassesAdapters(activity!!.applicationContext , zajecia);
        list.adapter = classesAdapters
        list.onItemClickListener = OnItemClickListener { listView, _, itemPosition, _ ->

            var timestamp: Timestamp? = listView.getItemAtPosition(itemPosition) as Timestamp?
            activity!!.supportFragmentManager.beginTransaction().replace(
                R.id.container, ClassResourcesFragment.newInstance()
            ).addToBackStack("classList").commit()
        }

    }
}
//        classesAdapters = ClassesAdapters(activity!!.applicationContext, zajecia)
//        list.adapter = classesAdapters
//        list.onItemClickListener = OnItemClickListener { listView, _, itemPosition, _ ->
//
//            var timestamp: Timestamp? = listView.getItemAtPosition(itemPosition) as Timestamp?
////            activity!!.supportFragmentManager.beginTransaction().replace(
////                R.id.container, ClassResourcesFragment.newInstance()
////            ).commit()
//        }
  //  }



        //textView2.text = "class"

//}
//}
