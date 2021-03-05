package com.tugoapp.mobile.data.remote.model.response

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

public data class GetProvidersResponseModel(var data : ArrayList<ProviderModel>?) : BaseResponseModel() {}

public data class ProviderModel(var businessId : String?, var companyName : String?, var imagePath : String?,var featuredImage : String?  ,var description : String?,
                                var startingFrom : String?, var numOfPlans : String?, var offer : String?,
                                var isActive : Boolean?) {}

public data class GetProviderDetailsData(var businessId : String?, var companyName : String?, var backgroundImage : String? ,var icon : String?,
                                         var address : String?, var isFavourite : Boolean?,var phoneNumber : String?,
                                         var companyLogo : String?, var defaultUserAddress : String?, var addressId : String?,var numbeOfDeliveryDays : Int?,
                                         var description : String?,var planData : ArrayList<MealPlanModel>?) {}

public data class GetProviderDetailsResponseModel(var data : GetProviderDetailsData) : BaseResponseModel() {}


@Parcelize
public data class MealPlanModel(var title : String?, var description : String?, var planId : String? ,var startingFrom : String?,
                                var featuredImage : String?,var trailPlanMainDescription : String?,var deliveryDays : String?,
                                var trialPlanDescription : String?, var isTrialPlanAvailable : Boolean,var trailPlanPricing : String,var trialPlanDays : String, var trialPlanAmount : String,
                                var trialPlanMeals : String,var trialPlanWeeks : String,var phoneNumber : String?,
                                var review : String?, var locations : String?, var noOfReviews : String?, var sampleMenu : ArrayList<SampleMenu>?, var deliveryTime: String?,
                                var mealOptions : HashMap<String,ArrayList<MealOptionsModel>>, var startTime : String?,var endTime : String?, var avgMeal : String?, var avgDay : String?,
                                var numbeOfDeliveryDays : Int?,var defaultUserAddress : String?, var addressId : String?): Parcelable {}

@Parcelize
public data class SampleMenu(var planId : String?, var title : String?, var imagePath : String? ,var description : String?) : Parcelable{}

@Parcelize
public data class MealOptionsModel(var planId : String?, var weeks : String?, var noOfDays : String? ,var noOfMeals : String?,var price : String?,var amount : String?,var priceWithSnack : String?,
                                   var mealId : String?,var maxSnack: String?,var pricePerSnack : String?,var originalPrice : String?,var sortOrder : String?,var discount : String?, var snackQty : String?) : Parcelable{}

