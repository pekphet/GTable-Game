package cc.fish91.gtable.base.net

import android.content.Context
import android.net.ConnectivityManager
import android.os.Handler
import cc.fish91.gtable.base.net.exception.FishNetException
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.lenovo.lcui.base.net.Requester
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import javax.net.ssl.HttpsURLConnection

/**
 * Created by fish on 18-2-27.
 */
class NetCore<Result> {
    companion object {
        val IO_EXCEPTION = "IO-EXCEPTION"
    }
    fun doGet(request: Requester<Result>, h: Handler, success: Result?.() -> Unit, failed: String.() -> Unit) {
        request.mUrl.let {
            if (!it.contains('?')) {
                request.mUrl += "?"
            }
            if (request.mUParam.isNotEmpty())
                request.mUrl += map2reqBody(request.mUParam)
        }
        getConnetion(request).apply {
            requestMethod = "GET"
            doOutput = false
            try {
                connect()
            } catch (ex: Exception) {
                ex.printStackTrace()
                h.post {
                    failed(IO_EXCEPTION)
                }
                return@apply
            }
            doNet(request.mType, request.mTypeToken, h, success, failed)
        }
    }

    fun doPost(request: Requester<Result>, h: Handler, success: Result?.() -> Unit, failed: String.() -> Unit) {
        getConnetion(request).apply {
            requestMethod = "POST"
            doOutput = true
            try {
                connect()
            } catch (ex: Exception) {
                ex.printStackTrace()
                h.post {
                    failed(IO_EXCEPTION)
                }
                return@apply
            }
            outputStream.apply {
                if (request.mBody.isNotEmpty())
                    write(map2reqBody(request.mBody).toByteArray())
                else if (request.mJsonBody.isNotEmpty())
                    write(request.mJsonBody.toByteArray())
                flush()
                close()
            }
            doNet(request.mType, request.mTypeToken, h, success, failed)
        }
    }

    fun map2reqBody(data: Map<String, String>) = data.map { "${it.key}=${it.value}" }.reduce { ori, add -> "$ori&$add" }

    private fun HttpURLConnection.doNet(type: Class<Result>?, typeToken: TypeToken<Result>?, h: Handler, success: Result?.() -> Unit, failed: String.() -> Unit) {
        val respCode = responseCode
        if (respCode != 200) {
//            ZInfoRecorder.e("http requester", "url: $url   response code: $respCode")
            h.post {
                FishNetException(respCode).getReasonMessage().failed()
            }
            return
        }
        try {
            val data = IS2Str(inputStream)
            h.post {
                val s = if (type == null) {
                    if (typeToken == null) {
                        null
                    } else {
                        Gson().fromJson<Result>(data, typeToken.type)
                    }
                } else if (type.name.equals("java.lang.String")) {
                    (data as? Result ?: null).success()
                    return@post
                } else Gson().fromJson<Result>(data, type)
                s.success()
            }
        } catch (ex: IOException) {
            ex.printStackTrace()
            h.post {
                "I/O错误".failed()
            }
        } catch (ex: TypeCastException) {
            ex.printStackTrace()
            h.post {
                "数据结构错误".failed()
            }
        }
    }

    private fun getConnetion(request: Requester<*>) = URL(request.mUrl.toString()).openConnection().apply {
        if (request.mSSLContext != null && this is HttpsURLConnection)
            sslSocketFactory = request.mSSLContext?.socketFactory
        connectTimeout = 3000
        readTimeout = 10000
        doInput = true
        useCaches = false
        setRequestProperty("Charset", "UTF-8")
        setRequestProperty("Connection", "Keep-Alive")
        setRequestProperty("Content-Type", if (request.mIsJsonBody) "application/json" else "application/x-www-form-urlencoded")
        request.mHeader.map { addRequestProperty(it.key, it.value) }
    } as HttpURLConnection

    private fun IS2Str(inputStream: InputStream): String {
        val reader = InputStreamReader(inputStream, "UTF-8")
        val buf = CharArray(1024)
        val sb = StringBuffer()
        var readCnt: Int
        try {
            do {
                readCnt = reader.read(buf)
                if (readCnt > 0)
                    sb.append(buf, 0, readCnt)
            } while (readCnt >= 0)
            return sb.toString()
        } catch (ex: Exception) {
            ex.printStackTrace()
            return ""
        } finally {
            try {
                reader.close()
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }
    }
}

object NetUtils {
    fun hasNet(ctx: Context?) = (ctx?.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager)?.activeNetworkInfo?.isAvailable
            ?: false
}