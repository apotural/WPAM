package com.poturalakonieczka.hellyeeeah

import android.database.DataSetObserver
import android.os.Bundle
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
        adapter.filter.filter(dateFilter)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        initCalendarView()

        var calendarClassesList: MutableList<CalendarItem?> = mutableListOf()



        val l0 = LocalDateTime.parse("05/06/2020 10:15", DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))
        val l1 = LocalDateTime.parse("05/06/2020 12:15", DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))
        val l2 = LocalDateTime.parse("05/06/2020 14:15", DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))
        val l3 = LocalDateTime.parse("05/06/2020 18:15", DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))
        val l4 = LocalDateTime.parse("06/06/2020 10:15", DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))
        val l5 = LocalDateTime.parse("07/06/2020 12:15", DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))
        val l6 = LocalDateTime.parse("03/06/2020 10:15", DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))
        val l7 = LocalDateTime.parse("03/06/2020 12:15", DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))

        val date0 = Date.from(l0.atZone(ZoneId.systemDefault()).toInstant());
        val date1 = Date.from(l1.atZone(ZoneId.systemDefault()).toInstant());
        val date2 = Date.from(l2.atZone(ZoneId.systemDefault()).toInstant());
        val date3 = Date.from(l3.atZone(ZoneId.systemDefault()).toInstant());
        val date4 = Date.from(l4.atZone(ZoneId.systemDefault()).toInstant());
        val date5 = Date.from(l5.atZone(ZoneId.systemDefault()).toInstant());
        val date6 = Date.from(l6.atZone(ZoneId.systemDefault()).toInstant())
        val date7 = Date.from(l7.atZone(ZoneId.systemDefault()).toInstant());

        val cal1 = CalendarItem(null, null, Timestamp(date0), null, date0, null, "Pole dance", "", "Marta Polak", "", "BASIC")

        val cal2 = CalendarItem(null, null, Timestamp(date1), null, date1, null, "Pole dance", "", "Marta Polak", "", "ADDITIONAL")

        val cal3 = CalendarItem(null, null, Timestamp(date2), Timestamp(date3), date2, date3, "Pole dance", "Stretching", "Marta Polak", "Marta Polak", "CATCH_UP")

        val cal4 = CalendarItem(null, null, Timestamp(date3), Timestamp(date2), date3, date2, "Stretching", "Pole dance", "Marta Polak", "Marta Polak", "MISSED_CATCH_UP")

        val cal5 = CalendarItem(null, null, Timestamp(date4), null, date4, null, "Pole dance", "", "Marta Polak", "", "MISSED")

        val cal6 = CalendarItem(null, null, Timestamp(date6), null, date6, null, "Pole dance", "", "Marta Polak", "", "EXCUSED")

        val cal7 = CalendarItem(null, null, Timestamp(date7), null, date7, null, "Pole dance", "", "Marta Polak", "", "LOST")

        calendarClassesList.add(cal1)
        calendarClassesList.add(cal2)
        calendarClassesList.add(cal3)

        calendarClassesList.add(cal4)
        calendarClassesList.add(cal5)
        calendarClassesList.add(cal6)
        calendarClassesList.add(cal7)

        adapter = CalendarAdapter(activity!!.applicationContext, calendarClassesList)

        val currentDay: Calendar = Calendar.getInstance()
        val currentDateString: String = currentDay.get(Calendar.MONTH).toString() +"/"+currentDay.get(Calendar.MONTH).toString()+
                                        "/"+currentDay.get(Calendar.YEAR).toString()
        calendarList.adapter = adapter

        updateCalendarList(currentDateString)
        
        calendarView.setOnDateChangedListener { _: MaterialCalendarView, calendarDay: CalendarDay, _: Boolean ->
            val dateString: String = calendarDay.day.toString()+"/"+calendarDay.month.toString()+"/"+calendarDay.year.toString()
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
