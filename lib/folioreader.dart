import 'dart:async';

import 'package:flutter/services.dart';

class Folioreader {
  static const MethodChannel _channel = const MethodChannel('folioreader');

  static Future config(
      {String themeColor = '#ff234567',
      ScrollDirection scrollDirection = ScrollDirection.vertical,
      bool showTts = false,
      bool showRemainingIndicator = false,
      bool allowedSystemUI = false}) async {
    String directionStr = scrollDirection.toString();
    await _channel.invokeMethod('config', {
      'themeColor': themeColor,
      'scrollDirection': directionStr.substring(directionStr.indexOf('.') + 1),
      'showTts': showTts,
      'showRemainingIndicator': showRemainingIndicator,
      'allowedSystemUI': allowedSystemUI
    });
  }

  static Future open(String bookPath, {String lastLocation}) async {
    await _channel
        .invokeMethod('open', {'bookPath': bookPath, 'location': lastLocation});
  }
}

enum ScrollDirection { vertical, horizontal }
