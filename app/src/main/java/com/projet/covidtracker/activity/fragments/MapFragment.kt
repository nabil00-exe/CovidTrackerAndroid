package com.projet.covidtracker.activity.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.projet.covidtracker.R
import com.projet.covidtracker.adapter.userLocationAdapter
import com.projet.covidtracker.api.RetrofitClient
import com.projet.covidtracker.models.Locations
import com.projet.covidtracker.models.User
import com.projet.covidtracker.storage.SharedPrefManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MapFragment : Fragment() {

    private lateinit var viewOfLayout: View
    lateinit var myAdapter: userLocationAdapter
    lateinit var linearLayoutManager : LinearLayoutManager


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val thiscontext = container!!.getContext();

        val user : User = SharedPrefManager.getInstance(thiscontext).user;
        val id_user = user.id.toString()

        viewOfLayout = inflater!!.inflate(R.layout.fragment_map, container, false)


        val userLocationRecyclerView = viewOfLayout.findViewById<RecyclerView>(R.id.userLocationRecyclerView)
        val btnMaps = viewOfLayout.findViewById<Button>(R.id.btn_maps)
        btnMaps.setOnClickListener {

            val builder = AlertDialog.Builder(thiscontext)
            builder.setMessage("Add Location module will be available soon!")
                .setCancelable(false)
                .setPositiveButton("OK") { dialog, id ->
                    dialog.dismiss()
                }
            val alert = builder.create()
            alert.show()

            /*val builder = AlertDialog.Builder(thiscontext)
            builder.setMessage("To add a location you need to select the location on Map")
                .setCancelable(false)
                .setPositiveButton("OK") { dialog, id ->
                    val intent = Intent(getActivity(), MapsActivity::class.java)

                    startActivity(intent)
                }
                .setNegativeButton("No") { dialog, id ->
                    // Dismiss the dialog
                    dialog.dismiss()
                }
            val alert = builder.create()
            alert.show()*/

        }
        userLocationRecyclerView.setHasFixedSize(true)
        linearLayoutManager = LinearLayoutManager(thiscontext)
        userLocationRecyclerView.layoutManager = linearLayoutManager


        RetrofitClient.instance.getUserLocations(id_user).enqueue(object :
            Callback<List<Locations>> {
            override fun onResponse(
                call: Call<List<Locations>>,
                response: Response<List<Locations>>
            ) {
                val responseBody = response.body()!!
                myAdapter = userLocationAdapter(thiscontext,responseBody)
                myAdapter.notifyDataSetChanged()
                userLocationRecyclerView.adapter = myAdapter


            }

            override fun onFailure(call: Call<List<Locations>>, t: Throwable) {
            }

        })

        return viewOfLayout
    }
}