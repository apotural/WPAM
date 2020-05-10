package com.poturalakonieczka.hellyeeeah.database

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentReference

data class ZajeciaDodatkowe(
    val lista: MutableList<Zajecie?>
){
    constructor() : this(mutableListOf<Zajecie?>() )
}