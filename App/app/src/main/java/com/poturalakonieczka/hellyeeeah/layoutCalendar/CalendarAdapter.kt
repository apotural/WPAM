package com.poturalakonieczka.hellyeeeah.layoutCalendar

import android.annotation.SuppressLint
import android.content.Context
import android.database.DataSetObserver
import android.graphics.Paint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import com.poturalakonieczka.hellyeeeah.R
import kotlinx.android.synthetic.main.user_calendar_fragment.*
import java.text.SimpleDateFormat
import java.util.*


class CalendarAdapter (private val appContext: Context, private val calendarList: List<CalendarItem?>):
    ArrayAdapter<CalendarItem>(appContext, 0, calendarList),
    Filterable {
    private var mCalendarList: List<CalendarItem?> = calendarList
    private var mCalendarDisplayList: List<CalendarItem?> = calendarList

    private val hourFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
    private val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    private val date2Format = SimpleDateFormat("dd/MM/yy HH:mm", Locale.getDefault())

    override fun getCount(): Int {
        return mCalendarDisplayList.size
    }

    override fun getItem(p0: Int): CalendarItem? {
        return mCalendarDisplayList[p0]
    }

    @SuppressLint("SetTextI18n")
    private fun changeViewByClassType(
        convertView: View?,
        calendarItem: CalendarItem?) {
        val type = calendarItem?.type

        if(type != null){
            var strikethrough1 = false
            var strikethrough2 = false
            var colour = ContextCompat.getColor(appContext, R.color.colorDot1)
            val circle: CardView = convertView!!.findViewById(R.id.circle)
            val textView: TextView = convertView!!.findViewById(R.id.type)
            val textType1: TextView = convertView!!.findViewById(R.id.textType1)
            val textDate1: TextView = convertView!!.findViewById(R.id.textDate1)
            val textWho1: TextView = convertView!!.findViewById(R.id.textTeacher1)
            val textType2: TextView = convertView!!.findViewById(R.id.textType2)
            val textDate2: TextView = convertView!!.findViewById(R.id.textDate2)
            val textWho2: TextView = convertView!!.findViewById(R.id.textTeacher2)
            val arrowR: ImageView = convertView.findViewById(R.id.right_arrow)
            val arrowL: ImageView = convertView.findViewById(R.id.left_arrow)
            arrowR.visibility = INVISIBLE
            arrowL.visibility = INVISIBLE
            when (type) {
                "BASIC" -> {
                    colour = ContextCompat.getColor(appContext, R.color.colorDot1)
                    textView.text = "obecność"
                }
                "ADDITIONAL" -> {
                    colour = ContextCompat.getColor(appContext, R.color.colorDot2)
                    textView.text = "dodatkowe"
                }
                "CATCH_UP" -> {
                    colour = ContextCompat.getColor(appContext, R.color.colorDot3)
                    arrowR.visibility = VISIBLE
                    textView.text = "odrabianie"
                    strikethrough2 = true
                }
                "MISSED" -> {
                    colour = ContextCompat.getColor(appContext, R.color.colorDot4)
                    textView.text = "nieuspraw."
                    strikethrough1 = true
                }
                "MISSED_CATCH_UP" -> {
                    colour = ContextCompat.getColor(appContext, R.color.colorDot5)
                    textView.text = "odrobione"
                    arrowL.visibility = VISIBLE
                    strikethrough1 = true
                }
                "EXCUSED" -> {
                    colour = ContextCompat.getColor(appContext, R.color.colorDot6)
                    textView.text = "usprawied."
                    strikethrough1 = true

                }
                "LOST" -> {
                    colour = ContextCompat.getColor(appContext, R.color.colorDot7)
                    textView.text = "niezrealiz."
                    strikethrough1 = true
                }
            }
            circle.setCardBackgroundColor(colour)
            textView.setTextColor(colour)
            if(strikethrough1) {
                textType1.paintFlags = textType1.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                textWho1.paintFlags = textWho1.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                textDate1.paintFlags = textDate1.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            }else{
                textType1.paintFlags = textType1.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
                textWho1.paintFlags = textWho1.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
                textDate1.paintFlags = textDate1.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
            }
            if(strikethrough2){
                textType2.paintFlags = textType2.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                textWho2.paintFlags = textWho2.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                textDate2.paintFlags = textDate2.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            }else{
                textType2.paintFlags = textType2.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
                textWho2.paintFlags = textWho2.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
                textDate2.paintFlags = textDate2.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
            }
        }
    }


    @SuppressLint("SetTextI18n")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        if(convertView == null){
            val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            convertView = inflater.inflate(R.layout.calendar_item, null)
        }

        val textType1: TextView = convertView!!.findViewById(R.id.textType1)
        val textDate1: TextView = convertView.findViewById(R.id.textDate1)
        val textWho1: TextView = convertView.findViewById(R.id.textTeacher1)
        val textType2: TextView = convertView.findViewById(R.id.textType2)
        val textDate2: TextView = convertView.findViewById(R.id.textDate2)
        val textWho2: TextView = convertView.findViewById(R.id.textTeacher2)

        textType1.text = mCalendarDisplayList[position]?.classType1
        textWho1.text = "z: " + mCalendarDisplayList[position]?.withWho1
        textDate1.text  = "o: " + hourFormat.format(mCalendarDisplayList[position]?.date1!!)

        val classType2: String? = mCalendarDisplayList[position]?.classType2
        if ((classType2 != null) and ((classType2 != "") )){
            textType2.visibility = VISIBLE
            textWho2.visibility = VISIBLE
            textDate2.visibility = VISIBLE
            textType2.text = mCalendarDisplayList[position]?.classType2
            textWho2.text = "z: " + mCalendarDisplayList[position]?.withWho2
            textDate2.text  = date2Format.format(mCalendarDisplayList[position]?.date2!!)
        }else{
            textType2.visibility = INVISIBLE
            textWho2.visibility = INVISIBLE
            textDate2.visibility = INVISIBLE
        }
        changeViewByClassType(convertView, mCalendarDisplayList[position])
        return convertView
    }

    override fun getFilter(): Filter {

        return object : Filter() {
            override fun publishResults(charSequence: CharSequence?, filterResults: Filter.FilterResults) {
                mCalendarDisplayList = filterResults.values as List<CalendarItem?>
                notifyDataSetChanged()

            }

            override fun performFiltering(charSequence: CharSequence?): Filter.FilterResults {
                val chosenDate: Date? = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).parse(charSequence.toString())

                val filterResults = Filter.FilterResults()
                filterResults.values = if (chosenDate==null)
                    mCalendarList
                else
                    mCalendarList.filter {
                        if(it == null)
                            false
                        else {
                            dateFormat.format(it.date1) == dateFormat.format(chosenDate)
                        }
                    }

                return filterResults
            }
        }
    }

    fun setList(calendarClassesList: List<CalendarItem?>) {
        mCalendarList = calendarClassesList
    }

}
