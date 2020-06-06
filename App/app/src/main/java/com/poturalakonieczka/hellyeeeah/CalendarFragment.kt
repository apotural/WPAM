package com.poturalakonieczka.hellyeeeah

import android.database.DataSetObserver
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.firebase.Timestamp
import com.poturalakonieczka.hellyeeeah.layoutCalendar.CalendarAdapter
import com.poturalakonieczka.hellyeeeah.layoutCalendar.CalendarItem
import com.poturalakonieczka.hellyeeeah.layoutCalendar.decorators.CancelledDayDecorator
import com.poturalakonieczka.hellyeeeah.layoutCalendar.decorators.CurrentDayDecorator
import com.poturalakonieczka.hellyeeeah.layoutCalendar.decorators.EventDotDecorator
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.CalendarMode
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import kotlinx.android.synthetic.main.user_calendar_fragment.*
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*


class CalendarFragment: Fragment(){
    /* green yellow purple red pink blue grey*/
    private enum class ClassType {
        BASIC, ADDITIONAL, CATCH_UP, MISSED,  MISSED_CATCH_UP, EXCUSED, LOST
    }

    lateinit var adapter:CalendarAdapter
    private val TAG = "My-deb calendar"

    private val yearFormat = SimpleDateFormat("yyyy", Locale.getDefault())
    private val monthFormat = SimpleDateFormat("MM", Locale.getDefault())
    private val dayFormat = SimpleDateFormat("dd", Locale.getDefault())

    private lateinit var currentDateString: String
    /* list that contains only proper calendar items, must be actualized in model view properly */
    private var calendarClassesList: List<CalendarItem?> = mutableListOf()

//    private var previousCancelledClasses: ZajeciaOdwolane = ZajeciaOdwolane()
//    private var previousAdditionalClasses: MutableList<ClassInCalendar?> = mutableListOf()
//    private var previousAbsentClasses: MutableList<ClassInCalendar?> = mutableListOf()
//    private var previousBasicClasses: MutableList<BasicClassInCalendar?> = mutableListOf()

    private var listOfDecorator: MutableList<EventDotDecorator?> = mutableListOf()
    private var cancelledDecorator: CancelledDayDecorator =
        CancelledDayDecorator()

    companion object {
        fun newInstance() = CalendarFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.user_calendar_fragment, container, false)
    }

    private fun updateCalendarList(dateFilter : String){
        Log.d("my-deb", dateFilter)
        adapter.filter.filter(dateFilter)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        initCalendarView()

        adapter = CalendarAdapter(activity!!.applicationContext, calendarClassesList)

        currentDateString = UserActivity.viewModel.getCurrentDateString()

        Log.d("my-deb", "main")
        updateCalendarList(currentDateString)
        calendarList.adapter = adapter

        UserActivity.viewModel.calendarClassesList.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            calendarClassesList = it
            adapter.setList(calendarClassesList)
            Log.d("my-deb", "observe")
            updateCalendarList(currentDateString)
        })
        
        calendarView.setOnDateChangedListener { _: MaterialCalendarView, calendarDay: CalendarDay, _: Boolean ->
            val dateString: String = calendarDay.day.toString()+"/"+calendarDay.month.toString()+"/"+calendarDay.year.toString()
            Log.d("my-deb", "click")
            UserActivity.viewModel.updateCurrentDateString(dateString)
            updateCalendarList(dateString)
        }
    }

    private fun removeDecorators(){
        calendarView.removeDecorator(cancelledDecorator)
        for(decorator in listOfDecorator){
            calendarView.removeDecorator(decorator)
        }
        calendarView.invalidateDecorators()
    }

    private fun addDecorators(){
        for(decorator in listOfDecorator){
            calendarView.addDecorator(decorator)
        }
        calendarView.addDecorator(cancelledDecorator)
    }

    private fun initCalendarView(){
        val min: Calendar = Calendar.getInstance()
        min.add(Calendar.MONTH, -2)
        val max: Calendar = Calendar.getInstance()
        max.add(Calendar.MONTH, 1)

        calendarView.state().edit()
            .setMinimumDate(CalendarDay.from(min.get(Calendar.YEAR), min.get(Calendar.MONTH)+1,  min.get(Calendar.DAY_OF_MONTH)))
            .setMaximumDate(CalendarDay.from(max.get(Calendar.YEAR), max.get(Calendar.MONTH)+1,  max.get(Calendar.DAY_OF_MONTH)))
            .setCalendarDisplayMode(CalendarMode.MONTHS)
            .commit()

        calendarView.addDecorator(
            CurrentDayDecorator(
                context!!.applicationContext
            )
        )
        calendarView.addDecorator(CancelledDayDecorator())

        listOfDecorator.add(
            EventDotDecorator(
                ContextCompat.getColor(context!!.applicationContext, R.color.colorDot1),
                0,
                hashSetOf()
            )
        )
        listOfDecorator.add(
            EventDotDecorator(
                ContextCompat.getColor(context!!.applicationContext, R.color.colorDot2),
                1,
                hashSetOf()
            )
        )
        listOfDecorator.add(
            EventDotDecorator(
                ContextCompat.getColor(context!!.applicationContext, R.color.colorDot3),
                2,
                hashSetOf()
            )
        )
        listOfDecorator.add(
            EventDotDecorator(
                ContextCompat.getColor(context!!.applicationContext, R.color.colorDot4),
                3,
                hashSetOf()
            )
        )
        listOfDecorator.add(
            EventDotDecorator(
                ContextCompat.getColor(context!!.applicationContext, R.color.colorDot5),
                4,
                hashSetOf()
            )
        )
        listOfDecorator.add(
            EventDotDecorator(
                ContextCompat.getColor(context!!.applicationContext, R.color.colorDot6),
                5,
                hashSetOf()
            )
        )
        listOfDecorator.add(
            EventDotDecorator(
                ContextCompat.getColor(context!!.applicationContext, R.color.colorDot7),
                6,
                hashSetOf()
            )
        )


    }


}
