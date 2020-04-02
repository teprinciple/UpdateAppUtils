package update
import androidx.core.content.FileProvider
import extension.log
import extension.yes
import util.GlobalContextProvider

/**
 * desc: UpdateFileProvider
 * time: 2019/7/10
 * @author Teprinciple
 */
class UpdateFileProvider : FileProvider() {
    override fun onCreate(): Boolean {
        val result = super.onCreate()
        (GlobalContextProvider.mContext == null && context != null).yes {
            GlobalContextProvider.mContext = context
            log("内部Provider初始化context：" + GlobalContextProvider.mContext)
        }
        return result
    }
}