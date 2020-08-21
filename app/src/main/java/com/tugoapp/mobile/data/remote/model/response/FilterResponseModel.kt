package com.tugoapp.mobile.data.remote.model.response

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


public data class GetFilterDataResponseModel(var data : FilterModel) : BaseResponseModel() {}


@Parcelize
public data class FilterModel(var maximumPrice : String?,var minimumPrice : String?,var numOfMeals : ArrayList<String>?,
                              var allLocations : ArrayList<String>?) : Parcelable {}
