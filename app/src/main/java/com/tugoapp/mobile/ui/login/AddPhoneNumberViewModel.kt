package com.tugoapp.mobile.ui.login

import android.app.Application
import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.tugoapp.mobile.R
import com.tugoapp.mobile.data.remote.MerchantApiService
import com.tugoapp.mobile.data.remote.model.request.SaveUserDetailRequestModel
import com.tugoapp.mobile.data.remote.model.response.*
import com.tugoapp.mobile.ui.base.BaseViewModel
import com.tugoapp.mobile.ui.base.SingleLiveEvent
import com.tugoapp.mobile.utils.CommonUtils
import com.tugoapp.mobile.utils.NetworkUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddPhoneNumberViewModel(application: Application?, private val mPpsApiService: MerchantApiService) : BaseViewModel(application) {
    private val mApplicationContext: Context = getApplication<Application>().applicationContext

    var mIsUserDetailSubmitted : SingleLiveEvent<Int> = SingleLiveEvent()
    var mIsUserDetailExistOnServer : SingleLiveEvent<UserDetailModel> = SingleLiveEvent()
    var mToastMessage: SingleLiveEvent<String> = SingleLiveEvent()

    var mShowProgress: SingleLiveEvent<Pair<Boolean,String>> = SingleLiveEvent()
    var mCountryData: SingleLiveEvent<ArrayList<CountryCodeModel>> = SingleLiveEvent()

    fun doSaveUserDetailOnServer(token: String?, saveUserDetailRequestModel: SaveUserDetailRequestModel) {
        if(NetworkUtils.isNetworkConnected(mApplicationContext)) {
            mPpsApiService.doSaveUserDetail(token, saveUserDetailRequestModel).enqueue(object : Callback<BaseResponseModel> {
                override fun onFailure(call: Call<BaseResponseModel>?, t: Throwable?) {
                    mToastMessage.postValue(t?.localizedMessage)
                }

                override fun onResponse(call: Call<BaseResponseModel>?, response: Response<BaseResponseModel>?) {
                    mIsUserDetailSubmitted.postValue(response?.body()?.isSuccess)
                }

            })
        } else {
            mToastMessage.postValue(mApplicationContext.getString(R.string.txt_no_internet))
        }
    }


    fun checkIfUserExist(token: String?) {
        if(NetworkUtils.isNetworkConnected(mApplicationContext)) {
            mPpsApiService.isUserExist(token).enqueue(object : Callback<GetUserDetailResponseModel> {
                override fun onFailure(call: Call<GetUserDetailResponseModel>?, t: Throwable?) {
                    mToastMessage.postValue(t?.localizedMessage)
                }

                override fun onResponse(call: Call<GetUserDetailResponseModel>?, response: Response<GetUserDetailResponseModel>?) {
                    mIsUserDetailExistOnServer.postValue(response?.body()?.data)
                }

            })
        } else {
            mToastMessage.postValue(mApplicationContext.getString(R.string.txt_no_internet))
        }
    }

    fun doLoadCountry() {
        if (NetworkUtils.isNetworkConnected(mApplicationContext)) {
            FirebaseAuth.getInstance().currentUser?.getIdToken(false)?.addOnCompleteListener(OnCompleteListener { task ->
                if (task.isSuccessful) {
                    mShowProgress.postValue(Pair(true, ""))
                    mPpsApiService.doGetCountryCodes().enqueue(object : Callback<GetCountryCodesResponseModel> {
                        override fun onFailure(call: Call<GetCountryCodesResponseModel>, t: Throwable) {
                            mShowProgress.postValue(Pair(false, ""))
                            mToastMessage.postValue(t.localizedMessage)
                        }

                        override fun onResponse(call: Call<GetCountryCodesResponseModel>, response: Response<GetCountryCodesResponseModel>) {
                            mCountryData.postValue(response.body()?.data)
                            mShowProgress.postValue(Pair(false, ""))
                        }

                    })
                } else {
                    mToastMessage.postValue(task.exception?.localizedMessage)
                }
            })
        } else {
            mToastMessage.postValue(mApplicationContext.getString(R.string.txt_no_internet))
        }
    }

}