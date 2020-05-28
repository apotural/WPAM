package com.poturalakonieczka.hellyeeeah.layoutCalendar

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentReference
import java.util.*

class BasicClassInCalendar(val groupRef: DocumentReference,
                           val date: Timestamp,
                           val classType: String,
                           val withWho: String
): Observable() {
}