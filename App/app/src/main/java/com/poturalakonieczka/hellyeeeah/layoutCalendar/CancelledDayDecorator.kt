package com.poturalakonieczka.hellyeeeah.layoutCalendar


import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import com.poturalakonieczka.hellyeeeah.R
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade
import java.util.HashSet


class CancelledDayDecorator(): DayViewDecorator  {
    private var dates: HashSet<CalendarDay?> = hashSetOf()

    fun addDate(day: CalendarDay?): Boolean {
        return dates.add(day)
    }

    fun removeDate(day: CalendarDay?): Boolean {
        return dates.remove(day)
    }

    fun contains(day: CalendarDay):Boolean {
        return dates.contains(day);
    }
    override fun shouldDecorate(day: CalendarDay): Boolean {
        return dates.contains(day);
    }

    override fun decorate(view: DayViewFacade) {

        view.addSpan(StyleSpan(Typeface.BOLD))
        view.addSpan(ForegroundColorSpan(Color.RED))
    }

}