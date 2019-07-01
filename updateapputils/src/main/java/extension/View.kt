package extension

import android.view.View

/**
 * desc: View 相关扩展
 * time: 2019/6/27
 * @author teprinciple
 */
fun View.visibleOrGone(show: Boolean){
    if (show){
        this.visibility = View.VISIBLE
    }else{
        this.visibility = View.GONE
    }
}