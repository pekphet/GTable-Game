package cc.fish91.gtable.base.net

import com.google.gson.Gson
import cc.fish91.gtable.base.net.exception.FishNetException
import com.lenovo.lcui.base.net.Requester
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.lang.reflect.Type
import java.net.HttpURLConnection
import java.net.URL
import javax.net.ssl.HttpsURLConnection

class SyncNetCore<T> {
    private val CONNECT_TIMEOUT = 3000
    private val READ_TIMEOUT = 10000

    suspend fun syncGet(requester: Requester<T>): ResponseJson {
        requester.makeUrl()

        return getConnection(requester).apply {
            requestMethod = "GET"
            doOutput = false
            try {
                connect()
            } catch (ex: Exception) {
                ex.printStackTrace()
                return ex.makeResponseJson()
            }
        }.request()
    }

    suspend fun syncPost(requester: Requester<T>): ResponseJson {
        return getConnection(requester).apply {
            requestMethod = "POST"
            doOutput = true
            try {
                connect()
            } catch (ex: Exception) {
                ex.printStackTrace()
                return ResponseJson("{}", ex.message ?: "", ex)
            }
            outputStream.apply {
                if (requester.mBody.isNotEmpty())
                    write(map2reqBody(requester.mBody).toByteArray())
                else if (requester.mJsonBody.isNotEmpty())
                    write(requester.mJsonBody.toByteArray())
                flush()
                close()
            }
        }.request()
    }

    private suspend fun HttpURLConnection.request(): ResponseJson {
        var respCode = 0
        try {
            respCode = responseCode
        } catch (ex: Exception) {
            ex.printStackTrace()
            return ex.makeResponseJson()
        }
        if (respCode != 200) {
            try {
                val json = inputStream.loadString()
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
            return FishNetException(responseCode).makeResponseJson()
        }
        return try {
            val json = inputStream.loadString()
            ResponseJson(json)
        } catch (ex: IOException) {
            ex.printStackTrace()
            ex.makeResponseJson()
        }
    }

    private fun getConnection(request: Requester<*>) = URL(request.mUrl).openConnection().apply {
        if (request.mSSLContext != null && this is HttpsURLConnection) {
            sslSocketFactory = request.mSSLContext?.socketFactory
        }
        connectTimeout = CONNECT_TIMEOUT
        readTimeout = READ_TIMEOUT
        doInput = true
        useCaches = false
        setRequestProperty("Charset", "UTF-8")
        setRequestProperty("Connection", "Keep-Alive")
        request.mHeader.map { addRequestProperty(it.key, it.value) }
        if (!request.tmpIsGet) {
            setRequestProperty(
                "Content-Type",
                if (request.mIsJsonBody) "application/json" else "application/x-www-form-urlencoded"
            )
        }
    } as HttpURLConnection

    private suspend fun InputStream.loadString(): String {
        return GlobalScope.async {
            val reader = InputStreamReader(this@loadString, "UTF-8")
            try {
                val buf = CharArray(1024)
                val sb = StringBuffer()
                do {
                    val readCnt = reader.read(buf)
                    if (readCnt > 0)
                        sb.append(buf, 0, readCnt)
                } while (readCnt >= 0)
                return@async sb.toString()
            } finally {
                try {
                    reader.close()
                } catch (ex: Exception) {
                    ex.printStackTrace()
                }
            }
        }.await()
    }
}

data class ResponseData<T>(var data: T?, var errMsg: String = "", var exception: Exception? = null)
data class ResponseJson(var json: String, var errMsg: String = "", var exception: Exception? = null)

fun <T> ResponseJson.toData(type: Type): ResponseData<T> {
    return try {
        ResponseData<T>(Gson().fromJson(json, type), errMsg, exception)
    } catch (ex: Exception) {
        ResponseData(null, errMsg, exception)
    }
}

fun <T> ResponseJson.toData(clz: Class<T>): ResponseData<T> {
    return try {
        ResponseData<T>(Gson().fromJson(json, clz), errMsg, exception)
    } catch (ex: Exception) {
        ResponseData(null, errMsg, exception)
    }
}

fun Exception.makeResponseJson() = ResponseJson("{}", message ?: "", this)

fun map2reqBody(data: Map<String, String>) =
    data.map { "${it.key}=${it.value}" }.reduce { ori, add -> "$ori&$add" }