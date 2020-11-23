package com.tugoapp.mobile.data.remote.model.response

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

public data class PaymentConfigResponseModel(var data : PaymentConfigModel?) : BaseResponseModel() {}

@Parcelize
public data class PaymentConfigModel(var merchant_id : String?, var access_code : String?, var working_key : String?,  var redirectUrl : String?, var cancelUrl : String?, var getRSA : String?) : Parcelable{}




