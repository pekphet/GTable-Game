package com.lenovo.lcui.base.net

import android.content.Context
import android.os.Handler
import android.os.Looper
import cc.fish91.gtable.base.net.*
import com.google.gson.reflect.TypeToken
import cc.fish91.gtable.base.net.extensions.ThreadPool
import java.io.InputStream
import java.lang.ref.WeakReference
import java.net.URLEncoder
import java.security.KeyStore
import java.security.SecureRandom
import java.security.cert.CertificateFactory
import java.security.cert.X509Certificate
import javax.net.ssl.KeyManagerFactory
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManagerFactory
import javax.net.ssl.X509TrustManager

/**
 * Created by fish on 18-2-27.
 */
class Requester<Result>() {
    lateinit var mUrl: String
    var mUParam = mutableMapOf<String, String>()
    var mBody = mutableMapOf<String, String>()
    var mHeader = mutableMapOf<String, String>()
    var mIsJsonBody = false
    var mJsonBody = ""
    var mSSLContext: SSLContext? = null
    var mType: Class<Result>? = null
    var mTypeToken: TypeToken<Result>? = null

    constructor(url: String, type: Class<Result>) : this() {
        mUrl = url
        mType = type
    }

    constructor(url: String, typeToken: TypeToken<Result>) : this() {
        mUrl = url
        mTypeToken = typeToken
    }

    fun url(u: String) {
        mUrl = u
    }

    fun urlParam(key: String, value: Any) {
        mUParam.put(key, URLEncoder.encode(value.toString(), "UTF-8"))
    }

    fun urlParam(params: Array<Pair<String, Any>>) {
        mUParam.putAll(params.associate {
            Pair(
                it.first,
                URLEncoder.encode(it.second.toString(), "UTF-8")
            )
        })
    }

    fun urlParam(params: Map<String, String>) {
        mUParam.clear()
        mUParam.putAll(params)
    }

    fun jsonBody(json: String) {
        mIsJsonBody = true
        mJsonBody = json
    }

    fun body(key: String, value: Any) {
        mBody.put(key, URLEncoder.encode(value.toString(), "UTF-8"))
    }

    fun body(params: Array<Pair<String, Any>>) {
        params.map { body(it.first, URLEncoder.encode(it.second.toString())) }
    }

    fun body(params: Map<String, String>) {
        mBody.putAll(params)
    }

    fun header(key: String, value: String) = mHeader.put(key, value)

    fun headers(headers: Map<String, String>) {
        mHeader.putAll(headers)
    }

    fun https(ctx: Context, pwdKey: String = "", pemFile: String?, p12File: String) {
        var keyIS: InputStream? = null
        var pemIS: InputStream? = null
        try {
            keyIS = ctx.assets.open(p12File)
            val keyStore =
                KeyStore.getInstance("PKCS12").apply { load(keyIS, pwdKey.toCharArray()) }
            val kmf =
                KeyManagerFactory.getInstance("X509").apply { init(keyStore, pwdKey.toCharArray()) }
            if (pemFile.isNullOrBlank()) {
                mSSLContext =
                    SSLContext.getInstance("TLS").apply { init(kmf.keyManagers, null, null) }
                return
            }
            pemIS = ctx.assets.open(pemFile)
            val cert =
                CertificateFactory.getInstance("X.509")
                    .generateCertificate(pemIS) as X509Certificate
            val trustStore = KeyStore.getInstance(KeyStore.getDefaultType())
            val tmf = TrustManagerFactory.getInstance("X509").apply { init(trustStore) }
            trustStore.apply {
                load(null)
                setCertificateEntry(cert.subjectX500Principal.name, cert)
            }
            mSSLContext = SSLContext.getInstance("TLS")
                .apply { init(kmf.keyManagers, tmf.trustManagers, null) }
        } catch (ex: Exception) {
            ex.printStackTrace()
            mSSLContext = null
        } finally {
            try {
                keyIS!!.close()
                pemIS!!.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
    }

    fun https(ctx: Context) {
        try {
            mSSLContext = SSLContext.getInstance("TLS").apply {
                init(null, arrayOf(object : X509TrustManager {
                    override fun checkClientTrusted(
                        chain: Array<out X509Certificate>?,
                        authType: String?
                    ) {}

                    override fun checkServerTrusted(
                        chain: Array<out X509Certificate>?,
                        authType: String?
                    ) {
                    }

                    override fun getAcceptedIssuers(): Array<X509Certificate> = arrayOf()
                }), SecureRandom())
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
            mSSLContext = null
        }
    }

    var tmpIsGet = true
    lateinit var tmpCtx: WeakReference<Context>
    lateinit var tmpHandler: WeakReference<Handler>
    lateinit var tmpSuccess: Result?.() -> Unit
    lateinit var tmpFailed: String.() -> Unit
    private fun request(
        isGet: Boolean,
        ctx: Context,
        handler: Handler,
        success: Result?.() -> Unit,
        failed: String.() -> Unit
    ) {
        tmpIsGet = isGet
        tmpCtx = WeakReference(ctx)
        tmpHandler = WeakReference(handler)
        tmpSuccess = success
        tmpFailed = failed
        if (!NetUtils.hasNet(tmpCtx.get()))
            "".failed()
        if (mUrl.isEmpty())
            "空请求".failed()
        ThreadPool.addTask {
            NetCore<Result>().run {
                if (isGet) doGet(this@Requester, handler, success, failed)
                else doPost(this@Requester, handler, success, failed)
            }
        }
    }

    fun retry() {
        try {
            if (tmpHandler.get() == null || tmpCtx.get() == null)
                return
            if (!NetUtils.hasNet(tmpCtx.get()))
                "无网络连接".tmpFailed()
            if (mUrl.isEmpty())
                "URL is EMPTY".tmpFailed()
            ThreadPool.addTask {
                NetCore<Result>().run {
                    if (tmpIsGet) doGet(this@Requester, tmpHandler.get()!!, tmpSuccess, tmpFailed)
                    else doPost(this@Requester, tmpHandler.get()!!, tmpSuccess, tmpFailed)
                }
            }
        } catch (e: UninitializedPropertyAccessException) {
            e.printStackTrace()
        } catch (ex: Exception) {
            ex.printStackTrace()
            if (this@Requester::tmpFailed.isInitialized) {
                "err".tmpFailed()
            }
        }
    }


    fun Get(
        ctx: Context,
        handler: Handler,
        success: Result?.() -> Unit,
        failed: String.() -> Unit
    ) =
        request(true, ctx, handler, success, failed)

    fun Get(ctx: Context, success: Result?.() -> Unit, failed: String.() -> Unit) =
        Get(ctx, Handler(Looper.getMainLooper()), success, failed)

    fun Post(
        ctx: Context,
        handler: Handler,
        success: Result?.() -> Unit,
        failed: String.() -> Unit
    ) =
        request(false, ctx, handler, success, failed)

    fun Post(ctx: Context, success: Result?.() -> Unit, failed: String.() -> Unit) =
        Post(ctx, Handler(Looper.getMainLooper()), success, failed)


    /****  FOR SYNC FUNCTIONS ****/
    fun makeUrl() {
        if (!mUrl.contains('?'))
            mUrl += '?'
        if (mUParam.isNotEmpty())
            mUrl += map2reqBody(mUParam)
    }

    private suspend fun syncRequest(isGet: Boolean, ctx: Context): ResponseJson {
        tmpIsGet = isGet
        tmpCtx = WeakReference(ctx)
        if (!NetUtils.hasNet(tmpCtx.get()))
            return RuntimeException("Network is ERROR").makeResponseJson()
        if (mUrl.isEmpty())
            return RuntimeException("NO URL REQUESTED!").makeResponseJson()
        SyncNetCore<Result>().let {
            if (isGet)
                return it.syncGet(this)
            else
                return it.syncPost(this)
        }
    }

    private suspend fun syncRequestObj(isGet: Boolean, ctx: Context) = syncRequest(isGet, ctx).run {
//        Log.e("resp", this.json)
        when {
            mType != null -> toData(mType!!)
            mTypeToken != null -> toData(mTypeToken!!.type)
            else -> ResponseData<Result>(null, "No Request Type Set", TypeCastException())
        }
    }


    suspend fun syncGet(ctx: Context) = syncRequest(true, ctx)
    suspend fun syncPost(ctx: Context) = syncRequest(false, ctx)
    suspend fun syncGetObj(ctx: Context) = syncRequestObj(true, ctx)
    suspend fun syncPostObj(ctx: Context) = syncRequestObj(false, ctx)

    suspend fun retry(ctx: Context) = syncRequest(tmpIsGet, ctx)
    suspend fun retryObj(ctx: Context) = syncRequestObj(tmpIsGet, ctx)
}