//
//  AliPushNotificationEvent.swift
//  ali_push
//
//  Created by 林小强 on 2022/1/28.
//

import Foundation
//
//  AliPushNotificationEvent.swift
//  ali_push1
//
//  Created by 林小强 on 2022/1/27.
//

import Foundation

enum AliPushNotificationEvent:String{
    case onNotificationRemoved="onNotificationRemoved"
    case onNotification="onNotification"
    case onMessage="onMessage"
    case onNotificationOpened="onNotificationOpened"
    case onNotificationReceivedInApp="onNotificationReceivedInApp"
    case onNotificationClickedWithNoAction="onNotificationClickedWithNoAction"
    case register="register"
    case deviceId="deviceId"
    case turnOnPushChannel="turnOnPushChannel"
    case turnOffPushChannel="turnOffPushChannel"
    case checkPushChannelStatus="checkPushChannelStatus"
    case bindAccount="bindAccount"
    case unbindAccount="unbindAccount"
    case bindTag="bindTag"
    case unbindTag="unbindTag"
    case listTags="listTags"
    case addAlias="addAlias"
    case removeAlias="removeAlias"
    case listAliases="listAliases"
    case setupNotificationManager="setupNotificationManager"
    case bindPhoneNumber="bindPhoneNumber"
    case unbindPhoneNumber="unbindPhoneNumber"
    case initCloudChannelResult="initCloudChannelResult"
    case onMessageArrived="onMessageArrived"
    case cleanBadge="cleanBadge"
    case getDeviceTokenFailed="getDeviceTokenFailed"
}

