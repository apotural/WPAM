package com.poturalakonieczka.hellyeeeah.layoutClasses

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.google.firebase.Timestamp
import com.poturalakonieczka.hellyeeeah.R
import android.icu.text.SimpleDateFormat
import android.icu.util.ULocale
import android.util.Log

class CanceledAdapter(var context: Context, var list: List<Timestamp?> ): BaseAdapter() {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var convertView = convertView
        if(convertView == null){
            val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            convertView = inflater.inflate(R.layout.canceled_item, null)
        }
        var textDay: TextView = convertView!!.findViewById(R.id.canceled_day)
        var textDate: TextView = convertView!!.findViewById(R.id.canceled_date)

        var dayFormat = SimpleDateFormat("EEEE", ULocale.getDefault())
        var dateFormat = SimpleDateFormat("MM-dd-yyyy")
        var date = list[position]!!.toDate()
        textDate.text = dateFormat.format(date)
        textDay.text = dayFormat.format(date)
        Log.d("MU", textDay.text.toString())
        return convertView
    }

    override fun getItem(position: Int): Timestamp? {
        return list[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return list.size
    }
}