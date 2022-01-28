#import "AliPushPlugin.h"
#if __has_include(<ali_push/ali_push-Swift.h>)
#import <ali_push/ali_push-Swift.h>
#else
// Support project import fallback if the generated compatibility header
// is not copied when this plugin is created as a library.
// https://forums.swift.org/t/swift-static-libraries-dont-copy-generated-objective-c-header/19816
#import "ali_push-Swift.h"
#endif

@implementation AliPushPlugin
+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar {
  [SwiftAliPushPlugin registerWithRegistrar:registrar];
}
@end
