package com.tugoapp.mobile.ui.profile

import android.app.Application
import android.content.Context
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.tugoapp.mobile.R
import com.tugoapp.mobile.data.remote.MerchantApiService
import com.tugoapp.mobile.data.remote.model.request.AddAddressRequestModel
import com.tugoapp.mobile.data.remote.model.response.AddressModel
import com.tugoapp.mobile.data.remote.model.response.BaseResponseModel
import com.tugoapp.mobile.data.remote.model.response.GetAddressResponseModel
import com.tugoapp.mobile.ui.base.BaseViewModel
import com.tugoapp.mobile.ui.base.SingleLiveEvent
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddAddressViewModel(application: Application?, private val mPpsApiService: MerchantApiService) : BaseViewModel(application) {
    var mAddAddressData: SingleLiveEvent<Pair<Int?,String?>> = SingleLiveEvent()

    var mToastMessage: SingleLiveEvent<String> = SingleLiveEvent()

    var mShowProgress: SingleLiveEvent<Pair<Boolean, String>> = SingleLiveEvent()

    fun doAddAddressOnServer(addAddressRequestModel: AddAddressRequestModel) {
        FirebaseAuth.getInstance().currentUser?.getIdToken(false)?.addOnCompleteListener(OnCompleteListener { task ->
            if (task.isSuccessful) {
                mShowProgress.postValue(Pair(true,mApplicationContext.getString(R.string.txt_loading_adding_address)))
                mPpsApiService.doAddAddress(task.result?.token,addAddressRequestModel).enqueue(object : Callback<BaseResponseModel> {
                    override fun onFailure(call: Call<BaseResponseModel>, t: Throwable) {
                        mShowProgress.postValue(Pair(false,""))
                        mToastMessage.postValue(t.localizedMessage)
                    }

                    override fun onResponse(call: Call<BaseResponseModel>, response: Response<BaseResponseModel>) {
                        mAddAddressData.postValue(Pair(response?.body()?.isSuccess,response?.body()?.message))
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