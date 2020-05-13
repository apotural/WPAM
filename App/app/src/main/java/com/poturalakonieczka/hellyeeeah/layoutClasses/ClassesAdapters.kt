package com.poturalakonieczka.hellyeeeah.layoutClasses

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.google.firebase.Timestamp
import com.poturalakonieczka.hellyeeeah.R

class ClassesAdapters(var context:Context, var arrayList: MutableList<Timestamp> ): BaseAdapter() {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view:View = View.inflate(context, R.layout.card_view_classes, null)
        var date:TextView = view.findViewById(R.id.dateClasses)
        date.text = arrayList.get(position).toString()
        return view!!
    }

    override fun getItem(position: Int): Any {
        return arrayList.get(position)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return arrayList.size
    }
}