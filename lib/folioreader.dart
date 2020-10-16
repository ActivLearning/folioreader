import 'dart:async';
import 'dart:io';

import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

class Folioreader {
  static const MethodChannel _readerChannel =
      const MethodChannel('folioreader');
  static const EventChannel _pageChannel = const EventChannel('page');

  static Future config({
    Color themeColor = Colors.blue,
    ScrollDirection scrollDirection = ScrollDirection.vertical,
    bool showTts = false,
    bool showRemainingIndicator = false,
  }) async {
    await _readerChannel.invokeMethod('config', {
      'themeColor': _getHexFromColor(themeColor),
      'scrollDirection': _direction2Str(scrollDirection),
      'showTts': showTts,
      'showRemainingIndicator': showRemainingIndicator
    });
  }

  static Future open(String bookPath, {String lastLocation}) async {
    await _readerChannel
        .invokeMethod('open', {'bookPath': bookPath, 'location': lastLocation});
  }

  static Stream get locatorStream {
    Stream pageStream = _pageChannel
        .receiveBroadcastStream()
        .map((value) => Platform.isAndroid ? value : '{}');

    return pageStream;
  }

  /// Get HEX code from [Colors], [MaterialColor],
  /// [Color] and [MaterialAccentColor]
  static String _getHexFromColor(Color color) {
    return '#${color.toString().replaceAll('ColorSwatch(', '').replaceAll('Color(0xff', '').replaceAll('MaterialColor(', '').replaceAll('MaterialAccentColor(', '').replaceAll('primary value: Color(0xff', '').replaceAll('primary', '').replaceAll('value:', '').replaceAll(')', '').trim()}';
  }

  static String _direction2Str(ScrollDirection direction) {
    switch (direction) {
      case ScrollDirection.vertical:
        return 'vertical';
      case ScrollDirection.horizontal:
        return 'horizontal';
      case ScrollDirection.allDirection:
        return 'alldirections';
    }
    return 'vertical';
  }
}

enum ScrollDirection { vertical, horizontal, allDirection }
