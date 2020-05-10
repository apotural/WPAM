package com.poturalakonieczka.hellyeeeah.database

data class ZajeciaNieobecnosci(
    val lista: MutableList<ZajecieDoOdrobienia?>
){
    constructor() : this(mutableListOf<ZajecieDoOdrobienia?>() )
}