package com.poturalakonieczka.hellyeeeah.database

import com.google.firebase.Timestamp

data class Grupa(
    val aktualna: Boolean,
    val dzien: String,
    val godzina: Timestamp,
    val odwolaneTerminy: MutableList<Timestamp?>,
    val poziom: Int,
    val rodzaj: String,
    val terminy: MutableList<Timestamp?>
){
    constructor() : this(false, "", Timestamp(0, 0),
    mutableListOf<Timestamp?>(), 0,"", mutableListOf<Timestamp?>())
}