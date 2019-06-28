package update

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import extension.yes

/**
 * desc: NotificationReceiver
 * time: 2019/6/28
 * @author yk
 */
class NotificationReceiver : BroadcastReceiver(){

    override fun onReceive(context: Context, intent: Intent) {
        (intent.action == ACTION_RE_DOWNLOAD).yes {

        }
    }

    companion object{
        const val ACTION_RE_DOWNLOAD = "ACTION_RE_DOWNLOAD"
    }
}