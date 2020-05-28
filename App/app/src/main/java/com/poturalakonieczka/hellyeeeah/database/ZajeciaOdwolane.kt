package com.poturalakonieczka.hellyeeeah.database

import com.google.firebase.Timestamp


data class ZajeciaOdwolane(
    val listaTerminow: MutableList<Timestamp?>
){
    constructor() : this(mutableListOf<Timestamp?>() )
}