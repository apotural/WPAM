package com.poturalakonieczka.hellyeeeah

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.user_calendar_fragment.*
import java.util.*


class CalendarFragment: Fragment(){
    val calendar: Calendar = Calendar.getInstance()
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

        val oneMonthsAhead = Calendar.getInstance()
        oneMonthsAhead.add(Calendar.MONTH, 1)

        calendarView.minDate = Date().time - 1000L*60L*60L*24L*365L
        calendarView.maxDate = Date().time + 1000L*60L*60L*24L*30L
    }
}
