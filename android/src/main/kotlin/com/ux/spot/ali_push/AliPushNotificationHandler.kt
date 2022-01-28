package com.ux.spot.ali_push

import android.util.Log
import io.flutter.plugin.common.MethodChannel
internal object  AliPushNotificationHandler{

   lateinit var methodChannel:MethodChannel


   fun invokeMethod(event:AliPushNotificationEvent,parameters:Any?=null){
       if(!::methodChannel.isInitialized){
           Log.e("methodChannel","methodChannel have not been initialized yet")
           throw RuntimeException("methodChannel have not been initialized yet")
       }
       methodChannel.invokeMethod(event.name,parameters)
   }
}