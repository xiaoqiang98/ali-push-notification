package com.ux.spot.ali_push

import android.os.Handler
import android.os.Looper
import android.util.Log
import com.alibaba.sdk.android.push.AndroidPopupActivity
import org.json.JSONObject

class AliPushNotificationPopupPushActivity:AndroidPopupActivity() {
    private val handler = Handler(Looper.getMainLooper())
    override fun onSysNoticeOpened(title: String?, summary: String?, extras: MutableMap<String, String>?) {
        Log.d("PopupPushActivity", "onSysNoticeOpened, title: $title, content: $summary, extMap: $extras")
        startActivity(packageManager.getLaunchIntentForPackage(packageName))
        var jsonExtras = JSONObject()
        extras?.let {
            for (key in it.keys){
                jsonExtras.putOpt(key, extras[key])
            }
        }
        Log.d("PopupPushActivity", "onSysNoticeOpened extras: ${jsonExtras.toString()}")
        handler.post{
            AliPushNotificationHandler.invokeMethod(AliPushNotificationEvent.onNotificationOpened, mapOf(
                "title" to title,
                "summary" to summary,
                "extras" to jsonExtras.toString()
            ))
            finish()
        }
    }
}