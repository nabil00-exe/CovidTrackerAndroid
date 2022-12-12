package com.projet.covidtracker.models

data class Locations(
    var name:String,
    var id: String,
    var categorie: String,
    var capacity:Int,
    var owner:String,
    var latitude: Double,
    var longitude:Double

)