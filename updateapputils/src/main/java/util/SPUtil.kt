package util

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import java.util.*

/**
 * SharedPreferences 数据保存
 */
internal object SPUtil {

    fun putBase(keyName: String, value: Any): Boolean {
        val sharedPreferences = getSp()
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        when (value) {
            is Int -> editor.putInt(keyName, value)
            is Boolean -> editor.putBoolean(keyName, value)
            is Float -> editor.putFloat(keyName, value)
            is String -> editor.putString(keyName, value)
            is Long -> editor.putLong(keyName, value)
            else -> throw IllegalArgumentException("SharedPreferences can,t be save this type")
        }
        return editor.commit()
    }

    fun putStringSet(keyName: String, value: Set<String>): Boolean {
        val sharedPreferences = getSp()
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putStringSet(keyName, value)
        return editor.commit()
    }

    /**
     * 保存set列表
     * @param context
     * @param key
     * @param value
     */
    @SuppressLint("ApplySharedPref")
    fun putToStringSet(context: Context, key: String, value: String) {
        val sharedPreferences = getSp()
        // 先获取之前的set
        val hashSet = sharedPreferences!!.getStringSet(key, HashSet()) as HashSet<String>
        val newSet = HashSet(hashSet)
        newSet.add(value)
        sharedPreferences!!.edit().putStringSet(key, newSet).commit()
    }

    /**
     * 获取set列表
     * @param context
     * @param key
     * @return
     */
    fun getFromStringSet(context: Context, key: String): HashSet<String> {
        val sharedPreferences = getSp()
        return sharedPreferences!!.getStringSet(key, HashSet()) as HashSet<String>
    }

    fun getInt(keyName: String, defaultValue: Int = -1): Int {
        val sharedPreferences = getSp()
        return sharedPreferences.getInt(keyName, defaultValue)
    }

    fun getBoolean(keyName: String, defaultValue: Boolean = false): Boolean {
        val sharedPreferences = getSp()
        return sharedPreferences.getBoolean(keyName, defaultValue)
    }

    fun getFloat(keyName: String, defaultValue: Float = -1F): Float {
        val sharedPreferences = getSp()
        return sharedPreferences.getFloat(keyName, defaultValue)
    }

    fun getString(keyName: String, defaultValue: String? = null): String {
        val sharedPreferences = getSp()
        return sharedPreferences.getString(keyName, defaultValue)
    }

    fun getLong(keyName: String, defaultValue: Long = -1L): Long {
        val sharedPreferences = getSp()
        return sharedPreferences.getLong(keyName, defaultValue)
    }

    fun getStringSet(keyName: String, defaultValue: Set<String>? = null): Set<String>? {
        val sharedPreferences = getSp()
        return sharedPreferences.getStringSet(keyName, defaultValue)
    }

    fun removeKeyName(keyName: String): Boolean {
        val sharedPreferences = getSp()
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.remove(keyName)
        return editor.commit()
    }

    fun clearTableName(): Boolean {
        val sharedPreferences = getSp()
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.clear()
        return editor.commit()
    }

    fun getSp(): SharedPreferences {
        val context = GlobalContextProvider.getGlobalContext()
        return context.getSharedPreferences(context.packageName, Activity.MODE_PRIVATE)
    }
}