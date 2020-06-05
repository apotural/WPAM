package com.poturalakonieczka.hellyeeeah.services

import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.poturalakonieczka.hellyeeeah.UserActivity


class MessagingService: FirebaseMessagingService() {
    private val _TAG = "My-deb service"

    override fun onMessageReceived(remoteMessage: RemoteMessage) {

        Log.d(_TAG, "From: " + remoteMessage.from)

        // Check if message contains a data payload.
        if (remoteMessage.data.size > 0) {
            Log.d(_TAG, "Message data payload: " + remoteMessage.data)
        }
        Events.serviceEvent.postValue(remoteMessage.data["body"])

        // Check if message contains a notification payload.
        if (remoteMessage.notification != null) {
            Log.d(_TAG, "Message Notification Body: " + remoteMessage.notification!!.body)
        }

    }

}