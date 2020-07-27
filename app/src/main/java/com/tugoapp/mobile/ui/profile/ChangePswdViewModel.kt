package com.tugoapp.mobile.ui.profile

import android.app.Application
import android.content.Context
import com.tugoapp.mobile.data.remote.MerchantApiService
import com.tugoapp.mobile.ui.base.BaseViewModel

class ChangePswdViewModel(application: Application?, private val mPpsApiService: MerchantApiService) : BaseViewModel(application) {
    private val mApplicationContext: Context = getApplication<Application>().applicationContext
}