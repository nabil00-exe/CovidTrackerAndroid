package com.projet.covidtracker.activity

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.projet.covidtracker.R
import com.projet.covidtracker.api.RetrofitClient
import com.projet.covidtracker.models.DefaultResponse
import com.projet.covidtracker.models.Locations
import com.projet.covidtracker.models.User
import com.projet.covidtracker.storage.SharedPrefCheckin
import com.projet.covidtracker.storage.SharedPrefManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDateTime

class ConfirmCheckInActivity : AppCompatActivity() {


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_confirm_check_in)

        val user : User = SharedPrefManager.getInstance(this).user;
        val userId = user.id.toString()

        val locId=intent.getStringExtra("location_id").toString()

        val locationName = findViewById<TextView>(R.id.tvLocationName)

        RetrofitClient.instance.getLocationById(locId).enqueue(object :
            Callback<Locations>{
            override fun onResponse(call: Call<Locations>, response: Response<Locations>) {

                locationName.setText(response.body()?.name)

            }

            override fun onFailure(call: Call<Locations>, t: Throwable) {
            }

        })

        var checkinTime: LocalDateTime? = null
        val tvConfirmCheckIn = findViewById<TextView>(R.id.tvconfirmCheckIn)
        val btnCheckIn = findViewById<Button>(R.id.btnConfirmLocation)
        val btnCheckOut = findViewById<Button>(R.id.btnCheckOut)

        var locCap:Int = 0

        btnCheckOut.visibility = View.INVISIBLE

        btnCheckIn.setOnClickListener {

            RetrofitClient.instance.getLocationById(locId).enqueue(object :
                Callback<Locations>{
                override fun onResponse(call: Call<Locations>, response: Response<Locations>) {

                    if(response.body()?.capacity ==0){
                        val builder = AlertDialog.Builder(this@ConfirmCheckInActivity)
                        builder.setMessage("This location has reached the maximum capacity you can't Check In")
                            .setCancelable(false)
                            .setNegativeButton("OK") { dialog, id ->
                                val intent = Intent(this@ConfirmCheckInActivity, ApplicationActivity::class.java)
                                startActivity(intent)
                                dialog.dismiss()
                            }
                        val alert = builder.create()
                        alert.show()
                    }
                    else{
                        locCap = response.body()?.capacity!! - 1

                        RetrofitClient.instance.updateLocation(locId, locCap).enqueue(object : Callback<DefaultResponse>{
                            override fun onResponse(
                                call: Call<DefaultResponse>,
                                response: Response<DefaultResponse>
                            ) {
                                Toast.makeText(applicationContext, "Check In", Toast.LENGTH_LONG).show()
                            }

                            override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                            }

                        })

                        checkinTime = LocalDateTime.now()

                        SharedPrefCheckin.getInstance(applicationContext).saveCheckin(userId,locId,checkinTime.toString())

                        val intent = Intent(applicationContext, ApplicationActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

                        startActivity(intent)
                    }

                }

                override fun onFailure(call: Call<Locations>, t: Throwable) {
                }
            })
        }
 /*       btnCheckOut.setOnClickListener {
            val checkoutTime= LocalDateTime.now()
            RetrofitClient.instance.addCheckin(userId, locId,checkinTime,checkoutTime)
                .enqueue(object : Callback<DefaultResponse>{
                    override fun onResponse(
                        call: Call<DefaultResponse>,
                        response: Response<DefaultResponse>
                    ) {
                        locCap +=1

                        RetrofitClient.instance.updateLocation(locId, locCap).enqueue(object : Callback<DefaultResponse>{
                            override fun onResponse(
                                call: Call<DefaultResponse>,
                                response: Response<DefaultResponse>
                            ) {
                                Toast.makeText(applicationContext, "Check Out", Toast.LENGTH_LONG).show()
                            }

                            override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                            }
                        })
                    }
                    override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                    }
                })
            val intent = Intent(this, ApplicationActivity::class.java)
            startActivity(intent)
            btnCheckOut.visibility = View.INVISIBLE
            btnCheckIn.visibility = View.VISIBLE
        }*/
    }
}


