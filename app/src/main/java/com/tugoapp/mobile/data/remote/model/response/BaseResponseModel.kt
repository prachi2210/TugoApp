package com.tugoapp.mobile.data.remote.model.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

open class BaseResponseModel {
    @SerializedName("success")
    var isSuccess: Int? = 0

    @Expose
    @SerializedName("message")
    var message: String? = null

    @Expose
    @SerializedName("addressId")
    var addressId: String? = null

    @Expose
    @SerializedName("orderNo")
    var orderNo: String? = null

    @Expose
    @SerializedName("orderId")
    var orderId: Int? = 0
}