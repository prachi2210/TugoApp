package com.tugoapp.mobile.data.remote.model.request

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tugoapp.mobile.data.remote.model.response.MealPlanModel

public data class GetProvidersRequestModel(var limit : String?,var offset : String?,var categoryIds: String?, var searchTerm : String?) {}

public data class GetProviderDetailRequestModel(var businessId: String?) {}

public data class SaveUserDetailRequestModel(var email : String?, var phone : String?, var name: String?,var firebaseUserId: String?,
                                             var deviceId: String?,var pushToken: String?,var os: String?, var timezone: String?) {}

public data class AddAddressRequestModel( var address: String?,var setDefault: Boolean? ) {}

public data class UpdateAddressRequestModel(var addressId : String?, var address : String, var setDefault: Boolean? ) {}

public data class DeleteAddressRequestModel(var addressId : String?) {}

public data class ResumeOrderRequestModel(var orderId : String?,var startFrom : String?)

public data class PlaceOrderRequestModel(var isTrialPlan : Boolean?,var noOfMeals : String?,var noOfWeeks : String?,var instructions : String?,var planId : String? ,var mealId : String?,
                                        var deliveryTime : String? ,var deliveryLocation : String? , var address : String?,var startFrom : String?,var endOn : String?,
                                         var price : String?, var planObj : MealPlanModel?) {}


public object PlaceOrderObject {
    var isTrialPlan : Boolean = false
    var noOfMeals : String? = null
    var noOfWeeks : String? = null
    var instructions : String? = null
    var planId : String? = null
    var mealId : String? = null
    var deliveryTime : String? = null
    var deliveryLocation : String? = null
    var address : String? = null
    var startFrom : String? = null
    var endOn : String? = null
    var price : String? = null
    var planObj : MealPlanModel? = null
}

public data class GetFilterProviderRequestModel(var maximumPrice : String?,var minimumPrice : String?,var numOfMeals : String?,
                                                var allLocations : ArrayList<String>?, var trailMealsAvailable : Boolean)

public data class AddReviewRequestModel(var orderId : String?,var starRating : String?,var reviewText : String?)

public data class GetReviewRequestModel(var planId : String?)

public data class UpdateNotiRequestModel(var sendPushNotification : Boolean?,var sendSMSNotification : Boolean?,
                                         var sendPromotionalNotification : Boolean?)


public data class SaveDeviceTokenRequestModel(var deviceId : String?,var pushToken : String?,var os : String?, var timezone : String?)

public data class SetFavirouteProviderRequestModel(var businessId : String?)


public data class LogoutRequestModel(var deviceId : String?)

public data class SubmitQueryRequestModel(var queryDescription : String?,var businessId : String?)




