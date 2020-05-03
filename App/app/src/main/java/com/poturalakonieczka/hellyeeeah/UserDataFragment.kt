package com.poturalakonieczka.hellyeeeah

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.user_data_fragment.*

class UserDataFragment : Fragment() {
    companion object {
        fun newInstance() = UserDataFragment()
    }

    private lateinit var viewModel: ModelView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View {

        return inflater.inflate(R.layout.user_data_fragment, container, false)

    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(ModelView::class.java)
        Log.d("My-deb", "Create user data fragment")
        var email = viewModel.getUser()!!.email
        textView2.text = email
        //val imie = viewModel.getUserName()
        val kursant = viewModel.downloadKursant()



    }

}