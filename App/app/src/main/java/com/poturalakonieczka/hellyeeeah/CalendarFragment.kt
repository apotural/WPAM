package com.poturalakonieczka.hellyeeeah

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.user_calendar_fragment.*
import java.time.Month
import java.util.*


class CalendarFragment: Fragment(){
    val TAG = "My-deb calendar"

    companion object {
        fun newInstance() = CalendarFragment()
    }
    private lateinit var viewModel: ModelView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.user_calendar_fragment, container, false)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val min: Calendar = Calendar.getInstance()
        min.add(Calendar.MONTH, -2);
        val max: Calendar = Calendar.getInstance()
        max.add(Calendar.MONTH, 1);

        calendarView.setMinimumDate(min)
        calendarView.setMaximumDate(max)

    }

}
