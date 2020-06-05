package com.poturalakonieczka.hellyeeeah

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.poturalakonieczka.hellyeeeah.database.Grupa
import com.poturalakonieczka.hellyeeeah.layoutClasses.GroupsAdapter
import kotlinx.android.synthetic.main.user_data_fragment.*


class UserDataFragment : Fragment() {
    private var groupsAdapter: GroupsAdapter? = null
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
        //groupsAdapter = GroupsAdapter(activity!!.applicationContext, UserActivity.viewModel.participantGroupsLive.value!!)



        groupsAdapter = UserActivity.viewModel.participantGroupsLive.value?.let {
            GroupsAdapter(activity!!.applicationContext , it)
        }
        groupsAdapter!!.notifyDataSetChanged()
        list_groups.adapter = groupsAdapter
        UserActivity.viewModel.participantGroupsLive.observe(activity!!, Observer {
            Log.d(_TAG, "refresh")
            groupsAdapter?.notifyDataSetChanged()
        })

        UserActivity.viewModel.participantName.observe(activity!!, Observer {
            if (it != null){
                Log.d(_TAG, "name refresh")
                name_body.text = it.name.imie
                surname_body.text = it.name.nazwisko
                email_body.text = UserActivity.viewModel.getParticipantMail()
            }
        })



//        if(  UserActivity.viewModel.getUser() != null){
//            var email = UserActivity.viewModel.getUser()!!.email
//
//            textView2.text = email
//        }else{
//            textView2.text = "empty user"
//        }

        //val imie = viewModel.getUserName()
        //val kursant = viewModel.downloadKursant()

    }
}