package com.poturalakonieczka.hellyeeeah.layoutCalendar

import com.google.firebase.Timestamp
import java.util.*

class ClassInCalendar( val dateN: Timestamp, val classTypeN: String, val withWhoN: String,
                      val dateO: Timestamp?=null, val classTypeO: String?=null,
                      val withWhoO: String?=null, val absentType: String? = null):
    Observable() {


}