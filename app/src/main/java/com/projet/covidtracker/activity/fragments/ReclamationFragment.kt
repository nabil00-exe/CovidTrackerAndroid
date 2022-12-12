package com.projet.covidtracker.activity.fragments

import android.app.AlertDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.DatePicker
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.projet.covidtracker.R
import com.projet.covidtracker.activity.ApplicationActivity
import com.projet.covidtracker.api.RetrofitClient
import com.projet.covidtracker.models.Checkin
import com.projet.covidtracker.models.DefaultResponse
import com.projet.covidtracker.models.Locations
import com.projet.covidtracker.models.User
import com.projet.covidtracker.storage.SharedPrefManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDateTime
import java.util.*
import javax.mail.*
import javax.mail.internet.*


/**
 * A simple [Fragment] subclass.
 * Use the [ReclamationFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ReclamationFragment : Fragment() {

    private lateinit var viewOfLayout: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        viewOfLayout = inflater!!.inflate(R.layout.fragment_reclamation, container, false)

        if (Build.VERSION.SDK_INT > 9) {
            val policy = ThreadPolicy.Builder().permitAll().build()
            StrictMode.setThreadPolicy(policy)
        }

        val thiscontext = container!!.getContext();
        val user : User = SharedPrefManager.getInstance(thiscontext).user;

        val name = user.name.toString()
        val email = user.email.toString()
        val userId = user.id.toString()
        val passqr = user.passqr.toString()

        val dateTest =viewOfLayout.findViewById<DatePicker>(R.id.datePickerRec)
        val btnAddRec =viewOfLayout.findViewById<Button>(R.id.btnAddRec)

        var dateReclamation: LocalDateTime? = null

        btnAddRec.setOnClickListener {

            val builder = AlertDialog.Builder(thiscontext)
            builder.setMessage("Confirm this reclamation?")
                .setCancelable(false)
                .setPositiveButton("Yes") { dialog, id ->

                    val testDate = getDateFromDatePicker(dateTest)
                    dateReclamation = LocalDateTime.now()

                    RetrofitClient.instance.addReclamation(userId,testDate,dateReclamation)
                        .enqueue(object : Callback<DefaultResponse> {


                            override fun onResponse(
                                call: Call<DefaultResponse>,
                                response: Response<DefaultResponse>
                            ) {
                                if (response.body()?.message == "reclamation success") {



                                    RetrofitClient.instance.getUserCheckIn(userId).enqueue(object :Callback<List<Checkin>>{
                                        override fun onResponse(
                                            call: Call<List<Checkin>>,
                                            response1: Response<List<Checkin>>
                                        ) {
                                            response1.body()?.forEach{ checkin ->
                                                RetrofitClient.instance.getLocationById(checkin.location).enqueue(object :Callback<Locations>{
                                                    override fun onResponse(
                                                        call: Call<Locations>,
                                                        response2: Response<Locations>
                                                    ) {
                                                        RetrofitClient.instance.getUserById(response2.body()?.owner).enqueue(object :Callback<User>{
                                                            override fun onResponse(
                                                                call: Call<User>,
                                                                response3: Response<User>
                                                            ) {
                                                                val recipient = response3.body()!!.email


                                                                RetrofitClient.instance.sendEmail(response3.body()?.email).enqueue(object : Callback<DefaultResponse>{
                                                                    override fun onResponse(
                                                                        call: Call<DefaultResponse>,
                                                                        response: Response<DefaultResponse>
                                                                    ) {
                                                                    }

                                                                    override fun onFailure(
                                                                        call: Call<DefaultResponse>,
                                                                        t: Throwable
                                                                    ) {
                                                                    }

                                                                })



                                                            }
                                                            override fun onFailure(
                                                                call: Call<User>,
                                                                t: Throwable
                                                            ) {
                                                            }

                                                        })
                                                    }
                                                    override fun onFailure(
                                                        call: Call<Locations>,
                                                        t: Throwable
                                                    ) {
                                                    }
                                                })

                                            }
                                        }

                                        override fun onFailure(
                                            call: Call<List<Checkin>>,
                                            t: Throwable
                                        ) {
                                        }

                                    })
                                    val intent = Intent(thiscontext, ApplicationActivity::class.java)
                                    intent.flags =
                                        Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

                                    startActivity(intent)
                                } else {
                                    Toast.makeText(
                                        thiscontext,
                                        "Error !",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                            }

                            override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                            }
                        })
                }
                .setNegativeButton("No") { dialog, id ->
                    // Dismiss the dialog
                    dialog.dismiss()
                }
            val alert = builder.create()
            alert.show()
        }
        return viewOfLayout
    }


    fun getDateFromDatePicker(datePicker: DatePicker): Date? {
        val day = datePicker.dayOfMonth
        val month = datePicker.month
        val year = datePicker.year
        val calendar: Calendar = Calendar.getInstance()
        calendar.set(year, month, day,1,0,0)
        return calendar.getTime()
    }


    private fun putIfMissing(props: Properties, key: String, value: String) {
        if (!props.containsKey(key)) {
            props[key] = value
        }
    }
}