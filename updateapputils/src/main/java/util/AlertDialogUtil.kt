package util

import android.app.Activity
import android.app.AlertDialog

/**
 * desc: AlertDialogUtil
 * time: 2018/8/20
 * @author yk
 */
object AlertDialogUtil {

    fun show(
        activity: Activity,
        message: String,
        onCancelClick: () -> Unit = {},
        onSureClick: () -> Unit = {},
        cancelable: Boolean = false,
        title: String = "提示",
        cancelText: String = "取消",
        sureText: String = "确认"
    ) {

        AlertDialog.Builder(activity)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton(sureText) { _, _ ->
                onSureClick.invoke()
            }
            .setNegativeButton(cancelText) { _, _ ->
                onCancelClick.invoke()
            }
            .setCancelable(cancelable)
            .create()
            .show()
    }
}