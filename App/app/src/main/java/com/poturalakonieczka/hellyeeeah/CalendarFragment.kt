package com.poturalakonieczka.hellyeeeah

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.CalendarMode
import kotlinx.android.synthetic.main.user_calendar_fragment.*
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
        calendarView.state().edit()
            .setMinimumDate(CalendarDay.from(2016, 4, 3))
            .setMaximumDate(CalendarDay.from(2016, 5, 12))
            .setCalendarDisplayMode(CalendarMode.MONTHS)
            .commit();

        //calendarView.minDate = Date().time - 1000L*60L*60L*24L*365L
        //calendarView.maxDate = Date().time + 1000L*60L*60L*24L*30L
    }

}
