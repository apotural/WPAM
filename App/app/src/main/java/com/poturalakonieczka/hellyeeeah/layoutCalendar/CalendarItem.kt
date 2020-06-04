package com.poturalakonieczka.hellyeeeah.layoutCalendar

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentReference
import java.util.*

class CalendarItem (val groupRef1: DocumentReference?,
                       val groupRef2: DocumentReference?,
                       val timestamp1: Timestamp,
                       val timestamp2: Timestamp?,
                       val date1: Date,
                       val date2: Date?,
                       val classType1: String,
                       val classType2: String,
                       val withWho1: String,
                       val withWho2: String,
                       val type: String
): Observable() {





}


