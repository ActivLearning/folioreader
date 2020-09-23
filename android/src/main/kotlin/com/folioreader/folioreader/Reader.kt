package android.src.main.kotlin.com.folioreader.folioreader

import android.content.Context
import android.util.Log
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.folioreader.FolioReader
import com.folioreader.FolioReader.OnClosedListener
import com.folioreader.model.HighLight
import com.folioreader.model.HighLight.HighLightAction
import com.folioreader.model.locators.ReadLocator
import com.folioreader.util.OnHighlightListener
import com.folioreader.util.ReadLocatorListener
import io.flutter.plugin.common.BinaryMessenger
import io.flutter.plugin.common.EventChannel
import io.flutter.plugin.common.EventChannel.EventSink
import io.flutter.plugin.common.MethodChannel
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.util.*

class Reader internal constructor(private val context: Context, messenger: BinaryMessenger, config: ReaderConfig) : OnHighlightListener, ReadLocatorListener, OnClosedListener {
    private val readerConfig: ReaderConfig
    var folioReader: FolioReader
    var result: MethodChannel.Result? = null
    private var pageEventSink: EventSink? = null
    private val messenger: BinaryMessenger? = null
    fun open(bookPath: String, lastLocation: String?) {
        Thread(Runnable {
            try {
                Log.i("SavedLocation", "-> savedLocation -> $lastLocation")
                if (lastLocation != null && !lastLocation.isEmpty()) {
                    val readLocator = ReadLocator.fromJson(lastLocation)
                    folioReader.setReadLocator(readLocator)
                }
                folioReader.setConfig(readerConfig.config, true)
                        .openBook(bookPath)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }).start()
    }

    fun close() {
        folioReader.close()
    }

    private fun setPageHandler(messenger: BinaryMessenger) {
//        final MethodChannel channel = new MethodChannel(registrar.messenger(), "page");
//        channel.setMethodCallHandler(new EpubKittyPlugin());
        EventChannel(messenger, PAGE_CHANNEL).setStreamHandler(object : EventChannel.StreamHandler {
            override fun onListen(o: Any, eventSink: EventSink) {
                pageEventSink = eventSink
            }

            override fun onCancel(o: Any) {}
        })
    }

    //You can do anything on successful saving highlight list
    private val highlightsAndSave: Unit
        private get() {
            Thread(Runnable {
                var highlightList: ArrayList<HighLight?>? = null
                val objectMapper = ObjectMapper()
                try {
                    highlightList = objectMapper.readValue<ArrayList<HighLight?>>(
                            loadAssetTextAsString("highlights/highlights_data.json"),
                            object : TypeReference<List<HighlightData?>?>() {})
                } catch (e: IOException) {
                    e.printStackTrace()
                }
                if (highlightList == null) {
                    folioReader.saveReceivedHighLights(highlightList) {
                        //You can do anything on successful saving highlight list
                    }
                }
            }).start()
        }

    private fun loadAssetTextAsString(name: String): String? {
        var bufferedReader: BufferedReader? = null
        try {
            val buf = StringBuilder()
            val fileAsset = context.assets.open(name)
            bufferedReader = BufferedReader(InputStreamReader(fileAsset))
            var str: String?
            var isFirst = true
            while (bufferedReader.readLine().also { str = it } != null) {
                if (isFirst) isFirst = false else buf.append('\n')
                buf.append(str)
            }
            return buf.toString()
        } catch (e: IOException) {
            Log.e("Reader", "Error opening asset $name")
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close()
                } catch (e: IOException) {
                    Log.e("Reader", "Error closing asset $name")
                }
            }
        }
        return null
    }

    override fun onFolioReaderClosed() {}
    override fun onHighlight(highlight: HighLight, type: HighLightAction) {}
    override fun saveReadLocator(readLocator: ReadLocator) {
        Log.i("readLocator", "-> saveReadLocator -> " + readLocator.toJson())
        if (pageEventSink != null) {
            pageEventSink!!.success(readLocator.toJson())
        }
    }

    companion object {
        private const val PAGE_CHANNEL = "page"
    }

    init {
        readerConfig = config
        // highlightsAndSave
        folioReader = FolioReader.get()
                .setOnHighlightListener(this)
                .setReadLocatorListener(this)
                .setOnClosedListener(this)
        setPageHandler(messenger)
    }
}