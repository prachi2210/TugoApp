package com.tugoapp.mobile

import android.app.Activity
import android.app.Application
import android.content.Context
import com.facebook.FacebookSdk
import com.facebook.appevents.AppEventsLogger
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.tugoapp.mobile.di.component.DaggerAppComponent
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import javax.inject.Inject

class TugoApplication : Application(), HasActivityInjector {
    @JvmField
    @Inject
    var activityDispatchingAndroidInjector: DispatchingAndroidInjector<Activity>? = null
    override fun activityInjector(): DispatchingAndroidInjector<Activity> {
        return activityDispatchingAndroidInjector!!
    }

    override fun onCreate() {
        super.onCreate()
        DaggerAppComponent.builder()
                .application(this)
                .build()
                ?.inject(this)
        appContext = applicationContext
        FirebaseApp.initializeApp(applicationContext)
        FacebookSdk.sdkInitialize(this)
        AppEventsLogger.activateApp(this);
    }

    companion object {
        var appContext: Context? = null
            private set
    }
}