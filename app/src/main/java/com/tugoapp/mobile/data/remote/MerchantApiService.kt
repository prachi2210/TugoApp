package com.tugoapp.mobile.data.remote

import com.tugoapp.mobile.data.remote.model.request.*
import com.tugoapp.mobile.data.remote.model.response.*
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface MerchantApiService { //
 
    @POST("getCategories")
    fun doGetCategories(@Header("token") token: String?): Call<GetCategoryResponseModel>

    @POST("saveUserDetails")
    fun doSaveUserDetail(@Header("token") token: String?, @Body data : SaveUserDetailRequestModel): Call<BaseResponseModel>

    @POST("getProviders")
    fun doGetProviders(@Header("token") token: String?, @Body data : GetProvidersRequestModel): Call<GetProvidersResponseModel>

    @POST("getProviderDetailsAdvace")
    fun doGetProviderDetails(@Header("token") token: String?, @Body data : GetProviderDetailRequestModel): Call<GetProviderDetailsResponseModel>

    @POST("placeOrder")
    fun doPlaceOrder(@Header("token") token: String?, @Body data : PlaceOrderRequestModel?): Call<PlaceOrderResponseModel>

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

    @POST("filterProviders")
    fun doFilterProviders(@Header("token") token: String?, @Body model : GetFilterProviderRequestModel?): Call<GetProvidersResponseModel>

    @POST("getFilterData")
    fun doGetFilterData(@Header("token") token: String?): Call<GetFilterDataResponseModel>

    @POST("addReview")
    fun doAddReview(@Header("token") token: String?,  @Body model : AddReviewRequestModel?): Call<BaseResponseModel>

    @POST("getReviews")
    fun doGetReview(@Header("token") token: String?,  @Body model : GetReviewRequestModel?): Call<GetReviewResponseModel>

    @POST("countryCodes")
    fun doGetCountryCodes(): Call<GetCountryCodesResponseModel>

    @POST("getUserDetails")
    fun doGetUserDetails(@Header("token") token: String?): Call<GetUserDetailResponseModel>

    @POST("updateNotificationSetting")
    fun doUpdateNotificationSetting(@Header("token") token: String?, @Body model : UpdateNotiRequestModel?): Call<BaseResponseModel>

    @POST("addToFavourite")
    fun doAddToFavourite(@Header("token") token: String?, @Body model : SetFavirouteProviderRequestModel?): Call<BaseResponseModel>

    @POST("saveDeviceToken")
    fun doSaveDeviceToken(@Header("token") token: String?, @Body model : SaveDeviceTokenRequestModel?): Call<BaseResponseModel>

    @POST("logout")
    fun doServerLogout(@Header("token") token: String?, @Body deviceId : LogoutRequestModel?): Call<BaseResponseModel>

    @POST("getFavourites")
    fun doGetFavorites(@Header("token") token: String?): Call<FavoriteResponseModel>

    @POST("submitQuery")
    fun doSubmitQuery(@Header("token") token: String?, @Body model : SubmitQueryRequestModel?): Call<BaseResponseModel>

    @GET("getPaymentConfig")
    fun doGetPaymentConfig(@Header("token") token: String?): Call<PaymentConfigResponseModel>

    @GET("isUserExists")
    fun isUserExist(@Header("token") token: String?): Call<GetUserDetailResponseModel>

}



