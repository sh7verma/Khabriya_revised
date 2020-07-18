package com.sdei.khabriya.oneSignal

import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.content.Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
import android.util.Log
import com.onesignal.OSNotificationAction.ActionType
import com.onesignal.OSNotificationOpenResult
import com.onesignal.OneSignal.NotificationOpenedHandler
import com.sdei.khabriya.activity.DetailActivity
import com.sdei.khabriya.activity.MainActivity


internal class NotificationHandler(var context: Context) : NotificationOpenedHandler {
    // This fires when a notification is opened by tapping on it.
    override fun notificationOpened(result: OSNotificationOpenResult) {
        val actionType = result.action.type
        val data = result.notification.payload.additionalData
        val customKey: String?
        Log.i(
            "OSNotificationPayload",
            "result.notification.payload.toJSONObject().toString(): " + result.notification.payload.toJSONObject()
                .toString()
        )
        if (data != null) {
            customKey = data.optString("customkey", null)
            if (customKey != null) Log.i(
                "OneSignalExample",
                "customkey set with value: $customKey"
            )
        }
        if (actionType == ActionType.ActionTaken) Log.i(
            "OneSignalExample",
            "Button pressed with id: " + result.action.actionID
        )

        var intent = Intent(
            context, DetailActivity::class.java)
        intent.putExtra("notification",result.notification.payload.toJSONObject().getString("launchURL"))
//        intent.putExtra("category",result.notification.payload.toJSONObject().getString("category"))
        intent.putExtra("category","3")
        intent.flags = FLAG_ACTIVITY_NEW_TASK or FLAG_ACTIVITY_REORDER_TO_FRONT
            context.startActivity(intent);

    }
}