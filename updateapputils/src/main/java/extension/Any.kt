package extension

import android.util.Log

/**
 * desc: Any 扩展
 * author: teprinciple on 2019-06-03.
 */
val Any.TAG: String
    get() = this::class.java.simpleName

fun Any.log(content: String) {
    Log.i("UpdateAppUtils", content)
}