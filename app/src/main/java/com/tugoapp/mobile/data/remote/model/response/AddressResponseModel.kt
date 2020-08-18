package com.tugoapp.mobile.data.remote.model.response

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

public data class GetAddressResponseModel(var data : ArrayList<AddressModel>?) : BaseResponseModel() {}

public data class AddressModel(var userId : String?,var address : String?,var addressId : String?,var isDefault : String?)  {}
