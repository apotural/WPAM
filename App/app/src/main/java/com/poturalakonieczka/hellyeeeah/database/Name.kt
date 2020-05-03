package com.poturalakonieczka.hellyeeeah.database

data class Name(
    val imie: String,
    val nazwisko: String
){
    constructor() : this("", "")
}