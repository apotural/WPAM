package com.poturalakonieczka.hellyeeeah.layoutClasses

import android.content.ClipData.Item
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.google.firebase.Timestamp
import com.poturalakonieczka.hellyeeeah.R

class ClassesAdapters(var context: Context,  var arrayList: MutableList<Timestamp?> ): BaseAdapter() {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
        var convertView = convertView
        if(convertView == null){
            val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            convertView = inflater.inflate(R.layout.card_view_classes, null)
        }
        var date: TextView = convertView!!.findViewById(R.id.dateClasses)
        date.text = arrayList[position].toString()
        return convertView
    }
    override fun getItem(position: Int): Timestamp? {
        return arrayList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return arrayList.size
    }
}