package com.poturalakonieczka.hellyeeeah.database

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentReference

data class Zajecie(
    val grupa: DocumentReference?,
    val termin: Timestamp
){
    constructor() : this(null ,Timestamp(0, 0))
}