package util

import android.annotation.SuppressLint
import android.content.Context

/**
 * desc: 提供context.
 */
@SuppressLint("StaticFieldLeak")
internal object GlobalContextProvider {

    /** 全局context 提供扩展globalContext */
    internal var mContext: Context? = null
}