package com.projet.covidtracker.adapter

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.zxing.BarcodeFormat
import com.journeyapps.barcodescanner.BarcodeEncoder
import com.projet.covidtracker.R
import com.projet.covidtracker.activity.ApplicationActivity
import com.projet.covidtracker.api.RetrofitClient
import com.projet.covidtracker.models.DefaultResponse
import com.projet.covidtracker.models.Locations
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class userLocationAdapter (val context: Context, val userLocationsList:List<Locations>) : RecyclerView.Adapter<userLocationAdapter.ViewHolder>(){


    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        lateinit var locationName: TextView
        lateinit var locationCategory: TextView
        lateinit var locationCapacity: TextView
        lateinit var locationQrCode: ImageView
        lateinit var locationDelete: ImageView

        init {
            locationName = itemView.findViewById(R.id.userLocationName)
            locationCategory = itemView.findViewById(R.id.userLocationCategorie)
            locationCapacity = itemView.findViewById(R.id.userLocationCapacity)
            locationQrCode = itemView.findViewById(R.id.userLocationQrGenerator)
            locationDelete = itemView.findViewById(R.id.userLocationDelete)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var itemView = LayoutInflater.from(context).inflate(R.layout.user_location_row,parent,false)
        return ViewHolder(itemView)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.locationName.text = userLocationsList[position].name
        holder.locationCategory.text = userLocationsList[position].categorie
        holder.locationCapacity.text = userLocationsList[position].capacity.toString()

        holder.locationQrCode.setOnClickListener {
            val barcodeEncoder = BarcodeEncoder()
            val bitmap = barcodeEncoder.encodeBitmap(userLocationsList[position].id, BarcodeFormat.QR_CODE, 870, 870)

            val alertDialog = AlertDialog.Builder(context)
            alertDialog.setMessage("this is your location QR-Code");
            val imageView = ImageView(context)
            imageView.setImageBitmap(bitmap)

            alertDialog.setView(imageView);
            alertDialog.setNegativeButton("Close") { dialog, id ->
                // Dismiss the dialog
                dialog.dismiss()
            }
            alertDialog.create();
            alertDialog.show();
        }

        holder.locationDelete.setOnClickListener {
            val builder = AlertDialog.Builder(holder.locationName.context)
            builder.setMessage("Are you sure you want to delete this location?")
                .setCancelable(false)
                .setPositiveButton("Yes") { dialog, id ->
                    RetrofitClient.instance.deleteLocation(userLocationsList[position].id).enqueue(object :
                        Callback<DefaultResponse>{
                        override fun onResponse(
                            call: Call<DefaultResponse>,
                            response: Response<DefaultResponse>
                        ) {
                            notifyItemRemoved(holder.adapterPosition)
                        }
                        override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                        }
                    })
                    val myIntent = Intent(context, ApplicationActivity::class.java)
                    context.startActivity(myIntent)
                }
                .setNegativeButton("No") { dialog, id ->
                    // Dismiss the dialog
                    dialog.dismiss()
                }
            val alert = builder.create()
            alert.show()
        }
    }



    override fun getItemCount(): Int {
        return userLocationsList.size
    }
}