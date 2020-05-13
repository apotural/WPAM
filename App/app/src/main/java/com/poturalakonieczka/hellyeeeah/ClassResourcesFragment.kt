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

class ClassResourcesFragment : Fragment(){

    companion object {
        fun newInstance() = ClassResourcesFragment()
    }

    private lateinit var viewModel: ModelView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.class_resources_fragment, container, false)

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Log.d("My-deb", "OPEN")
    }




}