package com.poturalakonieczka.hellyeeeah.database

import com.google.firebase.Timestamp

data class Grupa(
    val aktualna: Boolean,
    val dzien: String,
    val godzina: Timestamp,
    val liczbaKurantek: Int,
    val poziom: Int,
    val rodzaj: String,
    val terminy: MutableList<Termin?>
){
    constructor() : this(false, "", Timestamp(0, 0), 0,0,"", mutableListOf<Termin?>())
}