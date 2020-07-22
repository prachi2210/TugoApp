package com.tugoapp.mobile.data.remote.model.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class BaseResponseModel {
    @SerializedName("status")
    var isSuccess = false

    @SerializedName("code")
    var code = 0

    @Expose
    @SerializedName("message")
    var message: String? = null
}