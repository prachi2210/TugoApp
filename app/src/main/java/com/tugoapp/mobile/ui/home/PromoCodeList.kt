package com.tugoapp.mobile.ui.home

data class PromoCodeList(
    val `data`: ArrayList<Data>,
    val message: String,
    val success: Int
)

data class Data(
    val addedBy: String,
    val created_at: String,
    val discount: String,
    val discription: String,
    val forBusiness: String,
    val id: String,
    val is_active: String,
    val mealId: String,
    val promoCode: String,
    val updated_at: String
)