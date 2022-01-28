package com.ux.spot.ali_push

enum class AliPushNotificationEvent(name:String) {
    onNotificationRemoved("onNotificationRemoved"),
    onNotification("onNotification"),
    onMessage("onMessage"),
    onNotificationOpened("onNotificationOpened"),
    onNotificationReceivedInApp("onNotificationReceivedInApp"),
    onNotificationClickedWithNoAction("onNotificationClickedWithNoAction"),

    register("register"),
    deviceId("deviceId"),
    turnOnPushChannel("turnOnPushChannel"),
    turnOffPushChannel("turnOffPushChannel"),
    checkPushChannelStatus("checkPushChannelStatus"),
    bindAccount("bindAccount"),
    unbindAccount("unbindAccount"),
    bindTag("bindTag"),
    unbindTag("unbindTag"),
    listTags("listTags"),
    addAlias("addAlias"),
    removeAlias("removeAlias"),
    listAliases("listAliases"),
    setupNotificationManager("setupNotificationManager"),
    bindPhoneNumber("bindPhoneNumber"),
    unbindPhoneNumber("unbindPhoneNumber"),
    initCloudChannelResult("initCloudChannelResult")
}
