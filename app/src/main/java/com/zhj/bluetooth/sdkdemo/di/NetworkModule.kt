package com.zhj.bluetooth.sdkdemo.di

import android.os.Build
import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.Request
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.zhj.bluetooth.sdkdemo.BuildConfig
import com.zhj.bluetooth.sdkdemo.data.source.remote.UploadBleApi
import okhttp3.logging.HttpLoggingInterceptor
import java.security.KeyStore
import java.util.*
import java.util.concurrent.TimeUnit
import javax.net.ssl.*

val networkModule = module {
    single { CommonHttpClient() }
    single { LongTimeHttpClient() }
    single { getCjmService<UploadBleApi>(get()) }
}
private const val CLIENT_NORMAL = 1
private const val CLIENT_LONG_TIMEOUT = 2

fun okHttpClientBuilder(clinet: Int): OkHttpClient.Builder {

    val builder = OkHttpClient.Builder()
    if (clinet == CLIENT_LONG_TIMEOUT) {
        builder.connectTimeout(1, TimeUnit.MINUTES)
            .readTimeout(1, TimeUnit.MINUTES)
            .writeTimeout(1, TimeUnit.MINUTES)
    } else {
        builder.connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(20, TimeUnit.SECONDS)
    }


    // 基於 performance 及 memory 考慮, 只會在 debug mode 情況下才會加 HttpLoggingInterceptor
    when (BuildConfig.DEBUG) {
        true -> {
            builder.addInterceptor(
                HttpLoggingInterceptor(HttpLoggingInterceptor.Logger.DEFAULT).setLevel(
                    HttpLoggingInterceptor.Level.BODY
                )
            )
        }
    }

    return when {
        Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT -> {
            val trustManagerFactory: TrustManagerFactory =
                TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm())
            trustManagerFactory.init(null as KeyStore?)
            val trustManagers: Array<TrustManager> = trustManagerFactory.getTrustManagers()
            check(!(trustManagers.size != 1 || trustManagers[0] !is X509TrustManager)) {
                "Unexpected default trust managers:" + Arrays.toString(
                    trustManagers
                )
            }
            val trustManager: X509TrustManager = trustManagers[0] as X509TrustManager
            val sslContext: SSLContext = SSLContext.getInstance("SSL")
            sslContext.init(null, arrayOf<TrustManager>(trustManager), null)
            val sslSocketFactory: SSLSocketFactory = sslContext.socketFactory
            builder.sslSocketFactory(sslSocketFactory, trustManager)
        }
        else -> builder
    }
}

class CommonHttpClient {
    val builder = okHttpClientBuilder(CLIENT_NORMAL)
        .addInterceptor { chain ->
            val original = chain.request()
            val request = original.newBuilder()
                .headerCommon()
                .header("Authorization", "Bearer " + getTokenOrRefreshToken(chain.request().url().toString()))
                .method(original.method(), original.body())
                .build()
            chain.proceed(request)
        }
//        .addInterceptor(RefreshTokenInterceptor())
        .build()

}

//长时间timeout的
class LongTimeHttpClient {
    val builder = okHttpClientBuilder(CLIENT_LONG_TIMEOUT)
        .addInterceptor { chain ->
            val original = chain.request()
            val request = original.newBuilder()
                .headerCommon()
//                .header("Authorization", "Bearer " + getTokenOrRefreshToken(chain.request().url().toString()))
                .method(original.method(), original.body())
                .build()
            chain.proceed(request)
        }
//        .addInterceptor(RefreshTokenInterceptor())
        .build()


}

private fun getTokenOrRefreshToken(url: String) =
    when (url.endsWith("user/refreshToken")) {
        true -> ""
        else -> ""
    }

inline fun <reified T> getCjmService(okHttpClient: CommonHttpClient, url: String = BuildConfig.BASE_URL): T {
    val retrofit = Retrofit.Builder()
        .baseUrl(url)
        .client(okHttpClient.builder)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    return retrofit.create(T::class.java)
}


inline fun <reified T> getCjmLongTimeService(okHttpClient: LongTimeHttpClient, url: String = BuildConfig.BASE_URL): T {
    val retrofit = Retrofit.Builder()
        .baseUrl(url)
        .client(okHttpClient.builder)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    return retrofit.create(T::class.java)
}

fun Request.Builder.headerCommon() =
    this.header("platform", "android")
        .header("OSType", "2")
        .header("versionCode", BuildConfig.VERSION_NAME)
        .header("clientVersion", BuildConfig.VERSION_NAME)

