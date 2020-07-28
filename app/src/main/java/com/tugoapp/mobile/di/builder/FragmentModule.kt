package com.tugoapp.mobile.di.builder

import com.tugoapp.mobile.ui.home.FragmentHome
import com.tugoapp.mobile.ui.login.*
import com.tugoapp.mobile.ui.orderdetail.FragmentOrderDetails
import com.tugoapp.mobile.ui.orders.FragmentHistoryOrders
import com.tugoapp.mobile.ui.orders.FragmentOnGoingOrders
import com.tugoapp.mobile.ui.orders.FragmentOrders
import com.tugoapp.mobile.ui.profile.*
import com.tugoapp.mobile.ui.splash.FragmentSplash
import com.tugoapp.mobile.ui.walkthrough.FragmentWalkthrough
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentModule {
    @ContributesAndroidInjector
    abstract fun bindEntryFragment(): FragmentSplash?

    @ContributesAndroidInjector
    abstract fun bindHome(): FragmentHome?

    @ContributesAndroidInjector
    abstract fun bindOrders(): FragmentOrders?

    @ContributesAndroidInjector
    abstract fun bindProfile(): FragmentProfile?

    @ContributesAndroidInjector
    abstract fun bindLogin(): FragmentLogin?

    @ContributesAndroidInjector
    abstract fun bindSignup(): FragmentSignUp?

    @ContributesAndroidInjector
    abstract fun bindWelcome(): FragmentWelcome?

    @ContributesAndroidInjector
    abstract fun bindWalkthrough(): FragmentWalkthrough?

    @ContributesAndroidInjector
    abstract fun bindAddPhoneNumber(): FragmentAddPhoneNumber?

    @ContributesAndroidInjector
    abstract fun bindVerifyOtp(): FragmentVerifyOTP?

    @ContributesAndroidInjector
    abstract fun bindHistory(): FragmentHistoryOrders?

    @ContributesAndroidInjector
    abstract fun bindOngoingOrder() : FragmentOnGoingOrders?

    @ContributesAndroidInjector
    abstract fun bindOrderDetail() : FragmentOrderDetails?

    @ContributesAndroidInjector
    abstract fun bindChangePswd() : FragmentChangePswd?

    @ContributesAndroidInjector
    abstract fun bindPersonalInfo() : FragmentPersonalInformation?

    @ContributesAndroidInjector
    abstract fun bindPaymentMethods() : FragmentPaymentMethods?

    @ContributesAndroidInjector
    abstract fun bindManageAddress() : FragmentManageAddress?

    @ContributesAndroidInjector
    abstract fun bindAddAddress() : FragmentAddAddress?

    @ContributesAndroidInjector
    abstract fun bindAddPaymentMethod() : FragmentAddPaymentMethod?

}