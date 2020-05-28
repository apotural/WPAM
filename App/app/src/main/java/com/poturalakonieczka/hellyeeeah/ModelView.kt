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
import com.poturalakonieczka.hellyeeeah.layoutCalendar.BasicClassInCalendar
import com.poturalakonieczka.hellyeeeah.layoutCalendar.ClassInCalendar
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

    private var _convertedAdditionalClasses: MutableList<ClassInCalendar?> = mutableListOf()
    private var _convertedAbsentClasses: MutableList<ClassInCalendar?> = mutableListOf()
    private var _convertedBasicClasses: MutableList<BasicClassInCalendar?> = mutableListOf()

    private val _basicClassesCalendar = MutableLiveData<List<BasicClassInCalendar?>>().apply {value = _convertedBasicClasses}
    private val _absentClassesCalendar = MutableLiveData<List<ClassInCalendar?>>().apply {value = _convertedAbsentClasses}
    private val _additionalClassesCalendar = MutableLiveData<List<ClassInCalendar?>>().apply {value = _convertedAdditionalClasses}
    private val _cancelledClassesCalendar = MutableLiveData<ZajeciaOdwolane?>().apply { value = _cancelledClasses }

    fun getMaxDate(): Date? {
        return maxDateCalendar
    }

    fun getMinDate(): Date? {
        return minDateCalendar
    }

    val absentClassesCalendar: LiveData<List<ClassInCalendar?>>
        get() = _absentClassesCalendar

    val additionalClassesCalendar: LiveData<List<ClassInCalendar?>>
        get() = _additionalClassesCalendar

    val basicClassesCalendar: LiveData<List<BasicClassInCalendar?>>
        get() = _basicClassesCalendar

    val cancelledClassesCalendar: LiveData<ZajeciaOdwolane?>
        get() = _cancelledClassesCalendar


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

    fun downloadParticipant() {
        if(_user != null) {
            _user!!.email?.let {_db.collection("Kursanci").document(it) }
                ?.addSnapshotListener { snapshot, e ->
                if (e != null) {
                    Log.w(_TAG, "Listen failed.", e)
                    return@addSnapshotListener
                }

                if (snapshot != null && snapshot.exists()) {
                    _participant = snapshot.toObject(Kursant::class.java)
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
                    }
                }
            }
        }
    }




    private fun addBasicClasses(groupRef: DocumentReference?, minimum: Date?, maximum: Date?) {
        val group = _mapGroupRef[groupRef]
        _convertedBasicClasses = _convertedBasicClasses.filter{ !removePreviousVersion(it, groupRef)} as MutableList<BasicClassInCalendar?>
        var tmpConvertedBasicClasses: MutableList<BasicClassInCalendar?> = mutableListOf()
        val type = group?.rodzaj
        val withWho = group?.prowadzaca
        if (group?.terminy != null) {
            for (basicClass in group.terminy) {
                if (basicClass != null) {
                    if (shouldRetain(basicClass.termin, minimum, maximum) ) {
                        tmpConvertedBasicClasses.add(
                            groupRef?.let {
                                BasicClassInCalendar(
                                    it,
                                    basicClass.termin,
                                    type!!, withWho!!
                                )
                            }
                        )
                    }
                }
            }
        }

        _convertedBasicClasses.addAll(tmpConvertedBasicClasses)
        _basicClassesCalendar.apply { value = _convertedBasicClasses }
    }

    private fun removePreviousVersion(it: BasicClassInCalendar?, groupR: DocumentReference?):Boolean {
        return it?.groupRef == groupR;
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
                var tmpConvertedAbsentClasses: MutableList<ClassInCalendar?> = mutableListOf()
                Log.d(_TAG, _absentClasses.toString())

                if (_absentClasses?.lista != null) {
                    for (absentClass in _absentClasses?.lista!!) {
                        if (absentClass != null) {
                            if (shouldRetain(absentClass.terminN) || shouldRetain(absentClass.terminO)) {
                                val typeO = _mapGroupRef[absentClass.grupaO]?.rodzaj
                                val withWhoO = _mapGroupRef[absentClass.grupaO]?.prowadzaca
                                val typeN = _mapGroupRef[absentClass.grupaN]?.rodzaj
                                val withWhoN = _mapGroupRef[absentClass.grupaN]?.prowadzaca
                                tmpConvertedAbsentClasses.add(
                                    ClassInCalendar(
                                        absentClass.terminN,
                                        typeN!!,
                                        withWhoN!!,
                                        absentClass.terminO,
                                        typeO,
                                        withWhoO,
                                        absentClass.mozliweOdrobienie
                                    )
                                )
                            }
                        }
                    }
                }

                _convertedAbsentClasses = tmpConvertedAbsentClasses
                _absentClassesCalendar.apply { value = _convertedAbsentClasses }
            }
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
                var tmpConvertedAdditionalClasses: MutableList<ClassInCalendar?> = mutableListOf()
                Log.d(_TAG, _additionalClasses.toString())
                if (_additionalClasses?.lista != null) {
                    for (additionalClass in _additionalClasses?.lista!!) {
                        if (additionalClass != null) {
                            if (shouldRetain(additionalClass.termin)) {
                                val type = _mapGroupRef[additionalClass.grupa]?.rodzaj
                                val withWho = _mapGroupRef[additionalClass.grupa]?.prowadzaca
                                if ((type != null) and (withWho != null)) {
                                    tmpConvertedAdditionalClasses.add(
                                        ClassInCalendar(
                                            additionalClass.termin,
                                            type!!,
                                            withWho!!
                                        )
                                    )
                                }
                            }
                        }
                    }
                }
                _convertedAdditionalClasses = tmpConvertedAdditionalClasses
                _additionalClassesCalendar.apply { value = _convertedAdditionalClasses }
            }
        }
    }

    private fun downloadClasses(){
        downloadGroupHistory()
        downloadCancelledClasses()
        downloadAbsentClasses()
        downloadAdditionalClasses()
    }

}

