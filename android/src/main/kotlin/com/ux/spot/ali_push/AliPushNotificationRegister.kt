package com.ux.spot.ali_push


import android.app.Application
import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.util.Log
import com.alibaba.sdk.android.push.huawei.HuaWeiRegister
import com.alibaba.sdk.android.push.register.*

object AliPushNotificationRegister {

   fun register(applicationContext: Context){

   val appInfo: ApplicationInfo = applicationContext.packageManager.getApplicationInfo(applicationContext.packageName, PackageManager.GET_META_DATA)
        registerXiaomi(applicationContext,appInfo)
        registerHuawei(applicationContext,appInfo)
        registerOppo(applicationContext,appInfo)
        registerVivo(applicationContext,appInfo)
        registerMeizu(applicationContext,appInfo)
        registerGcm(applicationContext,appInfo)

   }

   private fun registerXiaomi(application:Context,appInfo: ApplicationInfo){
        val xiaomiAppId = appInfo.metaData.getString("com.xiaomi.push.client.app_id")
        val xiaomiAppKey = appInfo.metaData.getString("com.xiaomi.push.client.app_key")
        if ((xiaomiAppId != null && xiaomiAppId.isNotBlank())
            && (xiaoamiAppKey != null && xiaomiAppKey.isNotBlank())){
            Log.d("XIAOMI", "正在注册小米推送服务...")
            MiPushRegister.register(application, xiaomiAppId, xiaomiAppKey)
        }
    }
    private fun registerHuawei(application:Context,appInfo: ApplicationInfo){
        val huaweiAppId = appInfo.metaData.getString("com.huawei.hms.client.appid")
        if (huaweiAppId != null && huaweiAppId.toString().isNotBlank()){
            Log.d("HUAWEI", "正在注册华为推送服务...")
            HuaWeiRegister.register(application as Application)
        }
    }

    private fun registerOppo(application:Context,appInfo: ApplicationInfo){
        val oppoAppKey = appInfo.metaData.getString("com.oppo.push.client.app_key")
        val oppoAppSecret = appInfo.metaData.getString("com.oppo.push.client.app_secret")
        if ((oppoAppKey != null && oppoAppKey.isNotBlank())
            && (oppoAppSecret != null && oppoAppSecret.isNotBlank())){
            Log.d("OPPO", "正在注册Oppo推送服务...")
            OppoRegister.register(application, oppoAppKey, oppoAppSecret)
        }
    }

    private fun registerVivo(application:Context,appInfo: ApplicationInfo){
        val vivoAppId = appInfo.metaData.getString("com.vivo.push.app_id")
        val vivoApiKey = appInfo.metaData.getString("com.vivo.push.api_key")
        if ((vivoAppId != null && vivoAppId.isNotBlank())
            && (vivoApiKey != null && vivoApiKey.isNotBlank())){
            Log.d("VIVO", "正在注册Vivo推送服务...")
            VivoRegister.register(application)
        }
    }

    private fun registerMeizu(application:Context,appInfo: ApplicationInfo){
        val meizuAppId = appInfo.metaData.getString("com.meizu.push.client.app_id")
        val meizuAppKey = appInfo.metaData.getString("com.meizu.push.client.app_key")
        if ((meizuAppId != null && meizuAppId.isNotBlank())
            && (meizuAppKey != null && meizuAppKey.isNotBlank())){
            Log.d("MEIZU", "正在注册魅族推送服务...")
            MeizuRegister.register(application, meizuAppId, meizuAppKey)
        }
    }

    private fun registerGcm(application:Context,appInfo: ApplicationInfo){
        val gcmSendId = appInfo.metaData.getString("com.gcm.push.send_id")
        val gcmApplicationId = appInfo.metaData.getString("com.gcm.push.app_id")
        if ((gcmSendId != null && gcmSendId.isNotBlank())
            && (gcmApplicationId != null && gcmApplicationId.isNotBlank())){
            Log.d("GCM", "正在注册Gcm推送服务...")
            GcmRegister.register(application, gcmSendId, gcmApplicationId)
        }
    }
}