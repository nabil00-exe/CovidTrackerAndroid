package com.projet.covidtracker.models

import java.time.LocalDateTime

data class Reclamation(
    var user: String,
    var dateTest: LocalDateTime?,
    var dateReclamation: LocalDateTime?
)

