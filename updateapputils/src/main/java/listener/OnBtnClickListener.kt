package listener

/**
 * desc: 按钮点击监听
 * time: 2019/9/16
 * @author teprinciple
 */
interface OnBtnClickListener {

    /**
     * 按钮点击
     * @return 是否消费事件
     */
    fun onClick(): Boolean
}