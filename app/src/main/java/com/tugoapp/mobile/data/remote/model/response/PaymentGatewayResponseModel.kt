package com.tugoapp.mobile.data.remote.model.response

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


public data class PaymentGatewayResponseModel(var success : Boolean, var status_message : String?, var order_id : String?){}

