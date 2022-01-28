package com.ux.spot.ali_push


import android.content.Context
import com.alibaba.sdk.android.push.MessageReceiver
import com.alibaba.sdk.android.push.notification.CPushMessage
import android.os.Handler
import android.os.Looper

class AliPushNotificationReceiver: MessageReceiver() {

    private val handler = Handler(Looper.getMainLooper())

    override fun onNotificationOpened(p0: Context?, p1: String?, p2: String?, p3: String?) {
        handler.postDelayed({
            AliPushNotificationHandler.invokeMethod(AliPushNotificationEvent.onNotificationOpened,
                parameters=mapOf(
                           "title" to p1,
                           "summary" to p2,
                           "extras" to p3
                       ))
        },1500)
    }

    override fun onNotificationRemoved(p0: Context?, p1: String?) {
        handler.postDelayed({
            AliPushNotificationHandler.invokeMethod(AliPushNotificationEvent.onNotificationRemoved,parameters=p1)
        },1500)
    }

    override fun onNotification(
        p0: Context?,
        p1: String?,
        p2: String?,
        p3: MutableMap<String, String>?
    ) {
        handler.postDelayed({
            AliPushNotificationHandler.invokeMethod(AliPushNotificationEvent.onNotification,parameters=mapOf(
                "title" to p1,
                "summary" to p2,
                "extras" to p3
            ))
        },1500)
    }

    override fun onMessage(p0: Context?, p1: CPushMessage?) {
        handler.postDelayed( {
            AliPushNotificationHandler.invokeMethod(AliPushNotificationEvent.onMessage,parameters= mapOf(
                "appId" to p1?.appId,
                "content" to p1?.content,
                "messageId" to p1?.messageId,
                "title" to p1?.title,
                "traceInfo" to p1?.traceInfo
            ))
        },1500)
    }

    override fun onNotificationClickedWithNoAction(
        p0: Context?,
        p1: String?,
        p2: String?,
        p3: String?
    ) {
        handler.postDelayed( {
            AliPushNotificationHandler.invokeMethod(AliPushNotificationEvent.onNotificationClickedWithNoAction,parameters= mapOf(
                "title" to p1,
                "summary" to p2,
                "extras" to p3
            ))
        },1500)
    }

    override fun onNotificationReceivedInApp(
        p0: Context?,
        p1: String?,
        p2: String?,
        p3: MutableMap<String, String>?,
        p4: Int,
        p5: String?,
        p6: String?
    ) {
        handler.postDelayed( {
            AliPushNotificationHandler.invokeMethod(AliPushNotificationEvent.onNotificationReceivedInApp,parameters= mapOf(
                "title" to p1,
                "summary" to p2,
                "extras" to p3,
                "openType" to p4,
                "openActivity" to p5,
                "openUrl" to p6
            ))
        },1500)
    }
}