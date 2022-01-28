//
//  AliPushNotificationHandler.swift
//  ali_push
//
//  Created by 林小强 on 2022/1/28.
//
import Flutter
import Foundation

typealias ChannelEventCallback = (FlutterMethodCall,@escaping FlutterResult) -> Void

class AliPushNotificationHandler:NSObject{
    
    static let shared = AliPushNotificationHandler()
    
    private var methodChannel:FlutterMethodChannel?
    
    
    
    public func setMethodChannel(methodChannel:FlutterMethodChannel){
        self.methodChannel = methodChannel
    }
    
    public func invoke(event:AliPushNotificationEvent,parameters:Any?=nil){
        guard let methodChannel = methodChannel else {
            debugPrint("methodChannel have not been initialized yet")
            return
        }
        methodChannel.invokeMethod(event.rawValue,arguments: parameters)
    }
    
    lazy var notificationHandlerCallback = {
        (callbackResult:CloudPushCallbackResult?,result: @escaping FlutterResult) in
        guard let callbackResult = callbackResult else {
            result(["isSuccessful":false,"message":"cloudPushCallbackResult is nil"])
            return
        }
        if(callbackResult.success){
            result(["isSuccessful":true,"data":callbackResult.data])
        }else{
            result(["isSuccessful":false,"message":callbackResult.error.debugDescription])
        }
    }
    
        
    
    
    @objc private func getDeviceId(call: FlutterMethodCall,result: @escaping FlutterResult){
         let deviceId =  CloudPushSDK.getDeviceId()
         result(deviceId)
     }
    
    
    @objc private func bindAccount(call: FlutterMethodCall, result: @escaping FlutterResult){
        CloudPushSDK.bindAccount("\(String(describing: call.arguments))") { (callbackResult:CloudPushCallbackResult?) in
            self.notificationHandlerCallback(callbackResult,result)
            
        }
    }
    
    @objc private func unbindAccount(call: FlutterMethodCall, result: @escaping FlutterResult){
        CloudPushSDK.unbindAccount { (callbackResult:CloudPushCallbackResult?) in
            self.notificationHandlerCallback(callbackResult,result)
            
        }
    }
    
    @objc private func bindTag(call: FlutterMethodCall, result: @escaping FlutterResult){
        guard let arguments =  call.arguments as? [String:Any?]  else{
            debugPrint("Invalid argument when binding tag")
            return
        }
        
        guard let target = arguments["target"] as? String else{
            debugPrint("Invalid target argument when binding tag")
            return
        }
        
        guard let tags = arguments["tags"] as? [Any] else{
            debugPrint("Invalid tags argument when binding tag")
            return
        }
        
        guard let alias = arguments["alias"] as? String else{
            debugPrint("Invalid alias argument when binding tag")
            return
        }
        
        CloudPushSDK.bindTag(NSString(string: target).intValue, withTags: tags, withAlias: alias){ (callbackResult:CloudPushCallbackResult?) in
            self.notificationHandlerCallback(callbackResult,result)
            
        }
    }
    
    @objc private func unbindTag(call: FlutterMethodCall, result: @escaping FlutterResult){
        guard let arguments =  call.arguments as? [String:Any?]  else{
            debugPrint("Invalid argument when unbindTag tag")
            return
        }
        
        guard let target = arguments["target"] as? String else{
            debugPrint("Invalid target argument when unbindTag tag")
            return
        }
        
        guard let tags = arguments["tags"] as? [Any] else{
            debugPrint("Invalid tags argument when unbindTag tag")
            return
        }
        
        guard let alias = arguments["alias"] as? String else{
            debugPrint("Invalid alias argument when unbindTag tag")
            return
        }
        
        CloudPushSDK.unbindTag(NSString(string: target).intValue, withTags: tags, withAlias: alias){ (callbackResult:CloudPushCallbackResult?) in
            self.notificationHandlerCallback(callbackResult,result)
            
        }
    }
    @objc private func listTags(call: FlutterMethodCall, result: @escaping FlutterResult){
        guard let target =  call.arguments as? String else{
            debugPrint("Invalid argument when listTags tag")
            return
        }
        CloudPushSDK.listTags(NSString(string: target).intValue){ (callbackResult:CloudPushCallbackResult?) in
            self.notificationHandlerCallback(callbackResult,result)
        }
    }
    
    @objc private func addAlias(call: FlutterMethodCall, result: @escaping FlutterResult){
        guard let alias =  call.arguments as? String else{
            debugPrint("Invalid argument when addAlias tag")
            return
        }
        CloudPushSDK.addAlias(alias){
            (callbackResult:CloudPushCallbackResult?) in
               self.notificationHandlerCallback(callbackResult,result)
        }
    }
    
    @objc private func removeAlias(call: FlutterMethodCall, result: @escaping FlutterResult){
        guard let alias =  call.arguments as? String else{
            debugPrint("Invalid argument when removeAlias tag")
            return
        }
        CloudPushSDK.removeAlias(alias){
            (callbackResult:CloudPushCallbackResult?) in
               self.notificationHandlerCallback(callbackResult,result)
        }
    }
    
    @objc private func listAliases(call: FlutterMethodCall, result: @escaping FlutterResult){
        CloudPushSDK.listAliases{
            (callbackResult:CloudPushCallbackResult?) in
               self.notificationHandlerCallback(callbackResult,result)
        }
    }
    
    @objc private func cleanBadge(call: FlutterMethodCall, result: @escaping FlutterResult){
        guard let count = call.arguments as? String else {
            debugPrint("Invalid argument when cleaning badge")
            return
        }
        CloudPushSDK.syncBadgeNum(UInt(NSString(string: count).intValue)){
            (callbackResult:CloudPushCallbackResult?) in
               self.notificationHandlerCallback(callbackResult,result)
        }
    }
   
    
    @objc public func excute(call: FlutterMethodCall, result: @escaping FlutterResult){
        let method = call.method
        if method == AliPushNotificationEvent.deviceId.rawValue{
           return getDeviceId(call: call, result: result)
        }
        if method == AliPushNotificationEvent.bindAccount.rawValue{
           return bindAccount(call: call, result: result)
        }
        if method == AliPushNotificationEvent.unbindAccount.rawValue{
           return unbindAccount(call: call, result: result)
        }
        if method == AliPushNotificationEvent.bindTag.rawValue{
           return bindTag(call: call, result: result)
        }
        if method == AliPushNotificationEvent.unbindTag.rawValue{
           return unbindTag(call: call, result: result)
        }
        if method == AliPushNotificationEvent.addAlias.rawValue{
           return addAlias(call: call, result: result)
        }
        if method == AliPushNotificationEvent.removeAlias.rawValue{
           return removeAlias(call: call, result: result)
        }
        if method == AliPushNotificationEvent.listTags.rawValue{
           return listTags(call: call, result: result)
        }
        if method == AliPushNotificationEvent.listAliases.rawValue{
           return listAliases(call: call, result: result)
        }
        if method == AliPushNotificationEvent.cleanBadge.rawValue{
           return cleanBadge(call: call, result: result)
        }
        result("method not implement")
        
    }
    
}
