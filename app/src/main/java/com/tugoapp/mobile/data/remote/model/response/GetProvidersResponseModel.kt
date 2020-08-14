package com.tugoapp.mobile.data.remote.model.response

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

public data class GetProvidersResponseModel(var data : ArrayList<ProviderModel>?) : BaseResponseModel() {}

public data class ProviderModel(var businessId : String?, var companyName : String?, var imagePath : String? ,var description : String?,
                                var startingFrom : String?, var numberOfPlans : String?, var offer : String?,
                                var isActive : Boolean?) {}

public data class GetProviderDetailsData(var businessId : String?, var companyName : String?, var imagePath : String? ,var icon : String?,
                                         var address : String?,
                                         var companyLogo : String?,
                                         var description : String?,var deliveryDays : String?,var planData : ArrayList<MealPlanModel>?) {}

public data class GetProviderDetailsResponseModel(var data : GetProviderDetailsData) : BaseResponseModel() {}


@Parcelize
public data class MealPlanModel(var title : String?, var description : String?, var planId : Int? ,var startingFrom : String?,
                                var featuredImage : String?,var trailPlanMainDescription : String?,var deliveryDays : String?,
                                var trialPlanDescription : String?, var isTrialPlanAvailable : Boolean,var trailPlanPricing : String,var trailPlanDays : String,
                                var review : String?, var locations : String?, var noOfReviews : String?, var sampleMenu : ArrayList<SampleMenu>?,
                                var priceForOneMeal : String?, var priceForTwoMeals : String? , var priceForThreeMeals : String?,  var startTime : String?,var endTime : String?,
                                var weekOptions : ArrayList<String>?,  var mealOptions : ArrayList<String>?,
                                var numbeOfDeliveryDays : Int?,

                                var isTrialMeal : Boolean,var noOfMeals : String?,var noOfWeeks : String?,var instructions : String?,
                                var address : String?,var startFrom : String?,var endOn : String?, var price : String?): Parcelable {}

@Parcelize
public data class SampleMenu(var planId : String?, var title : String?, var imagePath : String? ,var description : String?) : Parcelable{}

