package com.poturalakonieczka.hellyeeeah

import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import yalantis.com.sidemenu.interfaces.ScreenShotable


class CalendarFragment: Fragment(), ScreenShotable{
    private val containerView: View? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.user_calendar_fragment, container, false)
    }


    override fun getBitmap(): Bitmap {
        TODO("Not yet implemented")
    }

    override fun takeScreenShot() {
        TODO("Not yet implemented")
    }
}