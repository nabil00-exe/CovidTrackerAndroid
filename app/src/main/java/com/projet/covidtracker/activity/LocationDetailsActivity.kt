package com.projet.covidtracker.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.model.LatLng
import com.projet.covidtracker.R
import com.projet.covidtracker.api.RetrofitClient
import com.projet.covidtracker.models.DefaultResponse
import com.projet.covidtracker.models.User
import com.projet.covidtracker.storage.SharedPrefManager
import com.travijuu.numberpicker.library.NumberPicker
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LocationDetailsActivity : AppCompatActivity() {
    private val user : User = SharedPrefManager.getInstance(this).user;

    private val idUser = user.id.toString()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location_details)

        val objLatLng: LatLng? = intent.extras!!.getParcelable("Latlng")

        val lat = objLatLng!!.latitude
        val lng = objLatLng!!.longitude

        val btnAddLocation = findViewById<Button>(R.id.btnLocationConfirm)

        val editTextName = findViewById<EditText>(R.id.etLocationName)
        val categoriePicker = findViewById<Spinner>(R.id.spinnerLocationCategorie)

        val capacityPicker = findViewById<NumberPicker>(R.id.LocationCapacityPicker)

        btnAddLocation.setOnClickListener {
            val name = editTextName.text.toString().trim()
            val categorie = categoriePicker.selectedItem.toString().trim()
            val capacity = capacityPicker.value


            if(name.isEmpty()){
                editTextName.error = "Name required"
                editTextName.requestFocus()
                return@setOnClickListener
            }
            else {

                RetrofitClient.instance.addLocation(name, categorie, capacity, idUser,lat,lng)
                    .enqueue(object : Callback<DefaultResponse> {


                        override fun onResponse(
                            call: Call<DefaultResponse>,
                            response: Response<DefaultResponse>
                        ) {


                            if (response.body()?.message == "location added successfully") {

                                Toast.makeText(
                                    applicationContext,
                                    response.body()?.message,
                                    Toast.LENGTH_LONG
                                ).show()

                                val intent = Intent(applicationContext, ApplicationActivity::class.java)
                                intent.flags =
                                    Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

                                startActivity(intent)
                            } else {
                                Toast.makeText(
                                    applicationContext,
                                    "Error !",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        }

                        override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {

                        }

                    })
            }

        }

    }
}