package util

import android.content.ContentProvider
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.support.annotation.Nullable

/**
 * desc: 通过ContentProvider提供context.
 * time: 2018/8/13
 */
internal class GlobalContextProvider : ContentProvider() {

    private var mContext: Context? = null

    companion object {
        private val instance by lazy { GlobalContextProvider() }
        /**
         *  获取全局context
         */
        fun getGlobalContext(): Context {
            return instance.mContext!!
        }
    }

    override fun onCreate(): Boolean {
        instance.mContext = context.applicationContext
        return true
    }

    @Nullable
    override fun insert(uri: Uri?, values: ContentValues?): Uri? {
        return null
    }

    @Nullable
    override fun query(
        uri: Uri?,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?
    ): Cursor? {
        return null
    }

    override fun update(uri: Uri?, values: ContentValues?, selection: String?, selectionArgs: Array<out String>?): Int {
        return 0
    }

    override fun delete(uri: Uri?, selection: String?, selectionArgs: Array<out String>?): Int {
        return 0
    }

    @Nullable
    override fun getType(uri: Uri?): String? {
        return null
    }
}