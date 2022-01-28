import com.ux.spot.ali_push.AliPushPlugin
import io.flutter.app.FlutterApplication

class MyApplication:FlutterApplication() {
    override fun onCreate() {
        super.onCreate()
        AliPushPlugin.initPushService(this)
    }
}