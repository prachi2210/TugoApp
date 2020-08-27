package com.tugoapp.mobile.ui.home

import android.app.Application
import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.tugoapp.mobile.R
import com.tugoapp.mobile.data.remote.MerchantApiService
import com.tugoapp.mobile.data.remote.model.request.*
import com.tugoapp.mobile.data.remote.model.response.*
import com.tugoapp.mobile.ui.base.BaseViewModel
import com.tugoapp.mobile.ui.base.SingleLiveEvent
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel(application: Application?, private val mPpsApiService: MerchantApiService) : BaseViewModel(application) {

    var mCategoryData: SingleLiveEvent<ArrayList<CategoryDetailModel>> = SingleLiveEvent()
    var mProvidersData: SingleLiveEvent<ArrayList<ProviderModel>> = SingleLiveEvent()
    var mSearchedProvidersData: SingleLiveEvent<ArrayList<ProviderModel>> = SingleLiveEvent()
    var mProvidersDetailData: SingleLiveEvent<GetProviderDetailsData> = SingleLiveEvent()
    var mReviewModel: SingleLiveEvent<ReviewMainModel> = SingleLiveEvent()


    var mToastMessage: SingleLiveEvent<String> = SingleLiveEvent()
    var mShowProgress: SingleLiveEvent<Pair<Boolean,String>> = SingleLiveEvent()
    var mPlaceOrderResponse: SingleLiveEvent<Pair<Int?,String?>> = SingleLiveEvent()
    var mCustomFilterData: SingleLiveEvent<FilterModel> = SingleLiveEvent()


    fun doLoadCategory() {
        FirebaseAuth.getInstance().currentUser?.getIdToken(false)?.addOnCompleteListener(OnCompleteListener { task ->
            if (task.isSuccessful) {
                mShowProgress.postValue(Pair(true,""))
                mPpsApiService.doGetCategories(task.result?.token).enqueue(object : Callback<GetCategoryResponseModel> {
                    override fun onFailure(call: Call<GetCategoryResponseModel>, t: Throwable) {
                        mShowProgress.postValue(Pair(false,""))
                        mToastMessage.postValue(t.localizedMessage)
                    }

                    override fun onResponse(call: Call<GetCategoryResponseModel>, response: Response<GetCategoryResponseModel>) {
                        mCategoryData.postValue(response.body()?.data)
                        mShowProgress.postValue(Pair(false,""))
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
                //mShowProgress.postValue(Pair(true,mApplicationContext.getString(R.string.txt_loading_searching_provider)))
                mPpsApiService.doSearchTermForProvider(task.result?.token, model).enqueue(object : Callback<GetProvidersResponseModel> {
                    override fun onFailure(call: Call<GetProvidersResponseModel>, t: Throwable) {
                       // mShowProgress.postValue(Pair(false,""))
                        mToastMessage.postValue(t.localizedMessage)
                    }

                    override fun onResponse(call: Call<GetProvidersResponseModel>, response: Response<GetProvidersResponseModel>) {
                        mSearchedProvidersData.postValue(response.body()?.data)
                       // mShowProgress.postValue(Pair(false,""))
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
                mShowProgress.postValue(Pair(true,mApplicationContext.getString(R.string.txt_loading_provider)))
                mPpsApiService.doGetProviders(task.result?.token, model).enqueue(object : Callback<GetProvidersResponseModel> {
                    override fun onFailure(call: Call<GetProvidersResponseModel>, t: Throwable) {
                        mShowProgress.postValue(Pair(false,""))
                        mToastMessage.postValue(t.localizedMessage)
                    }

                    override fun onResponse(call: Call<GetProvidersResponseModel>, response: Response<GetProvidersResponseModel>) {
                        mProvidersData.postValue(response.body()?.data)
                        mShowProgress.postValue(Pair(false,""))
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
                mShowProgress.postValue(Pair(true,mApplicationContext.getString(R.string.txt_loading_searching_provider_detail)))
                mPpsApiService.doGetProviderDetails(task.result?.token,  GetProviderDetailRequestModel(mBusinessId)).enqueue(object : Callback<GetProviderDetailsResponseModel> {
                    override fun onFailure(call: Call<GetProviderDetailsResponseModel>, t: Throwable) {
                        mShowProgress.postValue(Pair(false,""))
                        mToastMessage.postValue(t.localizedMessage)
                    }

                    override fun onResponse(call: Call<GetProviderDetailsResponseModel>, response: Response<GetProviderDetailsResponseModel>) {
                        mProvidersDetailData.postValue(response.body()?.data)
                        mShowProgress.postValue(Pair(false,""))
                    }

                })
            } else {
                mToastMessage.postValue(task.exception?.localizedMessage)
            }

        })
    }

    fun doPlaceOrder(model: PlaceOrderRequestModel?) {
        FirebaseAuth.getInstance().currentUser?.getIdToken(false)?.addOnCompleteListener(OnCompleteListener { task ->
            if (task.isSuccessful) {
                mShowProgress.postValue(Pair(true,mApplicationContext.getString(R.string.txt_loading_place_order)))
                mPpsApiService.doPlaceOrder(task.result?.token,model).enqueue(object : Callback<BaseResponseModel> {
                    override fun onFailure(call: Call<BaseResponseModel>, t: Throwable) {
                        mShowProgress.postValue(Pair(false,""))
                        mToastMessage.postValue(t.localizedMessage)
                    }

                    override fun onResponse(call: Call<BaseResponseModel>, response: Response<BaseResponseModel>) {
                        mPlaceOrderResponse.postValue(Pair(response?.body()?.isSuccess,response?.body()?.message))
                        mShowProgress.postValue(Pair(false,""))
                    }
                })
            } else {
                mToastMessage.postValue(task.exception?.localizedMessage)
            }
        })
    }

    fun doGetCustomFilterParameters() {
        FirebaseAuth.getInstance().currentUser?.getIdToken(false)?.addOnCompleteListener(OnCompleteListener { task ->
            if (task.isSuccessful) {
                mShowProgress.postValue(Pair(true,mApplicationContext.getString(R.string.txt_loading)))
                mPpsApiService.doGetFilterData(task.result?.token).enqueue(object : Callback<GetFilterDataResponseModel> {
                    override fun onFailure(call: Call<GetFilterDataResponseModel>, t: Throwable) {
                        mShowProgress.postValue(Pair(false,""))
                        mToastMessage.postValue(t.localizedMessage)
                    }

                    override fun onResponse(call: Call<GetFilterDataResponseModel>, response: Response<GetFilterDataResponseModel>) {
                        mCustomFilterData.postValue(response?.body()?.data)
                        mShowProgress.postValue(Pair(false,""))
                    }
                })
            } else {
                mToastMessage.postValue(task.exception?.localizedMessage)
            }
        })
    }

    fun doGetCustomFilterProviders(model : GetFilterProviderRequestModel) {
        FirebaseAuth.getInstance().currentUser?.getIdToken(false)?.addOnCompleteListener(OnCompleteListener { task ->
            if (task.isSuccessful) {
                mShowProgress.postValue(Pair(true,mApplicationContext.getString(R.string.txt_loading_searching_provider_detail)))
                mPpsApiService.doFilterProviders(task.result?.token, model ).enqueue(object : Callback<GetProvidersResponseModel> {
                    override fun onFailure(call: Call<GetProvidersResponseModel>, t: Throwable) {
                        mShowProgress.postValue(Pair(false,""))
                        mToastMessage.postValue(t.localizedMessage)
                    }

                    override fun onResponse(call: Call<GetProvidersResponseModel>, response: Response<GetProvidersResponseModel>) {
                        mProvidersData.postValue(response.body()?.data)
                        mShowProgress.postValue(Pair(false,""))
                    }

                })
            } else {
                mToastMessage.postValue(task.exception?.localizedMessage)
            }

        })
    }

    fun doGetReviews(id: GetReviewRequestModel?) {
        FirebaseAuth.getInstance().currentUser?.getIdToken(false)?.addOnCompleteListener(OnCompleteListener { task ->
            if (task.isSuccessful) {
                mShowProgress.postValue(Pair(true,""))
                mPpsApiService.doGetReview(task.result?.token, id).enqueue(object : Callback<GetReviewResponseModel> {
                    override fun onFailure(call: Call<GetReviewResponseModel>, t: Throwable) {
                        mShowProgress.postValue(Pair(false,""))
                        mToastMessage.postValue(t.localizedMessage)
                    }

                    override fun onResponse(call: Call<GetReviewResponseModel>, response: Response<GetReviewResponseModel>) {
                        mReviewModel.postValue(response.body()?.data)
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