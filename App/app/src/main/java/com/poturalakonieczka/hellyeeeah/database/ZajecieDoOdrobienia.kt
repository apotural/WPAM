package com.poturalakonieczka.hellyeeeah.database

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentReference

data class ZajecieDoOdrobienia(
    val grupaN: DocumentReference?,
    val grupaO: DocumentReference?,
    val mozliweOdrobienie: Boolean,
    val terminN: Timestamp,
    val terminO: Timestamp?
){
    constructor() : this(null , null, true, Timestamp(0, 0), null)
}