package com.tugoapp.mobile.data.remote.model.response

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

public data class GetProvidersResponseModel(var data : ArrayList<ProviderModel>?) : BaseResponseModel() {}

public data class ProviderModel(var businessId : String?, var companyName : String?, var imagePath : String? ,var description : String?,
                                var startingFrom : String?, var numberOfPlans : String?, var offer : String?,
                                var isActive : Boolean?) {}

public data class GetProviderDetailsData(var businessId : String?, var companyName : String?, var imagePath : String? ,var icon : String?
                                                  ,var address : String?,var description : String?,var deliveryDays : String?,var planData : ArrayList<MealPlanModel>?) {}

public data class GetProviderDetailsResponseModel(var data : GetProviderDetailsData) : BaseResponseModel() {}



public data class MealPlanModel(var title : String?, var description : String?, var planId : String? ,var startingFrom : String?,
                                var review : String?, var locations : String?, var sampleMenu : ArrayList<SampleMenu>?) {}

@Parcelize
public data class SampleMenu(var planId : String?, var title : String?, var imagePath : String? ,var description : String?) : Parcelable{}