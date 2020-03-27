package util

import android.content.Context

/**
 * desc: 提供context.
 */
internal object GlobalContextProvider {

    /** 全局context 提供扩展globalContext */
    internal var mContext: Context? = null
}