package com.tugoapp.mobile.di.module

import com.tugoapp.mobile.TugoApplication
import com.tugoapp.mobile.data.remote.MerchantApiService
import com.tugoapp.mobile.ui.RootViewModel
import com.tugoapp.mobile.ui.home.HomeViewModel
import com.tugoapp.mobile.ui.login.AddPhoneNumberViewModel
import com.tugoapp.mobile.ui.login.FragmentVerifyOTP
import com.tugoapp.mobile.ui.login.VerifyOTPViewModel
import com.tugoapp.mobile.ui.orders.OrdersViewModel
import com.tugoapp.mobile.ui.profile.ProfileViewModel
import com.tugoapp.mobile.ui.splash.SplashViewModel
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap

@Module
class ViewModelModule {
    @Provides
    @IntoMap
    @ViewModelKey(RootViewModel::class)
    fun rootViewModel(application: TugoApplication?, service: MerchantApiService?): RootViewModel {
        return rootViewModel(application, service!!)
    }

    @Provides
    @IntoMap
    @ViewModelKey(SplashViewModel::class)
    fun splashViewModel(application: TugoApplication?, service: MerchantApiService?): SplashViewModel {
        return SplashViewModel(application,service!!)
    }

    @Provides
    @IntoMap
    @ViewModelKey(HomeViewModel::class)
    fun homeViewModel(application: TugoApplication?, service: MerchantApiService?): HomeViewModel {
        return HomeViewModel(application,service!!)
    }

    @Provides
    @IntoMap
    @ViewModelKey(OrdersViewModel::class)
    fun ordersViewModel(application: TugoApplication?, service: MerchantApiService?): OrdersViewModel {
        return OrdersViewModel(application,service!!)
    }

    @Provides
    @IntoMap
    @ViewModelKey(ProfileViewModel::class)
    fun profileViewModel(application: TugoApplication?, service: MerchantApiService?): ProfileViewModel {
        return ProfileViewModel(application,service!!)
    }

    @Provides
    @IntoMap
    @ViewModelKey(AddPhoneNumberViewModel::class)
    fun addPhoneNumberViewModel(application: TugoApplication?, service: MerchantApiService?): AddPhoneNumberViewModel {
        return AddPhoneNumberViewModel(application,service!!)
    }

    @Provides
    @IntoMap
    @ViewModelKey(VerifyOTPViewModel::class)
    fun verifyOtpViewModel(application: TugoApplication?, service: MerchantApiService?): VerifyOTPViewModel {
        return VerifyOTPViewModel(application,service!!)
    }


}