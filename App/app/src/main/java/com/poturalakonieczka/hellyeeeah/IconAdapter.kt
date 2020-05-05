package com.poturalakonieczka.hellyeeeah

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import com.google.android.gms.common.wrappers.Wrappers.packageManager


class IconAdapter(context: Context, list: MutableList<Int?>) :
    ArrayAdapter<Int?>(context, 0, list) {

    private val mContext: Context = context
    private var iconList: List<Int?> = list

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var listItem = convertView ?: LayoutInflater.from(mContext).inflate(R.layout.item, parent, false)

        val currentIcon = iconList[position]
        val image: ImageView = listItem.findViewById(R.id.icon_item) as ImageView
        if (currentIcon != null) {
            image.setImageResource(currentIcon)
        }

        return listItem
    }

}