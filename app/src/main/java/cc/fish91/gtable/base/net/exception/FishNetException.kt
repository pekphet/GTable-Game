package cc.fish91.gtable.base.net.exception

/**
 * Created by fish on 18-2-27.
 */
class FishNetException(responseCode: Int) : Exception() {
    var mResponseCode = responseCode
    fun getReasonMessage() = when (mResponseCode) {
        in 300..399 -> "${mResponseCode}重定向"
        400 -> "400 请求错误"
        401 -> "401 身份验证错误"
        403 -> "403 请求被禁止"
        404 -> "404 页面未找到"
        405 -> "405 请求方式错误"
        408 -> "408 页面超时"
        500 -> "500 服务器内部错误"
        501 -> "501 不可实现的请求"
        502 -> "502 网关错误"
        503 -> "503 服务不可用"
        504 -> "504 网关超时"
        else -> "${mResponseCode}未知错误"
    }
}
