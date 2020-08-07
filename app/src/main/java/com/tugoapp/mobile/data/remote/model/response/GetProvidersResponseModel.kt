package com.tugoapp.mobile.data.remote.model.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

public data class GetProvidersResponseModel(var data : ArrayList<ProviderModel>?) : BaseResponseModel() {}

public data class ProviderModel(var companyName : String?, var imagePath : String? ,var description : String?,
                                var startingFrom : String?, var numberOfPlans : String?, var offer : String?,
                                var isActive : Boolean?) {}