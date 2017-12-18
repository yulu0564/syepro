package com.syepro.app.api;

import com.syepro.app.commonjar.utils.LogUtils;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import io.reactivex.schedulers.Schedulers;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class JyAPIUtil {
    public static JyApi jyApi = getService();
    private static Retrofit retrofit;
    private static OkHttpClient okHttpClient = initOkHttp();

    private static JyApi getService() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(APIAddressConstants.BASE_CLOUD)
                    .client(genericClient())
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            jyApi = retrofit.create(JyApi.class);
        }
        return jyApi;
    }

    public static OkHttpClient genericClient() {
        if (okHttpClient == null) {
            okHttpClient = initOkHttp();
        }
        return okHttpClient;
    }

    private static OkHttpClient initOkHttp() {
        return new OkHttpClient.Builder()
                .connectTimeout(20000L, TimeUnit.MILLISECONDS)//超时
                .readTimeout(20000L, TimeUnit.MILLISECONDS)
                .addInterceptor(getCommonHeaderInterceptor())//配置通用header
                .addInterceptor(getLogInterceptor())//打印http整个请求的Log
                .build();
    }

    /**
     * 打印retrofit日志
     */
    private static HttpLoggingInterceptor getLogInterceptor() {
        return new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                LogUtils.error("RetrofitLog", "retrofitBack = " + message);
            }
        }).setLevel(HttpLoggingInterceptor.Level.BODY);
    }

    /**
     * 增加通用的请求头
     */
    private static Interceptor getCommonHeaderInterceptor() {
        return new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                //增加拦截器，用以加入公用头部
                Request.Builder builder = chain.request().newBuilder();
//                builder.headers(Headers.of(CommonHeaders.getCommonHeaders()));
                Request request = builder.build();
                return chain.proceed(request);
            }
        };
    }


}
