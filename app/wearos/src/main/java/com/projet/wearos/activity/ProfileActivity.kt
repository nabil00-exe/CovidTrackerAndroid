package com.projet.wearos.activity

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.google.zxing.BarcodeFormat
import com.journeyapps.barcodescanner.BarcodeEncoder
import com.projet.wearos.R
import com.projet.wearos.databinding.ActivityProfileBinding
import com.projet.wearos.models.User
import com.projet.wearos.storage.SharedPrefManager

class ProfileActivity : Activity() {

    private lateinit var binding: ActivityProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val user : User = SharedPrefManager.getInstance(this).user;

        val name = user.name.toString()
        val email = user.email.toString()
        val id = user.id.toString()
        val passqr = user.passqr.toString()

        val userName =findViewById<TextView>(R.id.usernameWearOs)
        val btnLogOut =findViewById<Button>(R.id.btnLogOutWearOs)
        val qrImg = findViewById<ImageView>(R.id.userQrWearOs)
        val userEmail =findViewById<TextView>(R.id.emailWearOs)

        val barcodeEncoder = BarcodeEncoder()
        val bitmap = barcodeEncoder.encodeBitmap(passqr, BarcodeFormat.QR_CODE, 870, 870)

        qrImg.setImageBitmap(bitmap)
        userName.setText(name)
        userEmail.setText(email)

        btnLogOut.setOnClickListener{

            val builder = AlertDialog.Builder(this)
            builder.setMessage("Are you sure you want to Logout?")
                .setCancelable(false)
                .setPositiveButton("Yes") { dialog, id ->
                    val intent = Intent(this, LoginActivity::class.java)
                    SharedPrefManager.getInstance(this).clear()

                    startActivity(intent)
                }
                .setNegativeButton("No") { dialog, id ->
                    // Dismiss the dialog
                    dialog.dismiss()
                }
            val alert = builder.create()
            alert.show()

        }

    }
}