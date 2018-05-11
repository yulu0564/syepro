package com.yulu.kotlindemo.api

import io.reactivex.Observable
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

/**
 * 网络请求
 */

interface JyApi {
    //    //**************************************    订单  end    ******************************************************/
    //    @GET("comments_list_interface")
    //    Observable<ResultBean<List<Comments>>> getOrderList(@Query("newsid") String newsid,
    //                                                        @Query("page") int page, @Query("rows") int rows);
    //
    //    @FormUrlEncoded
    //    @POST("add_comments")
    //    Observable<ResultBean<Comments>> post(@Field("newsid") String newsid, @Field("userid") String userid,
    //                                          @Field("contects") String contects);

    @Multipart
    @POST(value = APIAddressConstants.BASE_CLOUD)
    fun uploadMultipart(@Part file: MultipartBody.Part): Observable<Response<String>>

}
