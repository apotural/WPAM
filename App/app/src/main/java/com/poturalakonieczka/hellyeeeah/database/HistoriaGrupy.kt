package com.poturalakonieczka.hellyeeeah.database

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentReference

data class HistoriaGrupy (
    val grupa: DocumentReference?,
    val pierwsze: Timestamp,
    val ostatnie: Timestamp?
){
    constructor() : this(null, Timestamp(0, 0), null)
}