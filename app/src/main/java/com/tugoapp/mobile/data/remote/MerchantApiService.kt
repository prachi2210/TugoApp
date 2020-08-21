package com.tugoapp.mobile.data.remote

import com.tugoapp.mobile.data.remote.model.request.*
import com.tugoapp.mobile.data.remote.model.response.*
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

    @POST("getProviderDetailsAdvace")
    fun doGetProviderDetails(@Header("token") token: String?, @Body data : GetProviderDetailRequestModel): Call<GetProviderDetailsResponseModel>

    @POST("placeOrder")
    fun doPlaceOrder(@Header("token") token: String?, @Body data : PlaceOrderRequestModel?): Call<BaseResponseModel>

    @POST("searchProviders")
    fun doSearchTermForProvider(@Header("token") token: String?, @Body data : GetProvidersRequestModel): Call<GetProvidersResponseModel>

    @POST("saveAddress")
    fun doAddAddress(@Header("token") token: String?, @Body data : AddAddressRequestModel): Call<BaseResponseModel>

    @POST("updateAddress")
    fun doUpdateAddress(@Header("token") token: String?, @Body data : UpdateAddressRequestModel): Call<BaseResponseModel>

    @POST("getAddressData")
    fun doGetAddressList(@Header("token") token: String?): Call<GetAddressResponseModel>

    @POST("removeAddress")
    fun doDeleteAddress(@Header("token") token: String?, @Body data : DeleteAddressRequestModel): Call<BaseResponseModel>

    @POST("getActiveOrders")
    fun doGetOngoingOrders(@Header("token") token: String?): Call<OrdersResponseModel>

    @POST("getPastOrders")
    fun doGetHistoryOrders(@Header("token") token: String?): Call<OrdersResponseModel>

    @POST("pauseOrder")
    fun doPauseOrder(@Header("token") token: String?, @Body  model : ResumeOrderRequestModel?): Call<BaseResponseModel>

        @POST("resumeOrder")
    fun doResumeOrder(@Header("token") token: String?, @Body model : ResumeOrderRequestModel?): Call<BaseResponseModel>

    @POST("cancelOrder")
    fun doCancelPlan(@Header("token") token: String?, @Body model : ResumeOrderRequestModel?): Call<BaseResponseModel>
}