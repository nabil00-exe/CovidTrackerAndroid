package com.projet.covidtracker.storage

import android.content.Context
import com.projet.covidtracker.models.CheckinInt

class SharedPrefCheckin private constructor(private val mCtx: Context) {

    val isCheckedIn: Boolean
        get() {
            val sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
            return sharedPreferences.getString("user", null) != null
        }

    val checkinInt: CheckinInt
        get() {
            val sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE)
            return CheckinInt(
                sharedPreferences.getString("user", null),
                sharedPreferences.getString("location", null),
                sharedPreferences.getString("checkin", null)
            )
        }

    fun saveCheckin(userId:String,locationId:String,checkinTime:String) {

        val sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        editor.putString("user", userId)
        editor.putString("location", locationId)
        editor.putString("checkin", checkinTime)

        editor.apply()

    }

    fun clear() {
        val sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()
    }



    companion object {
        private val SHARED_PREF_NAME = "my_shared_preff_checkin"
        private var mInstance: SharedPrefCheckin? = null
        @Synchronized
        fun getInstance(mCtx: Context): SharedPrefCheckin {
            if (mInstance == null) {
                mInstance = SharedPrefCheckin(mCtx)
            }
            return mInstance as SharedPrefCheckin
        }
    }
}