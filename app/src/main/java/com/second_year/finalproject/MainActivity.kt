package com.second_year.finalproject

import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.second_year.finalproject.Fragments.FragmentAnnouncement
import com.second_year.finalproject.Fragments.FragmentNotifications
import com.second_year.finalproject.Fragments.FragmentSchedule


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // DrawerLayout and NavigationView var
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navigationView: NavigationView = findViewById(R.id.navigation_view)

        // Toolbar for opening and closing nav menu
        var toolbar : Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        val actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.setHomeAsUpIndicator(R.drawable.baseline_add_circle_24)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction().replace(R.id.main_content, FragmentSchedule()).commit()
            navigationView.setCheckedItem(R.id.nav_schedule)
        }

        // Setting ids for icons for the user to go to specific fragments
        navigationView.setNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_schedule -> {
                    supportFragmentManager.beginTransaction().replace(R.id.main_content, FragmentSchedule()).commit()
                }
                R.id.nav_notifications -> {
                    supportFragmentManager.beginTransaction().replace(R.id.main_content, FragmentNotifications()).commit()
                }
                R.id.nav_announcements -> {
                    supportFragmentManager.beginTransaction().replace(R.id.main_content, FragmentAnnouncement()).commit()
                }
            }
            drawerLayout.closeDrawer(GravityCompat.START)
            true
        }

        toolbar.setNavigationOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }
    }

    override fun onOptionsItemSelected(item: android.view.MenuItem): Boolean {
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        if (item.itemId == android.R.id.home) {
            drawerLayout.openDrawer(GravityCompat.START)
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}