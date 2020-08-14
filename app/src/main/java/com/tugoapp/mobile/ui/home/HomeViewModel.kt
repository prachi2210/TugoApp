package com.tugoapp.mobile.ui.home

import android.app.Application
import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.tugoapp.mobile.data.remote.MerchantApiService
import com.tugoapp.mobile.data.remote.model.request.GetProviderDetailRequestModel
import com.tugoapp.mobile.data.remote.model.request.GetProvidersRequestModel
import com.tugoapp.mobile.data.remote.model.response.*
import com.tugoapp.mobile.ui.base.BaseViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel(application: Application?, private val mPpsApiService: MerchantApiService) : BaseViewModel(application) {

    var mCategoryData: MutableLiveData<ArrayList<CategoryDetailModel>> = MutableLiveData()
    var mProvidersData: MutableLiveData<ArrayList<ProviderModel>> = MutableLiveData()
    var mProvidersDetailData: MutableLiveData<GetProviderDetailsData> = MutableLiveData()

    var mToastMessage: MutableLiveData<String> = MutableLiveData()

    var mShowProgress: MutableLiveData<Boolean> = MutableLiveData()

    fun doLoadCategory() {
        FirebaseAuth.getInstance().currentUser?.getIdToken(false)?.addOnCompleteListener(OnCompleteListener { task ->
            if (task.isSuccessful) {
                mPpsApiService.doGetCategories(task.result?.token).enqueue(object : Callback<GetCategoryResponseModel> {
                    override fun onFailure(call: Call<GetCategoryResponseModel>, t: Throwable) {
                        mToastMessage.postValue(t.localizedMessage)
                    }

                    override fun onResponse(call: Call<GetCategoryResponseModel>, response: Response<GetCategoryResponseModel>) {
                        mCategoryData.postValue(response.body()?.data)
                    }

                })
            } else {
                mToastMessage.postValue(task.exception?.localizedMessage)
            }

        })
    }

    fun doSearchTerm(model : GetProvidersRequestModel) {
        FirebaseAuth.getInstance().currentUser?.getIdToken(false)?.addOnCompleteListener(OnCompleteListener { task ->
            if (task.isSuccessful) {
                mPpsApiService.doSearchTermForProvider(task.result?.token, model).enqueue(object : Callback<GetProvidersResponseModel> {
                    override fun onFailure(call: Call<GetProvidersResponseModel>, t: Throwable) {
                        mToastMessage.postValue(t.localizedMessage)
                    }

                    override fun onResponse(call: Call<GetProvidersResponseModel>, response: Response<GetProvidersResponseModel>) {
                        mProvidersData.postValue(response.body()?.data)
                    }

                })
            } else {
                mToastMessage.postValue(task.exception?.localizedMessage)
            }

        })
    }

    fun doLoadProviders(model : GetProvidersRequestModel) {
        FirebaseAuth.getInstance().currentUser?.getIdToken(false)?.addOnCompleteListener(OnCompleteListener { task ->
            if (task.isSuccessful) {
                mPpsApiService.doGetProviders(task.result?.token, model).enqueue(object : Callback<GetProvidersResponseModel> {
                    override fun onFailure(call: Call<GetProvidersResponseModel>, t: Throwable) {
                        mToastMessage.postValue(t.localizedMessage)
                    }

                    override fun onResponse(call: Call<GetProvidersResponseModel>, response: Response<GetProvidersResponseModel>) {
                        mProvidersData.postValue(response.body()?.data)
                    }

                })
            } else {
                mToastMessage.postValue(task.exception?.localizedMessage)
            }

        })
    }

    fun doGetProviderDetails(mBusinessId: String) {
        FirebaseAuth.getInstance().currentUser?.getIdToken(false)?.addOnCompleteListener(OnCompleteListener { task ->
            if (task.isSuccessful) {
                mPpsApiService.doGetProviderDetails(task.result?.token,  GetProviderDetailRequestModel(mBusinessId)).enqueue(object : Callback<GetProviderDetailsResponseModel> {
                    override fun onFailure(call: Call<GetProviderDetailsResponseModel>, t: Throwable) {
                        mToastMessage.postValue(t.localizedMessage)
                    }

                    override fun onResponse(call: Call<GetProviderDetailsResponseModel>, response: Response<GetProviderDetailsResponseModel>) {
                        mProvidersDetailData.postValue(response.body()?.data)
                    }

                })
            } else {
                mToastMessage.postValue(task.exception?.localizedMessage)
            }

        })
    }

    private val mApplicationContext: Context = getApplication<Application>().applicationContext
}