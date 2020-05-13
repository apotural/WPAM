package com.poturalakonieczka.hellyeeeah

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.facebook.FacebookSdk
import com.facebook.login.LoginManager
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.user_main.*


class UserActivity : AppCompatActivity() {
    private var toggle: ActionBarDrawerToggle? = null

    companion object {
        lateinit var viewModel: ModelView
        lateinit var storageView: StorageView
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ModelView::class.java)
        storageView = ViewModelProvider(this).get(StorageView::class.java)
        viewModel.downloadParticipant()

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
                    R.id.logout -> {
                        displayPopUpLogout()
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


    fun displayPopUpLogout(){
        AlertDialog.Builder(this)
            .setTitle("Wyloguj się")
            .setMessage("Czy na pewno chcesz się wylogować??")
            .setPositiveButton("Tak" ) { _, _ ->
                FacebookSdk.sdkInitialize(this)
                FirebaseAuth.getInstance().signOut()
                LoginManager.getInstance().logOut()
                finish()
                val intent = Intent(this@UserActivity, MainActivity::class.java)
                startActivity(intent)
            }
            .setNegativeButton( "Nie", null)
            .show()
    }

}