package com.projet.covidtracker.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.projet.covidtracker.R
import com.projet.covidtracker.api.RetrofitClient
import com.projet.covidtracker.models.CheckinInt
import com.projet.covidtracker.models.DefaultResponse
import com.projet.covidtracker.models.Locations
import com.projet.covidtracker.models.User
import com.projet.covidtracker.storage.SharedPrefCheckin
import com.projet.covidtracker.storage.SharedPrefManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDateTime
import java.util.*
import kotlin.concurrent.schedule

class CheckOutActivity : AppCompatActivity() {

    val user : User = SharedPrefManager.getInstance(this).user;
    val ckin : CheckinInt = SharedPrefCheckin.getInstance(this).checkinInt;

    val name = user.name.toString()
    val email = user.email.toString()
    val userId = user.id.toString()
    val passqr = user.passqr.toString()

    val locId = ckin.location
    val checkinTime = ckin.checkin

    var locCap:Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_check_out)

        val locationName = findViewById<TextView>(R.id.tvLocName)

        RetrofitClient.instance.getLocationById(locId).enqueue(object :
            Callback<Locations> {
            override fun onResponse(call: Call<Locations>, response: Response<Locations>) {

                locationName.setText(response.body()?.name)
                locCap = response.body()?.capacity!! + 1

            }

            override fun onFailure(call: Call<Locations>, t: Throwable) {
            }
        })

        val btnCheckOut = findViewById<Button>(R.id.btnCheckOutConf)

        btnCheckOut.setOnClickListener {
            val checkoutTime= LocalDateTime.now()
            RetrofitClient.instance.addCheckin(userId, locId,checkinTime,checkoutTime.toString())
                .enqueue(object : Callback<DefaultResponse>{
                    override fun onResponse(
                        call: Call<DefaultResponse>,
                        response: Response<DefaultResponse>
                    ) {

                        RetrofitClient.instance.updateLocation(locId, locCap).enqueue(object : Callback<DefaultResponse>{
                            override fun onResponse(
                                call: Call<DefaultResponse>,
                                response: Response<DefaultResponse>
                            ) {
                                Toast.makeText(applicationContext, "Check Out", Toast.LENGTH_LONG).show()
                                SharedPrefCheckin.getInstance(this@CheckOutActivity).clear()
                            }

                            override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                            }
                        })
                    }
                    override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                    }
                })
            val intent = Intent(this, LoginActivity::class.java)

            Timer("SettingUp", false).schedule(500) {
                startActivity(intent)
            }



        }

    }
}