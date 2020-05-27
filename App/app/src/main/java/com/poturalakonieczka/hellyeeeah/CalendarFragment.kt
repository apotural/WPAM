package com.poturalakonieczka.hellyeeeah

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.firebase.Timestamp
import com.poturalakonieczka.hellyeeeah.database.ZajeciaOdwolane
import com.poturalakonieczka.hellyeeeah.layoutCalendar.*
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.CalendarMode
import kotlinx.android.synthetic.main.user_calendar_fragment.*
import java.text.SimpleDateFormat

import java.util.*


class CalendarFragment: Fragment(){
    /* green yellow purple red pink blue grey*/
    private enum class ClassType {
        BASIC, ADDITIONAL, CATCH_UP, MISSED,  MISSED_CATCH_UP, EXCUSED, LOST
    }

    private val TAG = "My-deb calendar"

    private val yearFormat = SimpleDateFormat("yyyy", Locale.getDefault())
    private val monthFormat = SimpleDateFormat("MM", Locale.getDefault())
    private val dayFormat = SimpleDateFormat("dd", Locale.getDefault())

    private var previousCancelledClasses: ZajeciaOdwolane = ZajeciaOdwolane()
    private var previousAdditionalClasses: MutableList<ClassInCalendar?> = mutableListOf()
    private var previousAbsentClasses: MutableList<ClassInCalendar?> = mutableListOf()
    private var previousBasicClasses: MutableList<BasicClassInCalendar?> = mutableListOf()

    private var listOfDecorator: MutableList<EventDotDecorator?> = mutableListOf()
    private var cancelledDecorator: CancelledDayDecorator = CancelledDayDecorator()

    companion object {
        fun newInstance() = CalendarFragment()
    }

    data class RAC_Lists(val remove: List<ClassInCalendar?>, val add:  List<ClassInCalendar?>, val common: Set<ClassInCalendar?>)

    private fun createRemoveAddedCommon(currentList : List<ClassInCalendar?>, previousList: List<ClassInCalendar?>) : RAC_Lists {
        val removed = previousList.minus(currentList)
        val added = currentList.minus(previousList)
        val common = currentList.intersect(previousList).plus(added)
        return RAC_Lists(removed, added, common)
    }

    private fun compareTwoDates( dateOne: Date, dateTwo: Date):Boolean {
        return ((dayFormat.format(dateOne).toInt() == dayFormat.format(dateTwo).toInt() ) and
                (monthFormat.format(dateOne).toInt() == monthFormat.format(dateTwo).toInt() ) and
                (yearFormat.format(dateOne).toInt() == yearFormat.format(dateTwo).toInt() ))
    }

    private fun checkOtherClasses(setToCheck: Set<ClassInCalendar?>, removeDate: Date, s: String): Boolean {
        var date: Date? = null
        for(oneClass in setToCheck){
            if (s == "ADDITIONAL"){
                date = oneClass?.dateN?.toDate()
            }
            if(date != null){
                if(compareTwoDates(date, removeDate)) return true
            }
        }
        return false
    }

    private fun showAdditionalDates(additional: List<ClassInCalendar?>) {
        val (removed, added, common) = createRemoveAddedCommon(additional, previousAdditionalClasses)

        if(removed.isNotEmpty() || added.isNotEmpty()){
            removeDecorators()

            for (remove in removed){
                val date: Date = (remove?.dateN as Timestamp).toDate()
                val calendarDay = CalendarDay.from(yearFormat.format(date).toInt(), monthFormat.format(date).toInt() , dayFormat.format(date).toInt())
                if(!checkOtherClasses(common, date, "ADDITIONAL"))
                    listOfDecorator[ClassType.ADDITIONAL.ordinal]?.removeDate(calendarDay)
            }

            for (add in added){
                val date: Date = (add?.dateN as Timestamp).toDate()
                val calendarDay = CalendarDay.from(yearFormat.format(date).toInt(), monthFormat.format(date).toInt() , dayFormat.format(date).toInt())
                if(!cancelledDecorator.contains(calendarDay)){
                    listOfDecorator[ClassType.ADDITIONAL.ordinal]?.addDate(calendarDay)
                }
            }

            addDecorators()
            previousAdditionalClasses = additional as MutableList<ClassInCalendar?>
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.user_calendar_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        initCalendarView()
        UserActivity.viewModel.cancelledClassesCalendar.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            //if (it != null) showCancelledDates(it)
            if (it != null) previousCancelledClasses = it
        })
        UserActivity.viewModel.additionalClassesCalendar.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            if (it != null) showAdditionalDates(it)
            if (it != null) previousAdditionalClasses = it as MutableList<ClassInCalendar?>
            Log.d(TAG, it.toString())
        })

        UserActivity.viewModel.absentClassesCalendar.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            //if (it != null) showAbsentDates(it)
            if (it != null) previousAbsentClasses = it as MutableList<ClassInCalendar?>
        })

        val maxDate = UserActivity.viewModel.getMaxDate()
        val minDate = UserActivity.viewModel.getMinDate()
        UserActivity.viewModel.basicClassesCalendar.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            //if (it != null) showBasicDates(it)
            if (it != null) previousBasicClasses = it as MutableList<BasicClassInCalendar?>
        })

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

        calendarView.addDecorator(CurrentDayDecorator(context!!.applicationContext))
        calendarView.addDecorator(CancelledDayDecorator())

        listOfDecorator.add( EventDotDecorator(ContextCompat.getColor(context!!.applicationContext, R.color.colorDot1), 0, hashSetOf()))
        listOfDecorator.add( EventDotDecorator(ContextCompat.getColor(context!!.applicationContext, R.color.colorDot2), 1, hashSetOf()))
        listOfDecorator.add( EventDotDecorator(ContextCompat.getColor(context!!.applicationContext, R.color.colorDot3), 2, hashSetOf()))
        listOfDecorator.add( EventDotDecorator(ContextCompat.getColor(context!!.applicationContext, R.color.colorDot4), 3, hashSetOf()))
        listOfDecorator.add( EventDotDecorator(ContextCompat.getColor(context!!.applicationContext, R.color.colorDot5), 4, hashSetOf()))
        listOfDecorator.add( EventDotDecorator(ContextCompat.getColor(context!!.applicationContext, R.color.colorDot6), 5, hashSetOf()))
        listOfDecorator.add( EventDotDecorator(ContextCompat.getColor(context!!.applicationContext, R.color.colorDot7), 6, hashSetOf()))


    }


}
