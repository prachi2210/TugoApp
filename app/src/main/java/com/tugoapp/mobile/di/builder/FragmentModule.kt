package com.tugoapp.mobile.di.builder

import com.tugoapp.mobile.ui.home.FragmentHome
import com.tugoapp.mobile.ui.login.FragmentLogin
import com.tugoapp.mobile.ui.login.FragmentSignUp
import com.tugoapp.mobile.ui.login.FragmentWelcome
import com.tugoapp.mobile.ui.orders.FragmentOrders
import com.tugoapp.mobile.ui.profile.FragmentProfile
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
}