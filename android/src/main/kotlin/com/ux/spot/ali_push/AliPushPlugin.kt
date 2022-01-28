package com.ux.spot.ali_push

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.annotation.NonNull
import com.alibaba.sdk.android.push.noonesdk.PushServiceFactory
import com.alibaba.sdk.android.push.CommonCallback

import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result
import java.lang.Exception
import kotlin.math.log

/** AliPushPlugin */
class AliPushPlugin: FlutterPlugin, MethodCallHandler {
  /// The MethodChannel that will the communication between Flutter and native Android
  ///
  /// This local reference serves to register the plugin with the Flutter Engine and unregister it
  /// when the Flutter Engine is detached from the Activity



  companion object{
    private val inHandler = Handler(Looper.getMainLooper())
    private val pushService = PushServiceFactory.getCloudPushService()
    private lateinit var applicationContext:Context

    fun initPushService(context:Context){
      PushServiceFactory.init(context)

    }
  }

  private lateinit var channel : MethodChannel



  override fun onAttachedToEngine(@NonNull flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
    channel = MethodChannel(flutterPluginBinding.binaryMessenger, "ali_push")
    channel.setMethodCallHandler(this)
    AliPushNotificationHandler.methodChannel=channel
    applicationContext = flutterPluginBinding.applicationContext

    Log.d("1111","初始化")
  }

  override fun onMethodCall(@NonNull call: MethodCall, @NonNull result: Result) {
    Log.d("CALL","call--->${call.method}")
    when(call.method){
      AliPushNotificationEvent.register.name->register(result)
      AliPushNotificationEvent.deviceId.name->getDeivceId(result)
      AliPushNotificationEvent.turnOnPushChannel.name->turnOnPushChannel(result)
      AliPushNotificationEvent.turnOffPushChannel.name->turnOffPushChannel(result)
      AliPushNotificationEvent.checkPushChannelStatus.name->checkPushChannelStatus(result)
      AliPushNotificationEvent.bindAccount.name->bindAccount(call,result)
      AliPushNotificationEvent.unbindAccount.name->unbindAccount(result)
      AliPushNotificationEvent.bindTag.name->bindTag(call,result)
      AliPushNotificationEvent.unbindTag.name->unbindTag(call,result)
      AliPushNotificationEvent.listTags.name->listTags(call,result)
      AliPushNotificationEvent.addAlias.name->addAlias(call,result)
      AliPushNotificationEvent.removeAlias.name->removeAlias(call,result)
      AliPushNotificationEvent.listAliases.name->listAliases(result)
      AliPushNotificationEvent.setupNotificationManager.name->setupNotificationManager(call,result)
      AliPushNotificationEvent.bindPhoneNumber.name->bindPhoneNumber(call,result)
      AliPushNotificationEvent.unbindPhoneNumber.name->unbindPhoneNumber(result)
      else -> result.notImplemented()
    }

  }

  override fun onDetachedFromEngine(@NonNull binding: FlutterPlugin.FlutterPluginBinding) {
    channel.setMethodCallHandler(null)
  }


  private fun register(result: Result){
    pushService.register(applicationContext,object:
     CommonCallback {
      override fun onSuccess(response: String?) {
        inHandler.post{
          AliPushNotificationHandler.invokeMethod(AliPushNotificationEvent.initCloudChannelResult, mapOf(
            "isSuccessful" to true,
            "response" to response
          ))
        }
        result.success(true)
        Log.d("Success","注册成功")
      }

      override fun onFailed(errorCode: String?, errorMessage: String?) {
        inHandler.post{
          AliPushNotificationHandler.invokeMethod(AliPushNotificationEvent.initCloudChannelResult, mapOf(
            "isSuccessful" to false,
            "errorCode" to errorCode,
            "errorMessage" to errorMessage
          ))
        }
        result.success(false)
        Log.d("Success","注册失败")
      }
    })
   AliPushNotificationRegister.register(applicationContext)
  }


  private fun getDeivceId(result: Result){
    Log.d("DEVICEID","+++++++++++++++++++++++++++++++")
    try {
        result.success(pushService.deviceId)
      Log.d("222",pushService.deviceId)
    }catch (e:Exception){
      Log.e("333","failed to get deviceId")
      result.error("Failed to get deviceID","${e.message}","${e.stackTrace}")
    }
  }

  private fun turnOnPushChannel(result: Result) {
    pushService.turnOnPushChannel(object : CommonCallback {
      override fun onSuccess(response: String?) {
        result.success(mapOf(
          "isSuccessful" to true,
          "response" to response
        ))

      }

      override fun onFailed(errorCode: String?, errorMessage: String?) {
        result.success(mapOf(
          "isSuccessful" to false,
          "errorCode" to errorCode,
          "errorMessage" to errorMessage
        ))
      }
    })
  }

  private fun turnOffPushChannel(result: Result) {

    pushService.turnOffPushChannel(object : CommonCallback {
      override fun onSuccess(response: String?) {
        result.success(mapOf(
          "isSuccessful" to true,
          "response" to response
        ))

      }

      override fun onFailed(errorCode: String?, errorMessage: String?) {
        result.success(mapOf(
          "isSuccessful" to false,
          "errorCode" to errorCode,
          "errorMessage" to errorMessage
        ))
      }
    })
  }
  private fun checkPushChannelStatus(result: Result) {
    pushService.checkPushChannelStatus(object : CommonCallback {
      override fun onSuccess(response: String?) {
        result.success(mapOf(
          "isSuccessful" to true,
          "response" to response
        ))

      }

      override fun onFailed(errorCode: String?, errorMessage: String?) {
        result.success(mapOf(
          "isSuccessful" to false,
          "errorCode" to errorCode,
          "errorMessage" to errorMessage
        ))
      }
    })
  }
  private fun bindAccount(call: MethodCall, result: Result) {
    pushService.bindAccount(call.arguments as String?, object : CommonCallback {
      override fun onSuccess(response: String?) {
        result.success(mapOf(
          "isSuccessful" to true,
          "response" to response
        ))

      }

      override fun onFailed(errorCode: String?, errorMessage: String?) {
        result.success(mapOf(
          "isSuccessful" to false,
          "errorCode" to errorCode,
          "errorMessage" to errorMessage
        ))
      }
    })
  }

  private fun unbindAccount(result: Result) {
    pushService.unbindAccount(object : CommonCallback {
      override fun onSuccess(response: String?) {
        result.success(mapOf(
          "isSuccessful" to true,
          "response" to response
        ))

      }

      override fun onFailed(errorCode: String?, errorMessage: String?) {
        result.success(mapOf(
          "isSuccessful" to false,
          "errorCode" to errorCode,
          "errorMessage" to errorMessage
        ))
      }
    })
  }
  private fun bindPhoneNumber(call: MethodCall, result: Result) {
    pushService.bindPhoneNumber(call.arguments as String?, object : CommonCallback {
      override fun onSuccess(response: String?) {
        result.success(mapOf(
          "isSuccessful" to true,
          "response" to response
        ))

      }

      override fun onFailed(errorCode: String?, errorMessage: String?) {
        result.success(mapOf(
          "isSuccessful" to false,
          "errorCode" to errorCode,
          "errorMessage" to errorMessage
        ))
      }
    })
  }
  private fun unbindPhoneNumber(result: Result) {
    pushService.unbindPhoneNumber(object : CommonCallback {
      override fun onSuccess(response: String?) {
        result.success(mapOf(
          "isSuccessful" to true,
          "response" to response
        ))

      }

      override fun onFailed(errorCode: String?, errorMessage: String?) {
        result.success(mapOf(
          "isSuccessful" to false,
          "errorCode" to errorCode,
          "errorMessage" to errorMessage
        ))
      }
    })
  }

  private fun bindTag(call: MethodCall, result: Result) {
//        target: Int, tags: Array<String>, alias: String, callback: CommonCallback
    val target = call.argument("target") ?: 1
    val tagsInArrayList = call.argument("tags") ?: arrayListOf<String>()
    val alias = call.argument<String?>("alias")

    val arr = arrayOfNulls<String>(tagsInArrayList.size)
    val tags: Array<String> = tagsInArrayList.toArray(arr)

    val pushService = PushServiceFactory.getCloudPushService()

    pushService.bindTag(target, tags, alias, object : CommonCallback {
      override fun onSuccess(response: String?) {
        result.success(mapOf(
          "isSuccessful" to true,
          "response" to response
        ))

      }

      override fun onFailed(errorCode: String?, errorMessage: String?) {
        result.success(mapOf(
          "isSuccessful" to false,
          "errorCode" to errorCode,
          "errorMessage" to errorMessage
        ))
      }
    })
  }


  private fun unbindTag(call: MethodCall, result: Result) {
//        target: Int, tags: Array<String>, alias: String, callback: CommonCallback
    val target = call.argument("target") ?: 1
    val tagsInArrayList = call.argument("tags") ?: arrayListOf<String>()
    val alias = call.argument<String?>("alias")

    val arr = arrayOfNulls<String>(tagsInArrayList.size)
    val tags: Array<String> = tagsInArrayList.toArray(arr)

    pushService.unbindTag(target, tags, alias, object : CommonCallback {
      override fun onSuccess(response: String?) {
        result.success(mapOf(
          "isSuccessful" to true,
          "response" to response
        ))

      }

      override fun onFailed(errorCode: String?, errorMessage: String?) {
        result.success(mapOf(
          "isSuccessful" to false,
          "errorCode" to errorCode,
          "errorMessage" to errorMessage
        ))
      }
    })
  }

  private fun listTags(call: MethodCall, result: Result) {
    val target = call.arguments as Int? ?: 1
    pushService.listTags(target, object : CommonCallback {
      override fun onSuccess(response: String?) {
        result.success(mapOf(
          "isSuccessful" to true,
          "response" to response
        ))

      }

      override fun onFailed(errorCode: String?, errorMessage: String?) {
        result.success(mapOf(
          "isSuccessful" to false,
          "errorCode" to errorCode,
          "errorMessage" to errorMessage
        ))
      }
    })
  }
  private fun addAlias(call: MethodCall, result: Result) {
    val alias = call.arguments as String?
    pushService.addAlias(alias, object : CommonCallback {
      override fun onSuccess(response: String?) {
        result.success(mapOf(
          "isSuccessful" to true,
          "response" to response
        ))

      }

      override fun onFailed(errorCode: String?, errorMessage: String?) {
        result.success(mapOf(
          "isSuccessful" to false,
          "errorCode" to errorCode,
          "errorMessage" to errorMessage
        ))
      }
    })
  }

  private fun removeAlias(call: MethodCall, result: Result) {
    val alias = call.arguments as String?
    pushService.removeAlias(alias, object : CommonCallback {
      override fun onSuccess(response: String?) {
        result.success(mapOf(
          "isSuccessful" to true,
          "response" to response
        ))

      }

      override fun onFailed(errorCode: String?, errorMessage: String?) {
        result.success(mapOf(
          "isSuccessful" to true,
          "errorCode" to errorCode,
          "errorMessage" to errorMessage
        ))
      }
    })
  }

  private fun listAliases(result: Result) {
    pushService.listAliases(object : CommonCallback {
      override fun onSuccess(response: String?) {
        result.success(mapOf(
          "isSuccessful" to true,
          "response" to response
        ))

      }

      override fun onFailed(errorCode: String?, errorMessage: String?) {
        result.success(mapOf(
          "isSuccessful" to false,
          "errorCode" to errorCode,
          "errorMessage" to errorMessage
        ))
      }
    })


  }
  private fun setupNotificationManager(call: MethodCall, result: Result) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      Log.d("Channel",call.arguments.toString())
      val channels = call.arguments as List<Map<String, Any?>>
      val mNotificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
      val notificationChannels = mutableListOf<NotificationChannel>()
      for (channel in channels){
        // 通知渠道的id
        val id = channel["id"] ?: applicationContext.packageName
        // 用户可以看到的通知渠道的名字.
        val name = channel["name"] ?: applicationContext.packageName
        // 用户可以看到的通知渠道的描述
        val description = channel["description"] ?: applicationContext.packageName
        val importance = channel["importance"] ?: NotificationManager.IMPORTANCE_HIGH
        val mChannel = NotificationChannel(id as String, name as String, importance as Int)
        // 配置通知渠道的属性
        mChannel.description = description as String
        mChannel.enableLights(true)
        mChannel.enableVibration(true)
        notificationChannels.add(mChannel)
      }
      if (notificationChannels.isNotEmpty()){
        mNotificationManager.createNotificationChannels(notificationChannels)
      }
    }
    result.success(true)
  }
}
