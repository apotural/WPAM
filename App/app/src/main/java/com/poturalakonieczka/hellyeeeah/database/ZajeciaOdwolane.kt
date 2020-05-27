package com.poturalakonieczka.hellyeeeah.database

import com.google.firebase.Timestamp
import java.util.*


data class ZajeciaOdwolane(
    var listaTerminow: MutableList<Timestamp?>
): Observable(){
    constructor() : this(mutableListOf<Timestamp?>() )
}