import 'dart:async';

import 'package:flutter/services.dart';

class AliPush {
  static const MethodChannel _channel = MethodChannel('ali_push');

  static Future<String?> get platformVersion async {
    final String? version = await _channel.invokeMethod('getPlatformVersion');
    return version;
  }
}
