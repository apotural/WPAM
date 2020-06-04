package com.poturalakonieczka.hellyeeeah.layoutCalendar.decorators

import android.text.style.LineBackgroundSpan
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade
import java.util.*


class EventDotDecorator(private val color: Int, private val spanType: Int, private var dates: HashSet<CalendarDay?>): DayViewDecorator {
    private val xOffsets = intArrayOf( -15, -5, 5, 15, -10, 0 , 10)
    private val yOffsets = intArrayOf( 0,0 ,0,0,  -10, -10, -10)

    fun addDate(day: CalendarDay?): Boolean {
        return dates.add(day)
    }

    fun removeDate(day: CalendarDay?): Boolean {
        return dates.remove(day)
    }

    override fun shouldDecorate(day: CalendarDay): Boolean {
        return dates.contains(day)
    }

    override fun decorate(view: DayViewFacade) {
        val span: LineBackgroundSpan =
            CustomDotSpan(
                6F,
                color,
                xOffsets[spanType],
                yOffsets[spanType]
            )
        view.addSpan(span)
    }
}