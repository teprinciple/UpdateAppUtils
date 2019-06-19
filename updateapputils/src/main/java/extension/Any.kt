package extension

import android.util.Log
import update.UpdateAppUtils

/**
 * desc: Any 扩展
 * author: teprinciple on 2019-06-03.
 */
val Any.TAG: String
    get() = this::class.java.simpleName

fun Any.log(content: String) {
    UpdateAppUtils.updateInfo.config.isDebug.yes {
        Log.e("[UpdateAppUtils]", content)
    }
}