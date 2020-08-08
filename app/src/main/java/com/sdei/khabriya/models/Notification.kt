package com.sdei.khabriya.models

import com.google.gson.annotations.SerializedName

class Notification {
    @SerializedName("google.delivered_priority")
    val delivered_priority: String? = null

    @SerializedName("google.sent_time")
    val sent_time: Long = 0

    @SerializedName("google.ttl")
    val ttl = 0

    @SerializedName("google.original_priority")
    val original_priority: String? = null
    val custom: String? = null
    val from: String? = null
    val alert: String? = null
    val title: String? = null

    @SerializedName("google.message_id")
    val message_id: String? = null

    @SerializedName("google.c.sender.id")
    val id: String? = null
    val androidNotificationId = 0
}