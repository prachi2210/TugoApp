package com.tugoapp.mobile.ui.profile

import android.app.Application
import android.content.Context
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.tugoapp.mobile.R
import com.tugoapp.mobile.data.remote.MerchantApiService
import com.tugoapp.mobile.data.remote.model.response.AddressModel
import com.tugoapp.mobile.data.remote.model.response.CategoryDetailModel
import com.tugoapp.mobile.data.remote.model.response.GetAddressResponseModel
import com.tugoapp.mobile.data.remote.model.response.GetCategoryResponseModel
import com.tugoapp.mobile.ui.base.BaseViewModel
import com.tugoapp.mobile.ui.base.SingleLiveEvent
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ManageAddressViewModel(application: Application?, private val mPpsApiService: MerchantApiService) : BaseViewModel(application) {
    var mAddressListData: SingleLiveEvent<ArrayList<AddressModel>> = SingleLiveEvent()

    var mToastMessage: SingleLiveEvent<String> = SingleLiveEvent()

    var mShowProgress: SingleLiveEvent<Pair<Boolean, String>> = SingleLiveEvent()

    fun doLoadAllAddress() {
        FirebaseAuth.getInstance().currentUser?.getIdToken(false)?.addOnCompleteListener(OnCompleteListener { task ->
            if (task.isSuccessful) {
                mShowProgress.postValue(Pair(true,mApplicationContext.getString(R.string.txt_loading_address)))
                mPpsApiService.doGetAddressList(task.result?.token).enqueue(object : Callback<GetAddressResponseModel> {
                    override fun onFailure(call: Call<GetAddressResponseModel>, t: Throwable) {
                        mShowProgress.postValue(Pair(false,""))
                        mToastMessage.postValue(t.localizedMessage)
                    }

                    override fun onResponse(call: Call<GetAddressResponseModel>, response: Response<GetAddressResponseModel>) {
                        mAddressListData.postValue(response.body()?.data)
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