package com.tugoapp.mobile.data.remote.model.response

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

public data class GetAddressResponseModel(var data : ArrayList<AddressModel>?) : BaseResponseModel() {}

@Parcelize
public data class AddressModel(var userId : String?,var address : String?,var addressId : String?,var isDefault : Boolean) : Parcelable {}


public data class CustomizeListModel(var value : String?, var isSelected : Boolean) {}

public data class GetCountryCodesResponseModel(var data : ArrayList<CountryCodeModel>?) : BaseResponseModel() {}

public data class CountryCodeModel(var name : String?, var flag : String?,var code : String?,var dial_code : String?) {}

public data class GetUserDetailResponseModel(var data : UserDetailModel?) : BaseResponseModel() {}

@Parcelize
public data class UserDetailModel(var userEmail : String?, var userName : String?,var userPhone : String?,
                                  var sendPushNotification : Boolean?,var sendSMSNotification : Boolean?,
                                  var sendPromotionalNotification : Boolean?) :Parcelable {}


