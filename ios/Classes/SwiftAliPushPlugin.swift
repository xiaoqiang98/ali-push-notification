import Flutter
import UIKit

public class SwiftAliPushPlugin: NSObject, FlutterPlugin,UNUserNotificationCenterDelegate {
  public static func register(with registrar: FlutterPluginRegistrar) {
    let channel = FlutterMethodChannel(name: "ali_push", binaryMessenger: registrar.messenger())
    let instance = SwiftAliPushPlugin()
    registrar.addMethodCallDelegate(instance, channel: channel)
      AliPushNotificationHandler.shared.setMethodChannel(methodChannel: channel)
  }

  public func handle(_ call: FlutterMethodCall, result: @escaping FlutterResult) {
      AliPushNotificationHandler.shared.excute(call: call, result: result)
  }
    
    public func application(_ application: UIApplication, didFinishLaunchingWithOptions launchOptions: [AnyHashable : Any] = [:]) -> Bool {
        registerAPNS(application:application)
        initPushService()
        listenOnChannelOpened()
        registerMessageReceive()
        CloudPushSDK.sendNotificationAck(launchOptions)
        return true
    }
    
    // APNs注册失败
        func application(_ application: UIApplication, didFailToRegisterForRemoteNotificationsWithError error: Error) {
            print("Get deviceToken from APNs failed, error: \(error).")
            AliPushNotificationHandler.shared.invoke(event: .getDeviceTokenFailed, parameters: error.localizedDescription)
        }
  
  
  @objc private func initPushService(){
        CloudPushSDK.turnOnDebug()
        CloudPushSDK.autoInit { (result:CloudPushCallbackResult?) in
            guard let result = result else{
                return
            }
            if result.success{
                debugPrint("Alipush SDK initialize success")
            }else{
                debugPrint("AliPush SDK initialize fail due to \(String(describing: result.error))")
            }
            
        }
    }
  
    @objc private func registerAPNS(application:UIApplication){
        if #available(iOS 10.0, *) {
            let notificationCenter = UNUserNotificationCenter.current()
            notificationCenter.delegate = self
            notificationCenter.requestAuthorization(options: [.alert,.badge,.sound]){
                (granted:Bool,error:Error?) in
                if granted {
                    debugPrint("User authored notification permission.")
                    DispatchQueue.main.async{
                        application.registerForRemoteNotifications()
                    }
                }else{
                    debugPrint("User denied notification.")
                }
            }
            return
        }
        if #available(iOS 8, *){
            application.registerUserNotificationSettings(UIUserNotificationSettings(types: [.badge,.badge,.sound], categories: nil))
            application.registerForRemoteNotifications()
            return
        }
        application.registerForRemoteNotifications(matching: [.alert,.badge,.sound])
    }
  
    
// 监听推送通道是否打开
 @objc private  func     listenOnChannelOpened() {
        let notificationName = Notification.Name("CCPDidChannelConnectedSuccess")
        NotificationCenter.default.addObserver(self,
                                               selector: #selector(channelOpenedFunc(notification:)),
                                               name: notificationName,
                                               object: nil)
    }
    @objc func channelOpenedFunc(notification : Notification) {
            print("Push SDK channel opened.")
        }
    
    // 注册消息到来监听
   @objc private func registerMessageReceive() {
        let notificationName = Notification.Name("CCPDidReceiveMessageNotification")
        NotificationCenter.default.addObserver(self,
                                               selector: #selector(onMessageReceivedFunc(notification:)),
                                               name: notificationName,
                                               object: nil)
    }
    
    // 处理推送消息
       @objc private func onMessageReceivedFunc(notification : Notification) {
           print("Receive one message.")
           let pushMessage: CCPSysMessage = notification.object as! CCPSysMessage
           let title = String.init(data: pushMessage.title, encoding: String.Encoding.utf8)
           let body = String.init(data: pushMessage.body, encoding: String.Encoding.utf8)
           
           func _onMessageReceivedFunc(){
               AliPushNotificationHandler.shared.invoke(event: AliPushNotificationEvent.onMessageArrived, parameters:["title":title,"body":body])
           }
           if !Thread.isMainThread{
               DispatchQueue.main.async{
                   _onMessageReceivedFunc()
               }
           }else{
               _onMessageReceivedFunc()
           }
       }
    
    @available(iOS 10.0, *)
    func handleiOS10Notification(_ notification: UNNotification,isFromFront:Bool=true) {
            let content: UNNotificationContent = notification.request.content
                        UIApplication.shared.applicationIconBadgeNumber = 0
        let result:[String : Any?] = ["title":content.title,"subtitle":content.subtitle,"date":notification.date,"body":content.body,"extras":content.userInfo,"badge": content.badge,"extras":content.userInfo]            //
         syncBadgeNum(0)
        UIApplication.shared.applicationIconBadgeNumber = 0
        CloudPushSDK.sendNotificationAck(content.userInfo)
        if isFromFront {
            AliPushNotificationHandler.shared.invoke(event:AliPushNotificationEvent.onNotification,parameters:result)
        }else{
            DispatchQueue.main.asyncAfter(deadline: .now()+0.5){
                AliPushNotificationHandler.shared.invoke(event:AliPushNotificationEvent.onNotificationOpened, parameters: result)
            }
        }
          
        }
    /* 同步角标数到服务端 */
     private  func syncBadgeNum(_ badgeNum: UInt) {
            CloudPushSDK.syncBadgeNum(badgeNum) { (res) in
                if (res!.success) {
                    print("Sync badge num: [\(badgeNum)] success")
                } else {
                    print("Sync badge num: [\(badgeNum)] failed, error: \(String(describing: res?.error))")
                }
            }
        }
    
    // App处于前台时收到通知(iOS 10+)
        @available(iOS 10.0, *)
    public func userNotificationCenter(_ center: UNUserNotificationCenter, willPresent notification: UNNotification, withCompletionHandler completionHandler: @escaping (UNNotificationPresentationOptions) -> Void) {
            print("Receive a notification in foreground.")
            handleiOS10Notification(notification,isFromFront: true)
            // 通知弹出，且带有声音、内容和角标
            completionHandler([.alert, .badge, .sound])
        }
    @available(iOS 10, *)
    public func userNotificationCenter(_ center: UNUserNotificationCenter, didReceive response: UNNotificationResponse, withCompletionHandler completionHandler: @escaping () -> Void) {
           let userAction = response.actionIdentifier
           if userAction == UNNotificationDefaultActionIdentifier {
               print("User opened the notification.")
               handleiOS10Notification(response.notification,isFromFront: false)
           }
           
           if userAction == UNNotificationDismissActionIdentifier {
               print("User dismissed the notification.")
               AliPushNotificationHandler.shared.invoke(event:AliPushNotificationEvent.onNotificationRemoved, parameters: response.notification.request.identifier)
           }
           completionHandler()
       }
        
}
