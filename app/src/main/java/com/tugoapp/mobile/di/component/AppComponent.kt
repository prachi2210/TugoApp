package com.tugoapp.mobile.di.component

import android.app.Application
import com.tugoapp.mobile.TugoApplication
import com.tugoapp.mobile.di.builder.ActivityBuilder
import com.tugoapp.mobile.di.module.AppModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, AndroidInjectionModule::class, ActivityBuilder::class])
interface AppComponent : AndroidInjector<TugoApplication> {
    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder
        fun build(): AppComponent?
    }

    override fun inject(myApp: TugoApplication)
}