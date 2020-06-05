package com.poturalakonieczka.hellyeeeah.layoutClasses

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.poturalakonieczka.hellyeeeah.R
import com.poturalakonieczka.hellyeeeah.database.Grupa
import android.icu.text.SimpleDateFormat
import android.util.Log

class GroupsAdapter(var context: Context, var list: List<Grupa?> ): BaseAdapter() {
    private val _TAG = "My-log GroupsAdapter"
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var convertView = convertView
        if(convertView == null){
            val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            convertView = inflater.inflate(R.layout.group_item, null)
        }
        Log.d(_TAG, "getting")
        var textName: TextView? = convertView?.findViewById(R.id.class_type)
        textName?.text = list[position]?.rodzaj?.toUpperCase()
        var textLevel :TextView? = convertView?.findViewById(R.id.level_body)
        textLevel?.text = list[position]?.poziom.toString()
        var textDay : TextView? = convertView?.findViewById(R.id.time_day)
        textDay?.text = list[position]?.dzien
        var textHour :TextView? = convertView?.findViewById(R.id.time_hour)
        var dateFormat = SimpleDateFormat("HH:mm")
        var date = list[position]!!.godzina.toDate()
        textHour?.text = dateFormat.format(date)
        var texTeacher : TextView? = convertView?.findViewById(R.id.teacher_body)
        texTeacher?.text = list[position]?.prowadzaca
        Log.d(_TAG, list[position]?.rodzaj+" "+list[position]?.prowadzaca)
        return convertView!!
    }

    override fun getItem(position: Int): Grupa? {
        return list[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return list.size
    }
}