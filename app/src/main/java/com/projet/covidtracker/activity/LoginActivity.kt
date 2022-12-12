package com.projet.covidtracker.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.projet.covidtracker.R
import com.projet.covidtracker.api.RetrofitClient
import com.projet.covidtracker.models.LoginResponse
import com.projet.covidtracker.storage.SharedPrefManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val editTextEmail = findViewById<EditText>(R.id.editTextEmail)
        val editTextPassword = findViewById<EditText>(R.id.editTextPassword)



        val buttonSignUp = findViewById<Button>(R.id.button_signup)
               buttonSignUp.setOnClickListener{
                   val intent = Intent(this, PassQrActivity::class.java)
                    startActivity(intent)
               }

        if (SharedPrefManager.getInstance(applicationContext).isLoggedIn){
            navigate()
        }


        val buttonSignIn = findViewById<Button>(R.id.button_signin)
        buttonSignIn.setOnClickListener{
            val email = editTextEmail.text.toString().trim()
            val password = editTextPassword.text.toString().trim()

            if(email.isEmpty()){
                editTextEmail.error = "Email required"
                editTextEmail.requestFocus()
                return@setOnClickListener
            }


            if(password.isEmpty()){
                editTextPassword.error = "Password required"
                editTextPassword.requestFocus()
                return@setOnClickListener
            }

            RetrofitClient.instance.userLogin(email, password)
                .enqueue(object: Callback<LoginResponse>{
                    override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                        Toast.makeText(applicationContext, t.message, Toast.LENGTH_LONG).show()
                    }

                    override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                        if(response.body()?.token != null){

                            SharedPrefManager.getInstance(applicationContext).saveUser(response.body()?.user!!)

                            val intent = Intent(applicationContext, ApplicationActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

                            startActivity(intent)


                        }else{
                            Toast.makeText(applicationContext, "Wrong Email or password !", Toast.LENGTH_LONG).show()
                        }

                    }
                })
        }




    }

    private fun navigate(){
        val mainIntent = Intent(this, ApplicationActivity::class.java)
        startActivity(mainIntent)
    }




}