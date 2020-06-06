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

class CanceledAdapter(var context: Context, var mutableList: MutableList<Timestamp?> ): BaseAdapter() {
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
        var date = mutableList[position]!!.toDate()
        textDate.text = dateFormat.format(date)
        textDay.text = dayFormat.format(date)
        return convertView
    }

    override fun getItem(position: Int): Timestamp? {
        return mutableList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return mutableList.size
    }
}