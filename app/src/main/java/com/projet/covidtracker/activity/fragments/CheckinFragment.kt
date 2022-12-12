package com.projet.covidtracker.activity.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.projet.covidtracker.R
import com.projet.covidtracker.activity.CheckInQrScannerActivity


/**
 * A simple [Fragment] subclass.
 * Use the [CheckinFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CheckinFragment : Fragment() {

    private lateinit var viewOfLayout: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewOfLayout = inflater!!.inflate(R.layout.fragment_checkin, container, false)

        val btnScanLocationQr =viewOfLayout.findViewById<Button>(R.id.btnScanLocationQr)

        btnScanLocationQr.setOnClickListener{
            val intent = Intent(getActivity(), CheckInQrScannerActivity::class.java)

            startActivity(intent)
        }



        return viewOfLayout
    }
}