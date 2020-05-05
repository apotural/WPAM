package com.poturalakonieczka.hellyeeeah

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.VectorDrawable
import android.os.Bundle
import android.view.View
import android.widget.AdapterView.OnItemClickListener
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import kotlinx.android.synthetic.main.user_main.*


class UserActivity : AppCompatActivity(){

    private var drawerToggle: ActionBarDrawerToggle? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.user_main)

        val userDataFragment = UserDataFragment.newInstance();

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.main_content, userDataFragment)
                .commitNow()
        }
        setupNavigationItems();
        setupDrawerAndToggle();
        showBookingList();
    }

    private fun setupDrawerAndToggle() {
        setSupportActionBar(main_toolbar)

        if (supportActionBar != null) {

            supportActionBar!!.setDisplayShowTitleEnabled(false)
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        }
        drawerToggle = object : ActionBarDrawerToggle(this, main_drawer, main_toolbar, 0, 0) {
            override fun onDrawerOpened(drawerView: View) {
                super.onDrawerOpened(drawerView)
                isDrawerIndicatorEnabled = true
            }

            override fun onDrawerClosed(drawerView: View) {
                super.onDrawerClosed(drawerView)
            }

        }
        main_drawer.addDrawerListener(drawerToggle as ActionBarDrawerToggle)
        (drawerToggle as ActionBarDrawerToggle).isDrawerIndicatorEnabled = false; //disable "hamburger to arrow" drawable
        (drawerToggle as ActionBarDrawerToggle).setHomeAsUpIndicator(R.drawable.ic_logo); //set your own
        (drawerToggle as ActionBarDrawerToggle).syncState()

    }

    private fun setupNavigationItems() {
        val navigationItems =
            mutableListOf<Int?>(R.drawable.ic_person, R.drawable.ic_classes,R.drawable.ic_group)
        val mAdapter =
            IconAdapter(this, navigationItems)
        main_navigation.adapter = mAdapter

        main_navigation.onItemClickListener = OnItemClickListener { _, _, position, _ ->
            when (position) {
                0 -> showBookingList()
                1 -> showRandom()
                2 -> showSettings()
                else -> {
                }
            }
            main_drawer.closeDrawer(main_navigation)
        }
    }

    private fun showSettings() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.main_content, UserDataFragment.newInstance())
            .commitNow()
    }

    private fun showRandom() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.main_content, UserDataFragment.newInstance())
            .commitNow()
    }

    private fun showBookingList() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.main_content, UserDataFragment.newInstance())
            .commitNow()
    }

    fun getDrawer(): DrawerLayout? {
        return main_drawer
    }

    fun getDrawerToggle(): ActionBarDrawerToggle? {
        return drawerToggle
    }

}


//        drawerLayout = findViewById(R.id.drawer_layout)
//        drawerLayout?.setScrimColor(Color.TRANSPARENT)
//        linearLayout = findViewById(R.id.left_drawer)
//        linearLayout?.setOnClickListener { drawerLayout?.closeDrawers() }
//        nav_button.setOnClickListener { drawerLayout?.openDrawer(left_drawer) }
//
//
//        viewAnimator = ViewAnimator(
//            this,
//            list,
//            userDataFragment,
//            drawerLayout,
//            this
//        )
//        nav_button.setDrawerLayout(drawerLayout)
//        nav_button.getDrawerLayout()!!.addDrawerListener(nav_button)
//        nav_button.setViewAnimator(viewAnimator)
//        nav_button.setLinearLayout(linearLayout)
//        nav_button.setOnClickListener(View.OnClickListener { nav_button.changeState() })

        //  setActionBar()
        // createMenuList()
    //}



/*
    private fun setActionBar() {
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(nav_button)
        supportActionBar!!.setHomeButtonEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.hide()
        drawerToggle = object : ActionBarDrawerToggle(
            this,  /* host Activity */
            drawerLayout,  /* DrawerLayout object */
            toolbar,  /* nav drawer icon to replace 'Up' caret */
            R.string.drawer_open,  /* "open drawer" description */
            R.string.drawer_close /* "close drawer" description */
        ) {
            override fun onDrawerClosed(drawerView: View) {
                super.onDrawerClosed(drawerView)
                linearLayout!!.removeAllViews()
                linearLayout?.invalidate()
            }

            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {
                super.onDrawerSlide(drawerView, slideOffset)
                if (slideOffset > 0.6 && linearLayout!!.childCount == 0) viewAnimator!!.showMenuContent()
            }

            override fun onDrawerOpened(drawerView: View) {
                super.onDrawerOpened(drawerView)
            }

        }
        drawerLayout!!.addDrawerListener(drawerToggle as ActionBarDrawerToggle)
    }
*/
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        if (drawerToggle!!.onOptionsItemSelected(item)) {
//            return true;
//        }
//        return super.onOptionsItemSelected(item)
//    }
//
//    private fun createMenuList() {
//        val menuItem0 = SlideMenuItem("Close", R.drawable.ic_close)
//        list.add(menuItem0)
//        val menuItem1 = SlideMenuItem("Person", R.drawable.ic_person)
//        list.add(menuItem1)
//        val menuItem2 = SlideMenuItem("Calendar", R.drawable.ic_calendar2)
//        list.add(menuItem2)
//        val menuItem3 = SlideMenuItem("Group", R.drawable.ic_group)
//        list.add(menuItem3)
//        val menuItem4 = SlideMenuItem("Classes", R.drawable.ic_classes)
//        list.add(menuItem4)
//    }

//    override fun onPostCreate(savedInstanceState: Bundle?) {
//        super.onPostCreate(savedInstanceState)
//        drawerToggle!!.syncState()
//    }
//
//
//    override fun onConfigurationChanged(newConfig: Configuration) {
//        super.onConfigurationChanged(newConfig)
//        drawerToggle!!.onConfigurationChanged(newConfig)
//    }

//    private fun animate(screenShotable: ScreenShotable, topPosition: Int){
//        val view: View = findViewById(R.id.content_frame)
//        val finalRadius = Math.max(view.width, view.height)
//        val animator =
//            ViewAnimationUtils.createCircularReveal(view, 0, topPosition, 0.0F, finalRadius.toFloat())
//        animator.interpolator = AccelerateInterpolator()
//        animator.duration = ViewAnimator.CIRCULAR_REVEAL_ANIMATION_DURATION.toLong()
//        animator.start()
//    }
//
//    private fun replacePersonFragment(screenShotable: ScreenShotable,  topPosition: Int  ): ScreenShotable? {
//        animate(screenShotable, topPosition)
//        val userDataFragment = UserDataFragment()
//        supportFragmentManager.beginTransaction()
//            .replace(R.id.content_frame, userDataFragment).commit()
//        return userDataFragment
//    }
//
//    private fun replaceCalendarFragment(screenShotable: ScreenShotable,  topPosition: Int  ): ScreenShotable? {
//        animate(screenShotable, topPosition)
//        val calendarFragment = CalendarFragment()
//        supportFragmentManager.beginTransaction()
//            .replace(R.id.content_frame, calendarFragment).commit()
//        return calendarFragment
//    }
//
//    private fun replaceGroupFragment(screenShotable: ScreenShotable,  topPosition: Int  ): ScreenShotable? {
//        animate(screenShotable, topPosition)
//        val groupFragment = GroupFragment()
//        supportFragmentManager.beginTransaction()
//            .replace(R.id.content_frame, groupFragment).commit()
//        return groupFragment
//    }
//
//    private fun replaceClassesFragment(screenShotable: ScreenShotable,  topPosition: Int  ): ScreenShotable? {
//        animate(screenShotable, topPosition)
//        val classFragment = ClassFragment()
//        supportFragmentManager.beginTransaction()
//            .replace(R.id.content_frame, classFragment).commit()
//        return classFragment
//    }
//
//    override fun onSwitch(
//        slideMenuItem: Resourceble,
//        screenShotable: ScreenShotable,
//        position: Int
//    ): ScreenShotable? {
//        return when (slideMenuItem.name) {
//            "Close"-> replacePersonFragment(screenShotable, position)
//            "Person" -> replacePersonFragment(screenShotable, position)
//            "Calendar" -> replaceCalendarFragment(screenShotable, position)
//            "Group" -> replaceGroupFragment(screenShotable, position)
//            else  -> replaceClassesFragment(screenShotable, position) //CLASSES
//        }
//    }
//
//    override fun disableHomeButton() {
//        supportActionBar!!.setHomeButtonEnabled(false)
//    }
//
//    override fun enableHomeButton() {
//        supportActionBar!!.setHomeButtonEnabled(true)
//        drawerLayout!!.closeDrawers()
//    }
//
//    override fun addViewToContainer(view: View?) {
//        linearLayout!!.addView(view)
//    }


//
//}