package util

import android.annotation.SuppressLint
import android.content.Context

/**
 * desc: 提供context.
 */
@SuppressLint("StaticFieldLeak")
object GlobalContextProvider {

    internal lateinit var mContext: Context

    /**
     *  获取全局context
     */
    fun getGlobalContext(): Context = mContext
}