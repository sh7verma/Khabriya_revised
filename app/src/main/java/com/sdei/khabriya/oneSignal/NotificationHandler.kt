package com.sdei.khabriya.oneSignal

import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.content.Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
import android.util.Log
import com.google.gson.Gson
import com.onesignal.OSNotificationAction.ActionType
import com.onesignal.OSNotificationOpenResult
import com.onesignal.OneSignal.NotificationOpenedHandler
import com.sdei.khabriya.activity.DetailActivity
import com.sdei.khabriya.models.Notification
import com.sdei.khabriya.models.NotificationModel


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


        var model =
            Gson().fromJson(result.notification.payload.rawPayload, Notification::class.java)

        var customModel = Gson().fromJson(model.custom, NotificationModel::class.java)


        val intent = Intent(
            context, DetailActivity::class.java
        )
        intent.putExtra(
            "notification",
            result.notification.payload.toJSONObject().getString("launchURL")
        )
//        intent.putExtra("category",result.notification.payload.toJSONObject().getString("category"))
        intent.putExtra("category", "3")
        intent.putExtra("customModel", customModel.u)
        intent.flags = FLAG_ACTIVITY_NEW_TASK or FLAG_ACTIVITY_REORDER_TO_FRONT
        context.startActivity(intent);

//        val intent = Intent(mContext, DetailActivity::class.java)
//        intent.putExtra(Utilities.EXTRA_COVER_IMAGE, viewModel?.getImage());
//        intent.putExtra(Utilities.TITLE, viewModel?.getTitle());
//        intent.putExtra(Utilities.EXTRA_LINK, viewModel?.getLink());
//        intent.putExtra(Utilities.CONTENT, viewModel?.getContent());
//        intent.putExtra(Utilities.DATE, viewModel?.getDate());
//        Log.i("Category", "Tag" + category);
//        intent.putExtra("category", category);
//        intent.putExtra("name", viewModel?.getAuthorName());
//        mContext.startActivity(intent);
    }
}