package com.projet.covidtracker.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

import com.projet.covidtracker.R
import com.projet.covidtracker.api.RetrofitClient
import com.projet.covidtracker.models.DefaultResponse

class SignupActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        val editTextEmail = findViewById<EditText>(R.id.editTextEmail)
        val editTextPassword = findViewById<EditText>(R.id.editTextPassword)
        val editTextName = findViewById<EditText>(R.id.editTextName)
        val buttonQrcode = findViewById<Button>(R.id.button_scan)

        val buttonlogin = findViewById<Button>(R.id.button_signin)
        buttonlogin.setOnClickListener{
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        val passqr=intent.getStringExtra("passqr").toString()


        buttonQrcode.setOnClickListener {
            val intent = Intent(this, PassQrActivity::class.java)
            startActivity(intent)

        }

        val buttonSignUp = findViewById<Button>(R.id.button_signup)
        buttonSignUp.setOnClickListener{

            val email = editTextEmail.text.toString().trim()
            val password = editTextPassword.text.toString().trim()
            val name = editTextName.text.toString().trim()




            if(email.isEmpty()){
                editTextEmail.error = "Email required"
                editTextEmail.requestFocus()
                return@setOnClickListener
            }


            else if(password.isEmpty()){
                editTextPassword.error = "Password required"
                editTextPassword.requestFocus()
                return@setOnClickListener
            }

            else if(name.isEmpty()){
                editTextName.error = "Name required"
                editTextName.requestFocus()
                return@setOnClickListener
            }

            else {

                RetrofitClient.instance.createUser(email, name, password, passqr)
                    .enqueue(object : Callback<DefaultResponse> {


                        override fun onResponse(
                            call: Call<DefaultResponse>,
                            response: Response<DefaultResponse>
                        ) {


                            if (response.body()?.message == "sign up success") {

                                Toast.makeText(
                                    applicationContext,
                                    response.body()?.message,
                                    Toast.LENGTH_LONG
                                ).show()

                                val intent = Intent(applicationContext, LoginActivity::class.java)
                                intent.flags =
                                    Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

                                startActivity(intent)
                            } else {
                                Toast.makeText(
                                    applicationContext,
                                    "Email already exists !",
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