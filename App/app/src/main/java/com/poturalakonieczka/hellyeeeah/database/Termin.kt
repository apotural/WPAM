package com.poturalakonieczka.hellyeeeah.database

import com.google.firebase.Timestamp

data class Termin(
    val licznikOsob: Int,
    val termin: Timestamp
){
    constructor() : this(0,  Timestamp(0, 0))
}