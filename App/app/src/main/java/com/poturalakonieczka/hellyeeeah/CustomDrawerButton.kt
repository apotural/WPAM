package com.poturalakonieczka.hellyeeeah

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.R
import com.google.android.material.floatingactionbutton.FloatingActionButton


class CustomDrawerButton(context: Context, attrs:AttributeSet?,defStyleAttr: Int ): FloatingActionButton(context, attrs, defStyleAttr),  DrawerLayout.DrawerListener {
    private var mDrawerLayout: DrawerLayout? = null
    private val side = Gravity.LEFT

    constructor(context: Context, attrs:AttributeSet?) : this(context, attrs,  R.style.Widget_Design_FloatingActionButton ) {

    }

    constructor(context: Context) : this(context, null,  R.style.Widget_Design_FloatingActionButton ) {

    }


    fun changeState() {
        if (mDrawerLayout!!.isDrawerOpen(side)) {
            mDrawerLayout!!.closeDrawer(side)
        } else {
            mDrawerLayout!!.openDrawer(side)
        }
    }

    override fun onDrawerStateChanged(newState: Int) {

    }

    override fun onDrawerSlide(drawerView: View, slideOffset: Float) {

    }

    override fun onDrawerClosed(drawerView: View) {

    }

    override fun onDrawerOpened(drawerView: View) {

    }

    fun getDrawerLayout(): DrawerLayout? {
        return mDrawerLayout
    }

    fun setDrawerLayout(mDrawerLayout: DrawerLayout?): CustomDrawerButton? {
        this.mDrawerLayout = mDrawerLayout
        return this
    }
}