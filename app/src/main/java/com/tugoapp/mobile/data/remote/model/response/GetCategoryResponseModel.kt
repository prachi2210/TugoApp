package com.tugoapp.mobile.data.remote.model.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

public data class GetCategoryResponseModel(var data : ArrayList<CategoryDetailModel>?) : BaseResponseModel() {}

public data class CategoryDetailModel(var categoryId : String?, var name : String? ,var imagePath : String?,var isActive : Boolean?) {}