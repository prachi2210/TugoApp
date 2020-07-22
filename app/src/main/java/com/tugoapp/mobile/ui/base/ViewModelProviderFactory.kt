package com.tugoapp.mobile.ui.base

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider.NewInstanceFactory
import com.tugoapp.mobile.data.remote.MerchantApiService
import com.tugoapp.mobile.ui.RootViewModel
import com.tugoapp.mobile.ui.home.HomeViewModel
import com.tugoapp.mobile.ui.login.LoginViewModel
import com.tugoapp.mobile.ui.login.SignUpViewModel
import com.tugoapp.mobile.ui.login.WelcomeViewModel
import com.tugoapp.mobile.ui.orders.OrdersViewModel
import com.tugoapp.mobile.ui.profile.ProfileViewModel
import com.tugoapp.mobile.ui.splash.SplashViewModel
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ViewModelProviderFactory @Inject constructor(private val application: Application, private val apiservice: MerchantApiService) : NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(SplashViewModel::class.java) -> {
                SplashViewModel(application, apiservice) as T
            }
            modelClass.isAssignableFrom(RootViewModel::class.java) -> {
                RootViewModel(application, apiservice) as T
            }
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> {
                HomeViewModel(application, apiservice) as T
            }

            modelClass.isAssignableFrom(OrdersViewModel::class.java) -> {
                OrdersViewModel(application, apiservice) as T
            }

            modelClass.isAssignableFrom(ProfileViewModel::class.java) -> {
                ProfileViewModel(application, apiservice) as T
            }

            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(application, apiservice) as T
            }

            modelClass.isAssignableFrom(WelcomeViewModel::class.java) -> {
                WelcomeViewModel(application, apiservice) as T
            }

            modelClass.isAssignableFrom(SignUpViewModel::class.java) -> {
                SignUpViewModel(application, apiservice) as T
            }

            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }

}