package com.poturalakonieczka.hellyeeeah.layoutCalendar.decorators

import android.graphics.Canvas
import android.graphics.Paint
import android.text.style.LineBackgroundSpan


class CustomDotSpan(private val radius: Float , private val color: Int, private val offsetX: Int, private val offsetY: Int) : LineBackgroundSpan {

    override fun drawBackground(
        canvas: Canvas,
        paint: Paint,
        left: Int,
        right: Int,
        top: Int,
        baseline: Int,
        bottom: Int,
        text: CharSequence?,
        start: Int,
        end: Int,
        lnum: Int
    ) {
        val oldColor = paint.color
        if (color != 0) {
            paint.color = color
        }
        val x = (left + right) / 2
        canvas.drawCircle((x + offsetX).toFloat(), bottom - offsetY + radius, radius, paint)
        paint.color = oldColor
    }

}