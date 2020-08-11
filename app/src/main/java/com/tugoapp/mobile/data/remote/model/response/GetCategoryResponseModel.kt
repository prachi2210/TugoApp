package com.tugoapp.mobile.data.remote.model.response

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

public data class GetCategoryResponseModel(var data : ArrayList<CategoryDetailModel>?) : BaseResponseModel() {}

@Parcelize
public data class CategoryDetailModel(var categoryId : String?, var name : String? ,var imagePath : String?,var isActive : Boolean?) : Parcelable {}