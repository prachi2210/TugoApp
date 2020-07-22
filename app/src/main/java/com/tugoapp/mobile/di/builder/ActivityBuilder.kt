package com.tugoapp.mobile.di.builder

import com.tugoapp.mobile.ui.RootActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBuilder {
    @ContributesAndroidInjector(modules = [FragmentModule::class])
    abstract fun bindMainActivity(): RootActivity?
}