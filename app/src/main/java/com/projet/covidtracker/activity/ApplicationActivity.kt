package com.projet.covidtracker.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.projet.covidtracker.R
import com.projet.covidtracker.activity.fragments.*
import com.projet.covidtracker.models.User
import com.projet.covidtracker.storage.SharedPrefCheckin
import com.projet.covidtracker.storage.SharedPrefManager

class ApplicationActivity : AppCompatActivity() {

    private val profileFragment = ProfileFragment()
    private val reclamationFragment = ReclamationFragment()
    private val checkinFragment = CheckinFragment()
    private val mapFragment = MapFragment()
    private val statsFragment = StatsFragment()



    override fun onCreate(savedInstanceState: Bundle?) {

        val user : User = SharedPrefManager.getInstance(this).user;

        val name = user.name.toString()
        val email = user.email.toString()
        val userId = user.id.toString()
        val passqr = user.passqr.toString()

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_application)
        replaceFragment(checkinFragment)

        findViewById<BottomNavigationView>(R.id.bottom_navigation).setOnItemSelectedListener{
            when(it.itemId){
                R.id.ic_checkin -> replaceFragment(checkinFragment)
                R.id.ic_user -> replaceFragment(profileFragment)
                R.id.ic_stats -> replaceFragment(statsFragment)
                R.id.ic_reclamation -> replaceFragment(reclamationFragment)
                R.id.ic_map -> replaceFragment(mapFragment)
            }
            true
        }



    }

    private fun replaceFragment(fragment: Fragment){
        if(fragment!=null){
            val transaction = supportFragmentManager.beginTransaction()

            transaction.replace(R.id.fragment_container, fragment)
            transaction.commit()
        }
    }

    override fun onStart() {
        super.onStart()

        if(!SharedPrefManager.getInstance(this).isLoggedIn){
            val intent = Intent(applicationContext, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }

        if (SharedPrefCheckin.getInstance(applicationContext).isCheckedIn){
            val intent = Intent(applicationContext, CheckOutActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }

    }

}