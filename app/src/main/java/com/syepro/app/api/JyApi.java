package com.syepro.app.api;

import com.syepro.app.api.network.Comments;
import com.syepro.app.api.network.ResultBean;

import java.util.List;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

/**
 * 网络请求
 */

public interface JyApi {
    //**************************************    订单  end    ******************************************************/
    @GET("comments_list_interface")
    Observable<ResultBean<List<Comments>>> getOrderList(@Query("newsid") String newsid,
                                                        @Query("page") int page, @Query("rows") int rows);
    @FormUrlEncoded
    @POST("add_comments")
    Observable<ResultBean<Comments>> post(@Field("newsid") String newsid, @Field("userid") String userid,
                                          @Field("contects") String contects);

//    @Multipart
//    @POST(APIAddressConstants.settingControl.uploadMultyFormFile)
    Observable<Response<String>> uploadMultipart(@Part MultipartBody.Part file);

}
