package com.tugoapp.mobile.ui.login

import android.app.Application
import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.tugoapp.mobile.data.remote.MerchantApiService
import com.tugoapp.mobile.data.remote.model.request.SaveUserDetailRequestModel
import com.tugoapp.mobile.data.remote.model.response.BaseResponseModel
import com.tugoapp.mobile.ui.base.BaseViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddPhoneNumberViewModel(application: Application?, private val mPpsApiService: MerchantApiService) : BaseViewModel(application) {
    private val mApplicationContext: Context = getApplication<Application>().applicationContext

    var mIsUserDetailSubmitted : MutableLiveData<Int> = MutableLiveData()
    var mToastMessage: MutableLiveData<String> = MutableLiveData()


    fun doSaveUserDetailOnServer(token: String?, saveUserDetailRequestModel: SaveUserDetailRequestModel) {
        mPpsApiService.doSaveUserDetail(token,saveUserDetailRequestModel).enqueue(object : Callback<BaseResponseModel> {
            override fun onFailure(call: Call<BaseResponseModel>?, t: Throwable?) {
                mToastMessage.postValue(t?.localizedMessage)
            }

            override fun onResponse(call: Call<BaseResponseModel>?, response: Response<BaseResponseModel>?) {
                mIsUserDetailSubmitted.postValue(response?.body()?.isSuccess)
            }

        })
    }

}