package com.tugoapp.mobile.ui.splash

import android.app.Application
import android.content.Context
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.tugoapp.mobile.data.remote.MerchantApiService
import com.tugoapp.mobile.data.remote.model.request.SaveDeviceTokenRequestModel
import com.tugoapp.mobile.data.remote.model.request.SetFavirouteProviderRequestModel
import com.tugoapp.mobile.data.remote.model.response.BaseResponseModel
import com.tugoapp.mobile.ui.base.BaseViewModel
import com.tugoapp.mobile.ui.base.SingleLiveEvent
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SplashViewModel(application: Application?, private val mPpsApiService: MerchantApiService) : BaseViewModel(application) {

    var mIsTokenUpdate: SingleLiveEvent<Boolean> = SingleLiveEvent()

    fun doUpdateToken(model: SaveDeviceTokenRequestModel) {
        FirebaseAuth.getInstance().currentUser?.getIdToken(false)?.addOnCompleteListener(OnCompleteListener { task ->
            if (task.isSuccessful) {
                //  mShowProgress.postValue(Pair(true,""))
                mPpsApiService.doSaveDeviceToken(task.result?.token, model).enqueue(object : Callback<BaseResponseModel>  {
                    override fun onFailure(call: Call<BaseResponseModel>, t: Throwable) {
                        mIsTokenUpdate.postValue(false)
                        //    mShowProgress.postValue(Pair(false,""))
                       // mToastMessage.postValue("Network Issue: Failed to add this provider to favorite")
                    }

                    override fun onResponse(call: Call<BaseResponseModel>, response: Response<BaseResponseModel>) {
                        mIsTokenUpdate.postValue(true)
                        //   mReviewModel.postValue(response.body()?.data)
                        //    mShowProgress.postValue(Pair(false,""))
                    }

                })
            } else {
                mIsTokenUpdate.postValue(false)
                // mToastMessage.postValue(task.exception?.localizedMessage)
            }

        })
    }

    private val mApplicationContext: Context = getApplication<Application>().applicationContext
}