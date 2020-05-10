package com.poturalakonieczka.hellyeeeah.database

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentReference

data class Kursant(
    val grupy: MutableList<DocumentReference>,
    val name: Name,
    val odwolaneZajecia: MutableList<Timestamp?>

){
    constructor() : this(mutableListOf<DocumentReference>() , Name(), mutableListOf<Timestamp?>())
}