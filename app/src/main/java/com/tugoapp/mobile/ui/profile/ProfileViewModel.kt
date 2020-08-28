package com.tugoapp.mobile.ui.profile

import android.app.Application
import android.content.Context
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.tugoapp.mobile.R
import com.tugoapp.mobile.data.remote.MerchantApiService
import com.tugoapp.mobile.data.remote.model.request.LogoutRequestModel
import com.tugoapp.mobile.data.remote.model.request.UpdateNotiRequestModel
import com.tugoapp.mobile.data.remote.model.response.BaseResponseModel
import com.tugoapp.mobile.data.remote.model.response.GetReviewResponseModel
import com.tugoapp.mobile.data.remote.model.response.GetUserDetailResponseModel
import com.tugoapp.mobile.data.remote.model.response.UserDetailModel
import com.tugoapp.mobile.ui.base.BaseViewModel
import com.tugoapp.mobile.ui.base.SingleLiveEvent
import com.tugoapp.mobile.utils.CommonUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileViewModel(application: Application?, private val mPpsApiService: MerchantApiService) : BaseViewModel(application) {
    var mUserDetailModel: SingleLiveEvent<UserDetailModel> = SingleLiveEvent()
    var mToastMessage: SingleLiveEvent<String> = SingleLiveEvent()
    var mShowProgress: SingleLiveEvent<Pair<Boolean, String>> = SingleLiveEvent()
    var mIsUserLogout: SingleLiveEvent<Int> = SingleLiveEvent()


    fun doLodUserDetail() {
        FirebaseAuth.getInstance().currentUser?.getIdToken(false)?.addOnCompleteListener(OnCompleteListener { task ->
            if (task.isSuccessful) {
                mShowProgress.postValue(Pair(true,mApplicationContext.getString(R.string.txt_loading_profile)))
                mPpsApiService.doGetUserDetails(task.result?.token).enqueue(object : Callback<GetUserDetailResponseModel> {
                    override fun onFailure(call: Call<GetUserDetailResponseModel>, t: Throwable) {
                        mShowProgress.postValue(Pair(false,""))
                        mToastMessage.postValue(t.localizedMessage)
                    }

                    override fun onResponse(call: Call<GetUserDetailResponseModel>, response: Response<GetUserDetailResponseModel>) {
                        mUserDetailModel.postValue(response.body()?.data)
                        mShowProgress.postValue(Pair(false,""))
                    }

                })
            } else {
                mToastMessage.postValue(task.exception?.localizedMessage)
            }

        })
    }

    fun doSetUserProfile(data: UserDetailModel?) {
        FirebaseAuth.getInstance().currentUser?.getIdToken(false)?.addOnCompleteListener(OnCompleteListener { task ->
            if (task.isSuccessful) {
                //mShowProgress.postValue(Pair(true,""))
                mPpsApiService.doUpdateNotificationSetting(task.result?.token, UpdateNotiRequestModel(data?.sendPushNotification,data?.sendSMSNotification,
                        data?.sendPromotionalNotification)).enqueue(object : Callback<BaseResponseModel> {
                    override fun onFailure(call: Call<BaseResponseModel>, t: Throwable) {
                      //  mShowProgress.postValue(Pair(false,""))
                      //  mToastMessage.postValue(t.localizedMessage)
                    }

                    override fun onResponse(call: Call<BaseResponseModel>, response: Response<BaseResponseModel>) {
                      //  mUserDetailModel.postValue(response.body()?.data)
                      //  mShowProgress.postValue(Pair(false,""))
                    }

                })
            } else {
              //  mToastMessage.postValue(task.exception?.localizedMessage)
            }

        })
    }

    fun doUserLogout() {
        FirebaseAuth.getInstance().currentUser?.getIdToken(false)?.addOnCompleteListener(OnCompleteListener { task ->
            if (task.isSuccessful) {
                mShowProgress.postValue(Pair(true,mApplicationContext.getString(R.string.txt_loading_logout)))
                mPpsApiService.doServerLogout(task.result?.token, LogoutRequestModel(CommonUtils.getDeviceId(mApplicationContext))).enqueue(object : Callback<BaseResponseModel> {
                    override fun onFailure(call: Call<BaseResponseModel>, t: Throwable) {
                        mShowProgress.postValue(Pair(false,""))
                        mToastMessage.postValue(t.localizedMessage)
                    }

                    override fun onResponse(call: Call<BaseResponseModel>, response: Response<BaseResponseModel>) {
                        mIsUserLogout.postValue(response.body()?.isSuccess)
                        mShowProgress.postValue(Pair(false,""))
                    }

                })
            } else {
                mToastMessage.postValue(task.exception?.localizedMessage)
            }

        })
    }

    private val mApplicationContext: Context = getApplication<Application>().applicationContext
}