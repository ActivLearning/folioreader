package android.src.main.kotlin.com.folioreader.folioreader

import android.content.Context
import android.graphics.Color
import android.util.Log
import com.folioreader.Config

class ReaderConfig(context: Context?, identifier: String?, themeColor: String? = "#ff234567",
                   scrollDirection: String? = "vertical", allowSharing: Boolean? = false, showTts: Boolean = false) {
    private val identifier: String? = null
    private val themeColor: String? = null
    private val scrollDirection: String? = null
    private val allowSharing = false
    private val showTts = false
    var config: Config

    init {

//        config = AppUtil.getSavedConfig(context);
//        if (config == null)
        config = Config()
        if (scrollDirection == "vertical") {
            config.allowedDirection = Config.AllowedDirection.ONLY_VERTICAL
        } else if (scrollDirection == "horizontal") {
            config.allowedDirection = Config.AllowedDirection.ONLY_HORIZONTAL
        } else {
            config.allowedDirection = Config.AllowedDirection.VERTICAL_AND_HORIZONTAL
        }
        Log.d("Config", "themeColor:$themeColor ,scrollDirection:$scrollDirection")
        config.setThemeColorInt(Color.parseColor(themeColor))
//        config.setNightThemeColorInt(Color.parseColor(themeColor))
//        config.setShowRemainingIndicator(true)
        config.isShowTts = showTts
    }
}