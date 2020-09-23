import 'dart:async';

import 'package:flutter/services.dart';

class Folioreader {
  static const MethodChannel _channel = const MethodChannel('folioreader');

  static Future config({
    String themeColor = '#ff234567',
    String scrollDirection = 'vertical',
  }) async {
    await _channel.invokeMethod('config',{'themeColor':themeColor,'scrollDirection':scrollDirection});
  }

  static Future open(String bookPath, {String location}) async {
    await _channel
        .invokeMethod('open', {'bookPath': bookPath, 'location': location});
  }
}
