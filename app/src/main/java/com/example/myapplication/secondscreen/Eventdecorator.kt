package com.example.myapplication.secondscreen

import android.graphics.Color
import android.media.metrics.Event
import android.util.Log
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade
import com.prolificinteractive.materialcalendarview.spans.DotSpan

class EventDecorator(dates: Collection<CalendarDay>, colorString : String): DayViewDecorator {
    //TODO 날짜들과 색을 지정해주기 위함
    val colorString : String = colorString
    val dates: HashSet<CalendarDay> = HashSet(dates)

    override fun shouldDecorate(day: CalendarDay?): Boolean {
        return dates.contains(day)
    }

    override fun decorate(view: DayViewFacade?) {
        view?.addSpan(DotSpan(10F, Color.parseColor(colorString)))

        Log.d("decorate called", dates.toString())
    }
}