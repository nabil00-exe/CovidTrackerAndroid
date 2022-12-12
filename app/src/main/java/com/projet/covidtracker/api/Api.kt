package com.projet.covidtracker.api

import com.projet.covidtracker.models.*
import retrofit2.Call
import retrofit2.http.*
import java.time.LocalDateTime
import java.util.*

interface Api {

    @FormUrlEncoded
    @POST("users/register")
    fun createUser(
        @Field("email") email:String,
        @Field("name") name:String,
        @Field("password") password:String,
        @Field("passqr") passqr:String
    ): Call<DefaultResponse>

    @FormUrlEncoded
    @POST("users/authenticate")
    fun userLogin(
        @Field("email") email:String,
        @Field("password") password: String
    ):Call<LoginResponse>


    @FormUrlEncoded
    @POST("locations/create")
    fun addLocation(
        @Field("name") name:String,
        @Field("categorie") categorie: String,
        @Field("capacity") capacity:Int,
        @Field("owner") owner:String,
        @Field("latitude") latitude: Double,
        @Field("longitude") longitude: Double

    ):Call<DefaultResponse>

    @POST("reclamations/sendmail/{email}")
    fun sendEmail(
        @Path("email") email : String?
    ):Call<DefaultResponse>

    @FormUrlEncoded
    @PUT("locations/{id}")
    fun updateLocation(
        @Path("id") id : String?,
        @Field("capacity") capacity:Int

    ):Call<DefaultResponse>


    @GET("locations/{id}")
    fun getLocationById(@Path("id") id: String?): Call<Locations>

    @GET("users/{id}")
    fun getUserById(@Path("id") id: String?): Call<User>

    @GET("locations/owner/{owner}")
    fun getUserLocations(@Path("owner")owner: String): Call<List<Locations>>

    @DELETE("locations/{id}")
    fun deleteLocation (@Path("id") id : String):Call<DefaultResponse>

    @FormUrlEncoded
    @POST("checkin/create")
    fun addCheckin(
        @Field("user") user: String?,
        @Field("location") location: String?,
        @Field("checkin") checkin: String?,
        @Field("checkout") checkout: String?
    ):Call<DefaultResponse>

    @GET("checkin/owner/{owner}")
    fun getUserCheckIn(@Path("owner")owner: String): Call<List<Checkin>>

    @FormUrlEncoded
    @POST("reclamations/create")
    fun addReclamation(
        @Field("user") user: String?,
        @Field("dateTest") dateTest: Date?,
        @Field("dateReclamation") dateReclamation: LocalDateTime?
    ):Call<DefaultResponse>

}