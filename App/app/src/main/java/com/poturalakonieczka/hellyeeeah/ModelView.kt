package com.poturalakonieczka.hellyeeeah

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.*
import com.poturalakonieczka.hellyeeeah.database.*
import com.poturalakonieczka.hellyeeeah.layoutCalendar.CalendarItem
import java.text.SimpleDateFormat
import java.util.*


class ModelView : ViewModel() {

    private val _TAG: String = "My-log viewModel"

    private var _fbAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private var _db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private var _user: FirebaseUser? = _fbAuth.currentUser

    private var _participant: Kursant? = null

    private val _groups: MutableList<Grupa?> = mutableListOf()
    private var _fullGroupReference: MutableList<DocumentReference?> =  mutableListOf()
    private var _previousFullGroupReference: MutableList<DocumentReference?> =  mutableListOf()
    private var _mapListener: MutableMap<DocumentReference?, ListenerRegistration> = mutableMapOf()
    private var _mapGroupRef: MutableMap<DocumentReference?, Grupa?> = mutableMapOf()

    private var _cancelledClasses: ZajeciaOdwolane? = null
    private var _additionalClasses: ZajeciaDodatkowe? = null
    private var _absentClasses: ZajeciaNieobecnosci? = null
    private var _groupHistory: HistoriaGrup? = null

    private var maxDateCalendar: Date? = null
    private var minDateCalendar: Date? = null
    private var currDateCalendar: Date? = null

    private var _convertedAdditionalClasses: MutableList<CalendarItem?> = mutableListOf()
    private var _convertedAbsentClasses: MutableList<CalendarItem?> = mutableListOf()
    private var _convertedBasicClasses: MutableList<CalendarItem?> = mutableListOf()

    /* new object */
    private var _convertedCalendarClassesList: MutableList<CalendarItem?> = mutableListOf()
    private val _calendarClassesList = MutableLiveData<List<CalendarItem?>>().apply {value = _convertedCalendarClassesList}
    val calendarClassesList: LiveData<List<CalendarItem?>>
        get() = _calendarClassesList
    /* new object */

    /* it was somewhere above */
    private var _participantGroups: MutableList<Grupa?> = mutableListOf()
    private val _participantGroupsLive = MutableLiveData<List<Grupa?>>().apply { value = _participantGroups }
    val participantGroupsLive: LiveData<List<Grupa?>>
        get() = _participantGroupsLive
    /*  it was somewhere above  */

    /*  _cancelledClasses is somewhere above  */
    val cancelledClassesCalendar: LiveData<ZajeciaOdwolane?>
        get() = _cancelledClassesCalendar
    private val _cancelledClassesCalendar = MutableLiveData<ZajeciaOdwolane?>().apply { value = _cancelledClasses }
    /*  _cancelledClasses is somewhere above  */

    /*  _cancelledClasses is somewhere above  */
    val cancelledClassesWeek: LiveData<List<Timestamp?>>
        get() = _cancelledClassesWeek
    private val _cancelledClassesWeek = MutableLiveData<List<Timestamp?>>().apply { value = mutableListOf() }
    /*  _cancelledClasses is somewhere above  */

    /*  _participant is somewhere above  */
    private val _participantData = MutableLiveData<Kursant?>().apply { value = _participant }
    val participantName : LiveData<Kursant?>
        get() = _participantData
    /*  _participant is somewhere above  */

    /* only for calendar fragment */
    private var _currentCalendarSelectedDate: String? = null

    fun getMaxDate(): Date? {
        return maxDateCalendar
    }
    fun getMinDate(): Date? {
        return minDateCalendar
    }


    fun setDateFilters(){
        val min: Calendar = Calendar.getInstance()
        min.add(Calendar.MONTH, -2)
        val max: Calendar = Calendar.getInstance()
        max.add(Calendar.MONTH, 1)

        currDateCalendar = Calendar.getInstance().time
        maxDateCalendar = max.time
        minDateCalendar = min.time
    }

    private fun findGroupChange(){
        val added = _fullGroupReference.toSet().minus(_previousFullGroupReference.toSet())
        val removed = _previousFullGroupReference.toSet().minus(_fullGroupReference.toSet())
        for (add in added){
            downloadGroup(add)
        }
        for (remove in removed){
            removeGroup(remove)
        }
    }

    private fun updateGroup(groupRef: DocumentReference?) {
        removeGroup(groupRef)
        downloadGroup(groupRef)
    }

    private fun downloadParticipant() {
        if(_user != null) {
            _user!!.email?.let {_db.collection("Kursanci").document(it) }
                ?.addSnapshotListener { snapshot, e ->
                if (e != null) {
                    Log.w(_TAG, "Listen failed.", e)
                    return@addSnapshotListener
                }

                if (snapshot != null && snapshot.exists()) {
                    _participant = snapshot.toObject(Kursant::class.java)
                    _participantData.value = _participant
                }
            }
            downloadClasses()
        }
    }

    fun downloadGroups(){
        _db.collection("Grupy")
            .addSnapshotListener{ querySnapshot: QuerySnapshot?, e: FirebaseFirestoreException? ->
                if (e != null) {
                    Log.w(_TAG, "Listen failed.", e)
                    return@addSnapshotListener
                }

                if (querySnapshot != null) {
                    val allGroupList = querySnapshot.documents
                    _fullGroupReference.clear()
                    for(doc in allGroupList){
                        _fullGroupReference.add(doc.reference)
                    }

                    findGroupChange()
                    _previousFullGroupReference = _fullGroupReference

                    if(_participant == null) downloadParticipant()

                }
            }
    }

    private fun downloadGroup(doc: DocumentReference?) {
        val lisReg =  doc!!.addSnapshotListener { snapshot, e ->
            if (e != null) {
                Log.w(_TAG, "Listen failed.", e)
                return@addSnapshotListener
            }

            if (snapshot != null && snapshot.exists()) {
                val group = snapshot.toObject(Grupa::class.java)
                _groups.add(group)
                _mapGroupRef[doc] = group
                updateBasicClassInCalendar(doc)
            }
        }
        _mapListener[doc] = lisReg
    }

    private fun updateBasicClassInCalendar(doc: DocumentReference) {
        if (_groupHistory != null) {

            for (groupH in _groupHistory?.listaTerminow!!) {
                if(groupH?.grupa == doc){
                    var minimum: Date? = minDateCalendar
                    var maximum: Date? = maxDateCalendar
                    if (groupH?.pierwsze?.toDate()!! > minDateCalendar) {
                        minimum = groupH.pierwsze.toDate()
                    }
                    if (groupH.ostatnie == null) {
                        addBasicClasses(groupH.grupa , minimum, maxDateCalendar)
                    } else {
                        if (groupH.ostatnie.toDate() > minDateCalendar) {
                            if (groupH.ostatnie.toDate() < maxDateCalendar) {
                                maximum = groupH.ostatnie.toDate()
                            }
                            addBasicClasses(groupH.grupa, minimum, maximum)
                        }
                    }
                }
            }
        }
    }

    private fun removeGroup(doc: DocumentReference?) {
        _mapListener[doc]?.remove()
        _mapListener.remove(doc)
        _groups.remove(_mapGroupRef[doc])
        _mapGroupRef.remove(doc)
    }

    private fun downloadCancelledClasses(){
        val doc = _db.collection("OdwolaneTerminy").document("OdwolaneTerminy")

        doc.addSnapshotListener { snapshot, e ->
            if (e != null) {
                Log.w(_TAG, "Listen failed.", e)
                return@addSnapshotListener
            }

            if (snapshot != null && snapshot.exists()) {
                _cancelledClasses = snapshot.toObject(ZajeciaOdwolane::class.java)
                _cancelledClasses?.listaTerminow = _cancelledClasses?.listaTerminow?.filter { shouldRetain(it) } as MutableList<Timestamp?>
                _cancelledClassesCalendar.value = _cancelledClasses

                val week: Calendar = Calendar.getInstance()
                week.add(Calendar.DATE, 7)

                var copy = _cancelledClasses?.listaTerminow
                copy = copy?.filter { shouldRetain(it, Calendar.getInstance().time, week.time) } as MutableList<Timestamp?>
                _cancelledClassesWeek.value = copy
            }
        }
    }

    private fun shouldRetain(time: Timestamp?, minDate: Date? = minDateCalendar, maxDate: Date? = maxDateCalendar) : Boolean{
        val date = time?.toDate()
        if (date != null) {
            if( (date >= minDate) and (date <= maxDate) ) return true
        }
        return false
    }

    private fun downloadGroupHistory(){
        val doc = _user?.email?.let {
            _db.collection("Kursanci/$it/zajecia").document("historiaGrup") }

        doc!!.addSnapshotListener { snapshot, e ->
            if (e != null) {
                Log.w(_TAG, "Listen failed.", e)
                return@addSnapshotListener
            }
            if (snapshot != null && snapshot.exists()) {
                _participantGroups.clear()
                _groupHistory= snapshot.toObject(HistoriaGrup::class.java)
                if(_groupHistory?.listaTerminow != null){
                    for(group in _groupHistory?.listaTerminow!!) {
                        var minimum: Date? = minDateCalendar
                        var maximum: Date? = maxDateCalendar
                        if (group?.pierwsze?.toDate()!! > minDateCalendar) {
                            minimum = group.pierwsze.toDate()
                        }
                        if (group.ostatnie == null) {
                            addBasicClasses(group.grupa , minimum, maxDateCalendar)
                        } else {
                            if (group.ostatnie.toDate() > minDateCalendar) {
                                if (group.ostatnie.toDate() < maxDateCalendar) {
                                    maximum = group.ostatnie.toDate()
                                }
                                addBasicClasses(group.grupa, minimum, maximum)
                            }
                        }
                        val grupa = _mapGroupRef[group.grupa]
                        Log.d(_TAG, "grupa "+grupa?.dzien)

                        if (grupa != null && (group.ostatnie == null || group.ostatnie.toDate() > Calendar.getInstance().time)){
                            _participantGroups.add(grupa)
                            _participantGroupsLive.value = _participantGroupsLive.value
                        }

                    }
                }
            }
        }
    }

    private fun addBasicClasses(groupRef: DocumentReference?, minimum: Date?, maximum: Date?) {
        val group = _mapGroupRef[groupRef]
        val previousBasicClasses = _convertedBasicClasses
        _convertedBasicClasses = _convertedBasicClasses.filter{ !removePreviousVersion(it, groupRef)} as MutableList<CalendarItem?>
        var tmpConvertedBasicClasses: MutableList<CalendarItem?> = mutableListOf()
        val classType = group?.rodzaj
        val withWho = group?.prowadzaca
        if (group?.terminy != null) {
            for (basicClass in group.terminy) {
                if (basicClass != null) {
                    if (shouldRetain(basicClass.termin, minimum, maximum) ) {
                        val timestamp = basicClass.termin
                        val date = timestamp.toDate()
                        val type = "BASIC"
                        if((classType != null) and (withWho != null)){
                            tmpConvertedBasicClasses.add(
                                CalendarItem(groupRef, null, timestamp, null,
                                    date, null, classType!!, "", withWho!!, "",
                                    type)
                            )
                        }
                    }
                }
            }
        }
        /* add new basic classes */
        _convertedBasicClasses.addAll(tmpConvertedBasicClasses)
        /* here compare classes and return differences */
        updateCalendarList(previousBasicClasses, _convertedBasicClasses, false)
    }

    private fun removePreviousVersion(it: CalendarItem?, groupR: DocumentReference?):Boolean {
        return it?.groupRef1 == groupR;
    }

    private fun downloadAbsentClasses(){
        val doc = _user?.email?.let {
            _db.collection("Kursanci/$it/zajecia").document("nieobecnosc") }

        doc!!.addSnapshotListener { snapshot, e ->
            if (e != null) {
                Log.w(_TAG, "Listen failed.", e)
                return@addSnapshotListener
            }

            if (snapshot != null && snapshot.exists()) {
                _absentClasses= snapshot.toObject(ZajeciaNieobecnosci::class.java)
                var tmpConvertedAbsentClasses: MutableList<CalendarItem?> = mutableListOf()

                if (_absentClasses?.lista != null) {
                    for (absentClass in _absentClasses?.lista!!) {
                        if (absentClass != null) {
                            val groupRef1 = absentClass.grupaN
                            val groupRef2 = absentClass.grupaO
                            val classType1 =_mapGroupRef[absentClass.grupaN]?.rodzaj
                            var classType2 =_mapGroupRef[absentClass.grupaO]?.rodzaj
                            val withWho1 =_mapGroupRef[absentClass.grupaN]?.prowadzaca
                            var withWho2 =_mapGroupRef[absentClass.grupaO]?.prowadzaca
                            val timestamp1 =absentClass.terminN
                            val timestamp2 =absentClass.terminO
                            val date1 = timestamp1.toDate()
                            var date2: Date? = null
                            if(timestamp2 != null) date2 = timestamp2.toDate()
                            if(classType2 == null) classType2 = ""
                            if(withWho2 == null) withWho2 = ""

                            if (shouldRetain(absentClass.terminN)){
                                val type = getAbsentClassType(absentClass.mozliweOdrobienie, absentClass.terminO)
                                tmpConvertedAbsentClasses.add(CalendarItem(
                                    groupRef1, groupRef2, timestamp1, timestamp2,
                                    date1, date2, classType1!!, classType2, withWho1!!, withWho2, type
                                ))
                            }
                            if (shouldRetain(absentClass.terminO)) {
                                tmpConvertedAbsentClasses.add(CalendarItem(
                                    groupRef2, groupRef1, timestamp2!!, timestamp1,
                                    date2!!, date1, classType2!!, classType1!!, withWho2!!, withWho1!!, "CATCH_UP"
                                ))
                            }
                        }
                    }
                }
                /* here compare classes and return differences */
                updateCalendarList(_convertedAbsentClasses, tmpConvertedAbsentClasses, true)
                _convertedAbsentClasses = tmpConvertedAbsentClasses
            }
        }
    }

    private fun getAbsentClassType(mozliweOdrobienie: String, terminO: Timestamp?): String {
        if(terminO != null){
            return "MISSED_CATCH_UP"
        }
        return when (mozliweOdrobienie) {
            "usprawiedliwione" -> {
                "EXCUSED"
            }
            "nieDoOdrobienia" -> {
                "LOST"
            }
            "doOdrobienia" -> {
                "MISSED"
            }
            else -> ""
        }
    }

    private fun downloadAdditionalClasses(){
        val doc = _user?.email?.let {
            _db.collection("Kursanci/$it/zajecia").document("dodatkowe") }

        doc!!.addSnapshotListener { snapshot, e ->
            if (e != null) {
                Log.w(_TAG, "Listen failed.", e)
                return@addSnapshotListener
            }

            if (snapshot != null && snapshot.exists()) {
                _additionalClasses = snapshot.toObject(ZajeciaDodatkowe::class.java)
                var tmpConvertedAdditionalClasses: MutableList<CalendarItem?> = mutableListOf()
                Log.d(_TAG, _additionalClasses.toString())
                if (_additionalClasses?.lista != null) {
                    for (additionalClass in _additionalClasses?.lista!!) {
                        if (additionalClass != null) {
                            if (shouldRetain(additionalClass.termin)) {
                                val groupRef = additionalClass.grupa
                                val timestamp = additionalClass.termin
                                val date = timestamp.toDate()
                                val classType = _mapGroupRef[additionalClass.grupa]?.rodzaj
                                val withWho = _mapGroupRef[additionalClass.grupa]?.prowadzaca
                                val type = "ADDITIONAL"
                                if((classType != null) and (withWho != null)){
                                    tmpConvertedAdditionalClasses.add(
                                        CalendarItem(groupRef, null,timestamp, null,
                                            date, null, classType!!, "", withWho!!, "",
                                            type)
                                    )
                                }
                            }
                        }
                    }
                }
                /* here compare classes and return differences */
                updateCalendarList(_convertedAdditionalClasses, tmpConvertedAdditionalClasses, false)
                /* change previous additional classes to new list */
                _convertedAdditionalClasses = tmpConvertedAdditionalClasses
            }
        }
    }

    private fun updateCalendarList(previousClasses: MutableList<CalendarItem?>, newClasses: MutableList<CalendarItem?>, ifAbsent : Boolean) {
        val removed = previousClasses.minus(newClasses)
        val added = newClasses.minus(previousClasses)

        for(remove_one in removed){
            _convertedCalendarClassesList.remove(remove_one)
            if (remove_one != null) {
                if(ifAbsent && remove_one.type != "CATCH_UP"){
                    _convertedCalendarClassesList.add(_convertedBasicClasses.find { addClassesIfBasic(
                        it, remove_one.timestamp1)  })
                }
            }
        }

        for(add_one in added){
            _convertedCalendarClassesList.add(add_one)
            if (add_one != null) {
                if(ifAbsent && add_one.type != "CATCH_UP"){ /* add absent class, hide basic class */
                    _convertedCalendarClassesList = _convertedCalendarClassesList.filter{ removeClassesIfBasic(
                        it, add_one.timestamp1)} as MutableList<CalendarItem?>
                }
            }
        }

        if(added.isNotEmpty() or removed.isNotEmpty() ){
            _calendarClassesList.apply { value = _convertedCalendarClassesList }
        }
    }

    private fun addClassesIfBasic(it: CalendarItem?, timestamp1: Timestamp): Boolean {
        if (it != null) {
            if((it.timestamp1 == timestamp1) and (it.type == "BASIC")) return true
        }
        return false
    }

    private fun removeClassesIfBasic(it: CalendarItem?, timestamp1: Timestamp): Boolean {
        if(it == null) return false
        if((it.type == "BASIC") and (it.timestamp1 == timestamp1)) return false
        return true
    }

    private fun downloadClasses(){
        downloadGroupHistory()
        downloadCancelledClasses()
        downloadAbsentClasses()
        downloadAdditionalClasses()
    }

    fun getParticipantName(): String{
        var toReturn:String = ""
        if(_participant != null){
            toReturn = _participant?.name?.imie+" "+_participant?.name?.nazwisko
        }
        return toReturn
    }

    fun getParticipantMail():String?{
        return _user?.email
    }
    private val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    fun getCurrentDateString(): String {
        if(_currentCalendarSelectedDate == null){
            val currentDay: Calendar = Calendar.getInstance()
            _currentCalendarSelectedDate = dateFormat.format(currentDay.time)
        }
        return _currentCalendarSelectedDate!!
    }

    fun updateCurrentDateString(dateCalendar: Calendar) {
        _currentCalendarSelectedDate = dateFormat.format(dateCalendar.time)
    }
}

