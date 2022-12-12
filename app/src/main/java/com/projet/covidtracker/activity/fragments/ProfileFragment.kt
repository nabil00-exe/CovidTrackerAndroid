package com.projet.covidtracker.activity.fragments

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.zxing.BarcodeFormat
import com.journeyapps.barcodescanner.BarcodeEncoder
import com.projet.covidtracker.R
import com.projet.covidtracker.activity.LoginActivity
import com.projet.covidtracker.models.User
import com.projet.covidtracker.storage.SharedPrefManager






/**
 * A simple [Fragment] subclass.
 * Use the [ProfileFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProfileFragment : Fragment() {

    private lateinit var viewOfLayout: View


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {



        viewOfLayout = inflater!!.inflate(R.layout.fragment_profile, container, false)

        val thiscontext = container!!.getContext();
        val user : User = SharedPrefManager.getInstance(thiscontext).user;

        val name = user.name.toString()
        val email = user.email.toString()
        val id = user.id.toString()
        val passqr = user.passqr.toString()

        val emailtxt =viewOfLayout.findViewById<TextView>(R.id.user_email)
        val nametxt =viewOfLayout.findViewById<TextView>(R.id.user_name)
        val idtxt =viewOfLayout.findViewById<TextView>(R.id.user_id)
        val btnLogOut =viewOfLayout.findViewById<Button>(R.id.button_logout)
        val qrImg = viewOfLayout.findViewById<ImageView>(R.id.user_passqr)

        val barcodeEncoder = BarcodeEncoder()
        val bitmap = barcodeEncoder.encodeBitmap(passqr, BarcodeFormat.QR_CODE, 870, 870)

        qrImg.setImageBitmap(bitmap)



        btnLogOut.setOnClickListener{

            val builder = AlertDialog.Builder(thiscontext)
            builder.setMessage("Are you sure you want to Logout?")
                .setCancelable(false)
                .setPositiveButton("Yes") { dialog, id ->
                    val intent = Intent(getActivity(), LoginActivity::class.java)
                    SharedPrefManager.getInstance(thiscontext).clear()

                    startActivity(intent)
                }
                .setNegativeButton("No") { dialog, id ->
                    // Dismiss the dialog
                    dialog.dismiss()
                }
            val alert = builder.create()
            alert.show()

        }

        nametxt.setText(name)
        emailtxt.setText(email)
        idtxt.setText(id)


        return viewOfLayout
    }

}