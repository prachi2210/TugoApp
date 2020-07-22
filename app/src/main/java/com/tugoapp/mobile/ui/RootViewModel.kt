package com.tugoapp.mobile.ui

import android.app.Application
import com.tugoapp.mobile.data.remote.MerchantApiService
import com.tugoapp.mobile.ui.base.BaseViewModel

class RootViewModel(application: Application?, var mPpsApiService: MerchantApiService) : BaseViewModel(application)