package com.poturalakonieczka.hellyeeeah.layoutCalendar


import android.content.Context
import android.graphics.drawable.Drawable
import com.poturalakonieczka.hellyeeeah.R
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade


class CurrentDayDecorator( context: Context): DayViewDecorator  {
    private var date: CalendarDay? = null
    var drawable: Drawable

    init {
        date = CalendarDay.today()
        drawable = context.getDrawable(R.drawable.current_day_selector)!!
    }

    override fun shouldDecorate(day: CalendarDay): Boolean {
        return day == date;
    }

    override fun decorate(view: DayViewFacade) {
        view.setBackgroundDrawable(drawable)
    }

}