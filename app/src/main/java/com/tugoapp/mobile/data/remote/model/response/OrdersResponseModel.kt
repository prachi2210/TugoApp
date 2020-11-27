package com.tugoapp.mobile.data.remote.model.response

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

public data class OrdersResponseModel(var data : ArrayList<OrderModel>?) : BaseResponseModel() {}

@Parcelize
public data class OrderModel(var orderNo : String?,var address : String?,var deliveryLocation : String?,
                            var deliveryTime : String?,var planId : String?,var planName : String?,
                             var businessId : String?,var companyName : String?,var companyLogo : String?, var phoneNumber : String?,
                             var price : String?,var startedOn : String?,var orderPlacedAt : String?,
                             var instructions : String?,var noOfMeals : String?,var noOfWeeks : String?,var noOfDays : String?,
                             var orderId : String?, var expiredOn : String?, var paymentType : String?,
                             var isDefault : Boolean, var isPaused : Boolean, var isCancelled : Boolean,
                             var trialPlanDescription : String?, var isTrialPlan : Boolean,var orderState : String?,
                             var trailPlanPricing : String) : Parcelable {}

public data class GetReviewResponseModel(var data : ReviewMainModel?) : BaseResponseModel() {}


@Parcelize
public data class ReviewMainModel(var planTitle : String?, var description : String?, var featuredImage : String?,
                                  var reviews  : ArrayList<ReviewModel>?) : Parcelable{}

@Parcelize
public data class ReviewModel(var starRating : String?, var reviewText : String?, var meals : String? ,var noOfDays : String?,
                              var userName : String, var addedOn : String?) : Parcelable{}


public data class FavoriteResponseModel(var data : ArrayList<FavoriteModel>?) : BaseResponseModel(){}



@Parcelize
public data class FavoriteModel(var imagePath : String?, var companyName : String?, var businessId : String?) : Parcelable{}

public data class PlaceOrderResponseModel(var name : String?, var email : String?, var orderNo : String?, var orderId : String?, var number: String?) : BaseResponseModel(){}





