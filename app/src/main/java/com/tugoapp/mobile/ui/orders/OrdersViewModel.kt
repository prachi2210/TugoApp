package com.tugoapp.mobile.ui.orders

import android.app.Application
import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.tugoapp.mobile.data.remote.MerchantApiService
import com.tugoapp.mobile.ui.base.BaseViewModel

class OrdersViewModel(application: Application?, private val mPpsApiService: MerchantApiService) : BaseViewModel(application) {
    fun setSelectedHistoryOrder(position: Int) {
        mSelectedHistoryOrder.postValue(position)
    }

    private val mApplicationContext: Context = getApplication<Application>().applicationContext

    val mSelectedHistoryOrder = MutableLiveData<Int>()
}