package com.projet.covidtracker.models

import java.time.LocalDateTime


data class Checkin(
    var user: String,
    var location: String,
    var checkin: String?,
    var checkout: String?
)

