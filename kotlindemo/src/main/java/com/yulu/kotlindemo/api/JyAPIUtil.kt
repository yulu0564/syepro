package com.yulu.kotlindemo.api


import com.yulu.kotlindemo.utils.LogUtils
import io.reactivex.schedulers.Schedulers
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit

object JyAPIUtil {
    var jyApi = service
    private var retrofit: Retrofit? = null
    private var okHttpClient: OkHttpClient? = initOkHttp()

    private val service: JyApi
        get() {
            if (retrofit == null) {
                retrofit = Retrofit.Builder()
                        .baseUrl(APIAddressConstants.BASE_CLOUD)
                        .client(genericClient())
                        .addConverterFactory(ScalarsConverterFactory.create())
                        .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                        .addConverterFactory(GsonConverterFactory.create())
                        .build()
                retrofit.let{
                    if (it != null) {
                        jyApi = it.create(JyApi::class.java)
                    }
                }
            }
            return jyApi
        }

    /**
     * 打印retrofit日志
     */
    private val logInterceptor: HttpLoggingInterceptor
        get() = HttpLoggingInterceptor(HttpLoggingInterceptor.Logger { message -> LogUtils.error("RetrofitLog", "retrofitBack = " + message) }).setLevel(HttpLoggingInterceptor.Level.BODY)

    /**
     * 增加通用的请求头
     */
    private//增加拦截器，用以加入公用头部
            //                builder.headers(Headers.of(CommonHeaders.getCommonHeaders()));
    val commonHeaderInterceptor: Interceptor
        get() = Interceptor { chain ->
            val builder = chain.request().newBuilder()
            val request = builder.build()
            chain.proceed(request)
        }

    fun genericClient(): OkHttpClient {
        if (okHttpClient == null) {
            okHttpClient = initOkHttp()
        }
        return okHttpClient as OkHttpClient
    }

    private fun initOkHttp(): OkHttpClient {
        return OkHttpClient.Builder()
                .connectTimeout(20000L, TimeUnit.MILLISECONDS)//超时
                .readTimeout(20000L, TimeUnit.MILLISECONDS)
                .addInterceptor(commonHeaderInterceptor)//配置通用header
                .addInterceptor(logInterceptor)//打印http整个请求的Log
                .build()
    }


}
