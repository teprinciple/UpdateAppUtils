package update

import android.support.v4.content.FileProvider
import extension.log
import util.GlobalContextProvider

/**
 * desc: UpdateFileProvider
 * time: 2019/7/10
 * @author Teprinciple
 */
class UpdateFileProvider : FileProvider(){
    override fun onCreate(): Boolean {
        val result = super.onCreate()
        GlobalContextProvider.mContext = context
        log("初始化context："+GlobalContextProvider.mContext)
        return result
    }
}