package com.tugoapp.mobile.ui.base

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider.NewInstanceFactory
import com.tugoapp.mobile.data.remote.MerchantApiService
import com.tugoapp.mobile.ui.RootViewModel
import com.tugoapp.mobile.ui.home.HomeViewModel
import com.tugoapp.mobile.ui.login.*
import com.tugoapp.mobile.ui.orders.HistoryOrdersViewModel
import com.tugoapp.mobile.ui.orders.OngoingOrdersViewModel
import com.tugoapp.mobile.ui.orders.OrderDetailViewModel
import com.tugoapp.mobile.ui.orders.OrdersViewModel
import com.tugoapp.mobile.ui.profile.*
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

            modelClass.isAssignableFrom(AddPhoneNumberViewModel::class.java) -> {
                AddPhoneNumberViewModel(application, apiservice) as T
            }

            modelClass.isAssignableFrom(VerifyOTPViewModel::class.java) -> {
                VerifyOTPViewModel(application, apiservice) as T
            }

            modelClass.isAssignableFrom(OngoingOrdersViewModel::class.java) -> {
                OngoingOrdersViewModel(application, apiservice) as T
            }

            modelClass.isAssignableFrom(HistoryOrdersViewModel::class.java) -> {
                HistoryOrdersViewModel(application, apiservice) as T
            }

            modelClass.isAssignableFrom(OrderDetailViewModel::class.java) -> {
                OrderDetailViewModel(application, apiservice) as T
            }

            modelClass.isAssignableFrom(PersonalInformationViewModel::class.java) -> {
                PersonalInformationViewModel(application, apiservice) as T
            }

            modelClass.isAssignableFrom(ChangePswdViewModel::class.java) -> {
                ChangePswdViewModel(application, apiservice) as T
            }

            modelClass.isAssignableFrom(PaymentMethodsViewModel::class.java) -> {
                PaymentMethodsViewModel(application, apiservice) as T
            }

            modelClass.isAssignableFrom(ManageAddressViewModel::class.java) -> {
                ManageAddressViewModel(application, apiservice) as T
            }

            modelClass.isAssignableFrom(AddAddressViewModel::class.java) -> {
                AddAddressViewModel(application, apiservice) as T
            }

            modelClass.isAssignableFrom(AddPaymentMethodViewModel::class.java) -> {
                AddPaymentMethodViewModel(application, apiservice) as T
            }


            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }

}