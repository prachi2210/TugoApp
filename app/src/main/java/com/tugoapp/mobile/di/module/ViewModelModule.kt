package com.tugoapp.mobile.di.module

import com.tugoapp.mobile.TugoApplication
import com.tugoapp.mobile.data.remote.MerchantApiService
import com.tugoapp.mobile.ui.RootViewModel
import com.tugoapp.mobile.ui.home.HomeViewModel
import com.tugoapp.mobile.ui.login.AddPhoneNumberViewModel
import com.tugoapp.mobile.ui.login.VerifyOTPViewModel
import com.tugoapp.mobile.ui.orderdetail.OrderDetailsViewModel
import com.tugoapp.mobile.ui.orders.OrdersViewModel
import com.tugoapp.mobile.ui.profile.*
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

    @Provides
    @IntoMap
    @ViewModelKey(ChangePswdViewModel::class)
    fun changePswdViewModel(application: TugoApplication?, service: MerchantApiService?): ChangePswdViewModel {
        return ChangePswdViewModel(application,service!!)
    }

    @Provides
    @IntoMap
    @ViewModelKey(PersonalInformationViewModel::class)
    fun personalInformationViewModel(application: TugoApplication?, service: MerchantApiService?): PersonalInformationViewModel {
        return PersonalInformationViewModel(application,service!!)
    }

    @Provides
    @IntoMap
    @ViewModelKey(PaymentMethodsViewModel::class)
    fun paymentMethodsViewModel(application: TugoApplication?, service: MerchantApiService?): PaymentMethodsViewModel {
        return com.tugoapp.mobile.ui.profile.PaymentMethodsViewModel(application, service!!)
    }

    @Provides
    @IntoMap
    @ViewModelKey(ManageAddressViewModel::class)
    fun manageAddressViewModel(application: TugoApplication?, service: MerchantApiService?): ManageAddressViewModel {
        return ManageAddressViewModel(application,service!!)
    }

    @Provides
    @IntoMap
    @ViewModelKey(AddPaymentMethodViewModel::class)
    fun addPaymentMethodViewModel(application: TugoApplication?, service: MerchantApiService?): AddPaymentMethodViewModel {
        return AddPaymentMethodViewModel(application,service!!)
    }


    @Provides
    @IntoMap
    @ViewModelKey(AddAddressViewModel::class)
    fun addAddressViewModel(application: TugoApplication?, service: MerchantApiService?): AddAddressViewModel {
        return AddAddressViewModel(application,service!!)
    }

    @Provides
    @IntoMap
    @ViewModelKey(OrderDetailsViewModel::class)
    fun orderDetailsViewModel(application: TugoApplication?, service: MerchantApiService?): OrderDetailsViewModel {
        return OrderDetailsViewModel(application,service!!)
    }



}