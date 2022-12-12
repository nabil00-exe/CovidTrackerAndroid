package com.projet.wearos.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.projet.wearos.R
import com.projet.wearos.api.RetrofitClient
import com.projet.wearos.databinding.ActivityLoginBinding
import com.projet.wearos.models.LoginResponse
import com.projet.wearos.storage.SharedPrefManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : Activity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val editTextEmail = findViewById<EditText>(R.id.etEmailWearOs)
        val editTextPassword = findViewById<EditText>(R.id.etPasswordWearOs)

        if (SharedPrefManager.getInstance(applicationContext).isLoggedIn){
            navigate()
        }


        val buttonSignIn = findViewById<Button>(R.id.btnSignInWearOs)
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
                .enqueue(object: Callback<LoginResponse> {
                    override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                        Toast.makeText(applicationContext, t.message, Toast.LENGTH_LONG).show()
                    }

                    override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                        if(response.body()?.token != null){

                            SharedPrefManager.getInstance(applicationContext).saveUser(response.body()?.user!!)

                            val intent = Intent(applicationContext, ProfileActivity::class.java)
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
        val mainIntent = Intent(this, ProfileActivity::class.java)
        startActivity(mainIntent)
    }
}