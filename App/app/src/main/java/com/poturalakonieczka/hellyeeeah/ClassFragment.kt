package com.poturalakonieczka.hellyeeeah

import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.user_data_fragment.*
import yalantis.com.sidemenu.interfaces.ScreenShotable

class ClassFragment : Fragment(){

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
        textView2.text = "class"
    }

}