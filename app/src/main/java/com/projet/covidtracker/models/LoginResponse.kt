package com.projet.covidtracker.models

data class LoginResponse(val token: String, val message:String, val user: User)