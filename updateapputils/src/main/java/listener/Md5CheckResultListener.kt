package listener

/**
 * desc: Md5校验结果回调
 * time: 2019/6/21
 * @author teprinciple
 */
interface Md5CheckResultListener {
    fun onResult(result: Boolean)
}