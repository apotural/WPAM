package com.poturalakonieczka.hellyeeeah.database

data class HistoriaGrup (
    val listaTerminow: MutableList<HistoriaGrupy?>
    ){
        constructor() : this(mutableListOf<HistoriaGrupy?>() )
    }