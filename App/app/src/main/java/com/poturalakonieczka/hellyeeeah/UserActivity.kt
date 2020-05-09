package com.poturalakonieczka.hellyeeeah

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import com.google.android.material.navigation.NavigationView
import com.google.firebase.firestore.auth.User
import kotlinx.android.synthetic.main.user_main.*
import java.security.acl.Group


class UserActivity : AppCompatActivity() {
    private var toggle: ActionBarDrawerToggle? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.user_main)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, UserDataFragment.newInstance())
                .commitNow()
        }

        toggle = ActionBarDrawerToggle(this, drawer_layout, R.string.Open, R.string.Close)
        drawer_layout.addDrawerListener(toggle!!)
        toggle?.syncState()
        nav_view.setNavigationItemSelectedListener(object :
            NavigationView.OnNavigationItemSelectedListener {
            override fun onNavigationItemSelected(item: MenuItem): Boolean {
                when (item.itemId) {
                    R.id.classes -> {
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.container, ClassFragment.newInstance()).commit()
                    }
                    R.id.person -> {
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.container, UserDataFragment.newInstance()).commit()
                    }
                    R.id.calendar -> {
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.container, CalendarFragment.newInstance()).commit()
                    }
                    R.id.group -> {
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.container, GroupFragment.newInstance()).commit()
                    }
                    else -> return true
                }
                drawer_layout.closeDrawers()
                return true
            }
        })
        nav_view.setItemIconSize(90)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return if (toggle!!.onOptionsItemSelected(item)) true else super.onOptionsItemSelected(item)
    }


}