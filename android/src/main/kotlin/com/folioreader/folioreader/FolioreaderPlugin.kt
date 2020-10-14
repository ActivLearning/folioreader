package com.folioreader.folioreader

import android.app.Activity
import android.content.Context
import android.src.main.kotlin.com.folioreader.folioreader.Reader
import android.src.main.kotlin.com.folioreader.folioreader.ReaderConfig
import androidx.annotation.NonNull
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.plugin.common.BinaryMessenger
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result

import android.util.Log

/** FolioreaderPlugin */
class FolioreaderPlugin: FlutterPlugin, MethodCallHandler {

  private var reader: Reader? = null
  private var config: ReaderConfig? = null

//  private var activity: Activity? = null
  private var context: Context? = null
  var messenger: BinaryMessenger? = null

  /// The MethodChannel that will the communication between Flutter and native Android
  ///
  /// This local reference serves to register the plugin with the Flutter Engine and unregister it
  /// when the Flutter Engine is detached from the Activity
  private lateinit var channel : MethodChannel

  override fun onAttachedToEngine(@NonNull flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
    channel = MethodChannel(flutterPluginBinding.binaryMessenger, "folioreader")
    channel.setMethodCallHandler(this)
    messenger = flutterPluginBinding.binaryMessenger
    context = flutterPluginBinding.applicationContext
  }

  override fun onMethodCall(@NonNull call: MethodCall, @NonNull result: Result) {
    when (call.method){
      "config" -> {
        val themeColor = call.argument<String>("themeColor")
        val identifier = call.argument<String>("identifier")
        val scrollDirection = call.argument<String>("scrollDirection")
        val showTts = call.argument<Boolean>("showTts")
        val showRemainingIndicator = call.argument<Boolean>("showRemainingIndicator")
        val allowedSystemUI = call.argument<Boolean>("allowedSystemUI")
        Log.d("config","themeColor:$themeColor")
        config = ReaderConfig(context,identifier,themeColor,scrollDirection,showTts,showRemainingIndicator,allowedSystemUI)}
      "open" -> {
        val bookPath = call.argument<String>("bookPath") as String
        val lastLocation = call.argument<String>("lastLocation")
        Log.d("open","bookPath:$bookPath")
        reader = Reader(context = context!!,messenger = messenger!!,config = config!!)
        reader!!.open(bookPath = bookPath,lastLocation = lastLocation)
      } else -> {
        result.notImplemented()
      }
    }
  }

  override fun onDetachedFromEngine(@NonNull binding: FlutterPlugin.FlutterPluginBinding) {
    channel.setMethodCallHandler(null)
    reader?.close()
  }
}
