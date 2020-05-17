package com.poturalakonieczka.hellyeeeah.layoutClasses

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.google.firebase.Timestamp
import com.poturalakonieczka.hellyeeeah.R

class ClassesAdapter(var context: Context, var mutableList: MutableList<Timestamp?> ): BaseAdapter() {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
        var convertView = convertView
        if(convertView == null){
            val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            convertView = inflater.inflate(R.layout.card_view_classes, null)
        }
        var date: TextView = convertView!!.findViewById(R.id.dateClasses)
        date.text = mutableList[position].toString()
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