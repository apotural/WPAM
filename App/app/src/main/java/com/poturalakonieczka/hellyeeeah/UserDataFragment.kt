package com.poturalakonieczka.hellyeeeah

import android.icu.text.SimpleDateFormat
import android.icu.util.ULocale
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.Timestamp
import com.poturalakonieczka.hellyeeeah.database.Grupa
import com.poturalakonieczka.hellyeeeah.layoutCalendar.CalendarItem
import com.poturalakonieczka.hellyeeeah.layoutClasses.CanceledAdapter
import com.poturalakonieczka.hellyeeeah.layoutClasses.GroupsAdapter
import kotlinx.android.synthetic.main.user_data_fragment.*
import java.util.*


class UserDataFragment : Fragment() {
    private var groupsAdapter: GroupsAdapter? = null
    private var canceledAdapter: CanceledAdapter? = null
    private val _TAG: String = "My-log UserDataFragment"
    companion object {
        fun newInstance() = UserDataFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.user_data_fragment, container, false)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Log.d(_TAG, "Activity created")

        groupsAdapter = UserActivity.viewModel.participantGroupsLive.value?.let {
            GroupsAdapter(activity!!.applicationContext , it)
        }
        groupsAdapter!!.notifyDataSetChanged()
        list_groups.adapter = groupsAdapter
        UserActivity.viewModel.participantGroupsLive.observe(activity!!, Observer {
            groupsAdapter?.notifyDataSetChanged()
            if (it.isNotEmpty()){
                section_groups.visibility = View.VISIBLE
                section_notify.visibility = View.VISIBLE
            }
        })

        canceledAdapter = UserActivity.viewModel.cancelledClassesWeek.value?.let {
            CanceledAdapter(activity!!.applicationContext , it)
        };
        list_canceled.adapter = canceledAdapter
        UserActivity.viewModel.cancelledClassesWeek.observe(activity!!, Observer {
            if(it != null){
                if(it.size > 0){
                    section_canceled.visibility = View.VISIBLE
                }
                canceledAdapter = CanceledAdapter(activity!!.applicationContext, it)
                list_canceled.adapter = canceledAdapter
            } })

        UserActivity.viewModel.participantName.observe(activity!!, Observer {
            if (it != null){
                Log.d(_TAG, "name refresh")
                name_body.text = it.name.imie
                surname_body.text = it.name.nazwisko
                email_body.text = UserActivity.viewModel.getParticipantMail()
            } })

        UserActivity.viewModel.calendarClassesList.observe(activity!!, Observer {
            setNextClasses(it)
        })

    }

    private fun setNextClasses(list:List<CalendarItem?>){
        val currentDay = Calendar.getInstance().time
        val filtered = list.filter{ futureClasses(it, Timestamp(currentDay))} as List<CalendarItem?>
        val sortedList = filtered.sortedBy { calendarItem -> calendarItem?.timestamp1 }
        if(sortedList.isNotEmpty()){
            section_next.visibility = View.VISIBLE
            next_class_body.visibility = View.VISIBLE
            section_notify.visibility = View.VISIBLE
            val dateFormat = SimpleDateFormat("EEE, dd-MM-yyyy HH:mm", ULocale.getDefault())
            val date = sortedList[0]!!.timestamp1.toDate()
            next_class_time.text = dateFormat.format(date)
            next_class_name.text = sortedList[0]!!.classType1.toUpperCase()
        }
        else{
            section_next.visibility = View.GONE
            next_class_body.visibility = View.GONE
            if (section_canceled.visibility == View.GONE){
                section_notify.visibility = View.GONE
            }
        }

    }
    private fun futureClasses(it: CalendarItem?, today:Timestamp):Boolean {
        val types  = listOf("BASIC", "ADDITIONAL", "CATCH_UP")
        if(it != null && it!!.timestamp1 > today && types.contains(it!!.type)){
            return true
        }
        return false
    }
}