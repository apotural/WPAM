package com.poturalakonieczka.hellyeeeah

import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ListView
import androidx.fragment.app.Fragment
import com.google.firebase.Timestamp
import com.poturalakonieczka.hellyeeeah.layoutClasses.ClassesAdapters
import kotlinx.android.synthetic.main.user_classes_fragment.*
import kotlinx.android.synthetic.main.user_data_fragment.*
import yalantis.com.sidemenu.interfaces.ScreenShotable
import java.util.*

class ClassFragment : Fragment(), AdapterView.OnItemClickListener{
    private var classesAdapters:ClassesAdapters ? = null
    private var zajecia: MutableList<Timestamp> ? = mutableListOf()
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
        zajecia!!.add(Timestamp(Date(5000))) //temporary code to see if list works
        zajecia!!.add(Timestamp(Date(500000)))
        classesAdapters = ClassesAdapters( activity!!.applicationContext, zajecia!!)
        list.adapter = classesAdapters
        list.onItemClickListener = this
        //textView2.text = "class"
    }

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        Log.d(_TAG, "kliknieto " + position)
        var timestamp : Timestamp = zajecia!!.get(position)
        // pobierz z storage rzeczy z tego dnia, otworz ClassResourcesFragment
        //todo stworz item z lista na zdjecia!!!
        //open new fragment??
        /*activity!!.supportFragmentManager.beginTransaction().replace(
            R.id.container, ClassResourcesFragment.newInstance()).commit()*/
    }

}