package com.tugoapp.mobile.data.remote

import com.tugoapp.mobile.data.remote.model.request.GetProviderDetailRequestModel
import com.tugoapp.mobile.data.remote.model.request.GetProvidersRequestModel
import com.tugoapp.mobile.data.remote.model.request.SaveUserDetailRequestModel
import com.tugoapp.mobile.data.remote.model.response.BaseResponseModel
import com.tugoapp.mobile.data.remote.model.response.GetCategoryResponseModel
import com.tugoapp.mobile.data.remote.model.response.GetProvidersResponseModel
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
import java.util.*
import javax.security.auth.callback.Callback

interface MerchantApiService { //
    //    @POST("RegisterItems/GetInventoryList")
    //    Call<GetSkuListResponseModel> doGetSKUList(@Body GetSkuListRequestModel model);
//
//    @POST("getCategories")
//    Call<Get> doGetCategories(@Header String token);

    @POST("getCategories")
    fun doGetCategories(@Header("token") token: String?): Call<GetCategoryResponseModel>

    @POST("saveUserDetails")
    fun doSaveUserDetail(@Header("token") token: String?, @Body data : SaveUserDetailRequestModel): Call<BaseResponseModel>

    @POST("getProviders")
    fun doGetProviders(@Header("token") token: String?, @Body data : GetProvidersRequestModel): Call<GetProvidersResponseModel>
}