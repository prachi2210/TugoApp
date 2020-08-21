package com.tugoapp.mobile.data.remote.model.response

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

public data class OrdersResponseModel(var data : ArrayList<OrderModel>?) : BaseResponseModel() {}

@Parcelize
public data class OrderModel(var orderNo : String?,var address : String?,var deliveryLocation : String?,
                            var deliveryTime : String?,var planId : String?,var planName : String?,
                             var businessId : String?,var companyName : String?,var companyLogo : String?,
                             var price : String?,var startedOn : String?,var orderPlacedAt : String?,
                             var instructions : String?,var noOfMeals : String?,var noOfWeeks : String?,var noOfDays : String?,
                             var orderId : String?, var expiredOn : String?, var paymentType : String?,
                             var isDefault : Boolean, var isPaused : Boolean) : Parcelable {}
