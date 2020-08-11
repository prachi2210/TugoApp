package com.tugoapp.mobile.data.remote.model.request

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

public data class GetProvidersRequestModel(var limit : String?,var offset : String?,var categoryIds: String?) {}

public data class GetProviderDetailRequestModel(var businessId: String?) {}

public data class SaveUserDetailRequestModel(var email : String?, var phone : String, var name: String?,var firebaseUserId: String? ) {}