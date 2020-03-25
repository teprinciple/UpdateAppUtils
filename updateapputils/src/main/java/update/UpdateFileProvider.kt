package update

import android.support.v4.content.FileProvider
import util.GlobalContextProvider

/**
 * desc: UpdateFileProvider
 * time: 2019/7/10
 * @author Teprinciple
 */
class UpdateFileProvider : FileProvider(){
    override fun onCreate(): Boolean {
        GlobalContextProvider.mContext = context!!
        return super.onCreate()
    }
}