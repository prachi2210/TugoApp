package com.tugoapp.mobile.ui.home

import android.app.Application
import android.content.Context
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.gson.Gson
import com.tugoapp.mobile.R
import com.tugoapp.mobile.data.remote.MerchantApiService
import com.tugoapp.mobile.data.remote.model.request.*
import com.tugoapp.mobile.data.remote.model.response.*
import com.tugoapp.mobile.ui.base.BaseViewModel
import com.tugoapp.mobile.ui.base.SingleLiveEvent
import com.tugoapp.mobile.utils.AppConstant
import com.tugoapp.mobile.utils.NetworkUtils
import com.tugoapp.mobile.utils.SharedPrefsUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel(application: Application?, private val mPpsApiService: MerchantApiService) : BaseViewModel(application) {

    var mCategoryData: SingleLiveEvent<ArrayList<CategoryDetailModel>> = SingleLiveEvent()
    var mProvidersData: SingleLiveEvent<ArrayList<ProviderModel>> = SingleLiveEvent()
    var mSearchedProvidersData: SingleLiveEvent<ArrayList<ProviderModel>> = SingleLiveEvent()
    var mProvidersDetailData: SingleLiveEvent<GetProviderDetailsData> = SingleLiveEvent()
    var mReviewModel: SingleLiveEvent<ReviewMainModel> = SingleLiveEvent()
    var mFavoriteData: SingleLiveEvent<ArrayList<FavoriteModel>> = SingleLiveEvent()


    var mToastMessage: SingleLiveEvent<String> = SingleLiveEvent()
    var mShowProgress: SingleLiveEvent<Pair<Boolean,String>> = SingleLiveEvent()
    var mPlaceOrderResponse: SingleLiveEvent<Pair<Int?,PlaceOrderResponseModel?>> = SingleLiveEvent()
    var mCustomFilterData: SingleLiveEvent<FilterModel> = SingleLiveEvent()

    var mIsPaymentConfigInfoUpdated: SingleLiveEvent<Boolean> = SingleLiveEvent()


    var mSubmitQueryResponse: SingleLiveEvent<Pair<Int?,String?>> = SingleLiveEvent()


    fun doLoadCategory() {
        if (NetworkUtils.isNetworkConnected(mApplicationContext)) {
            FirebaseAuth.getInstance().currentUser?.getIdToken(false)?.addOnCompleteListener(OnCompleteListener { task ->
                if (task.isSuccessful) {
                    mShowProgress.postValue(Pair(true, ""))
                    mPpsApiService.doGetCategories(task.result?.token).enqueue(object : Callback<GetCategoryResponseModel> {
                        override fun onFailure(call: Call<GetCategoryResponseModel>, t: Throwable) {
                            mShowProgress.postValue(Pair(false, ""))
                            mToastMessage.postValue(t.localizedMessage)
                        }

                        override fun onResponse(call: Call<GetCategoryResponseModel>, response: Response<GetCategoryResponseModel>) {
                            mCategoryData.postValue(response.body()?.data)
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

    fun doSearchTerm(model : GetProvidersRequestModel) {
        if (NetworkUtils.isNetworkConnected(mApplicationContext)) {
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
        } else {
            mToastMessage.postValue(mApplicationContext.getString(R.string.txt_no_internet))
        }
    }

    fun doLoadProviders(model : GetProvidersRequestModel) {
        if (NetworkUtils.isNetworkConnected(mApplicationContext)) {
            FirebaseAuth.getInstance().currentUser?.getIdToken(false)?.addOnCompleteListener(OnCompleteListener { task ->
                if (task.isSuccessful) {
                    mShowProgress.postValue(Pair(true, mApplicationContext.getString(R.string.txt_loading_provider)))
                    mPpsApiService.doGetProviders(task.result?.token, model).enqueue(object : Callback<GetProvidersResponseModel> {
                        override fun onFailure(call: Call<GetProvidersResponseModel>, t: Throwable) {
                            mShowProgress.postValue(Pair(false, ""))
                            mToastMessage.postValue(t.localizedMessage)
                        }

                        override fun onResponse(call: Call<GetProvidersResponseModel>, response: Response<GetProvidersResponseModel>) {
                            mProvidersData.postValue(response.body()?.data)
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

    fun doGetProviderDetails(mBusinessId: String) {
        if (NetworkUtils.isNetworkConnected(mApplicationContext)) {
            FirebaseAuth.getInstance().currentUser?.getIdToken(false)?.addOnCompleteListener(OnCompleteListener { task ->
                if (task.isSuccessful) {
                    mShowProgress.postValue(Pair(true, mApplicationContext.getString(R.string.txt_loading_searching_provider_detail)))
                    mPpsApiService.doGetProviderDetailsAdvance(task.result?.token, GetProviderDetailRequestModel(mBusinessId)).enqueue(object : Callback<GetProviderDetailsResponseModel> {
                        override fun onFailure(call: Call<GetProviderDetailsResponseModel>, t: Throwable) {
                            mShowProgress.postValue(Pair(false, ""))
                            mToastMessage.postValue(t.localizedMessage)
                        }

                        override fun onResponse(call: Call<GetProviderDetailsResponseModel>, response: Response<GetProviderDetailsResponseModel>) {
                            mProvidersDetailData.postValue(response.body()?.data)
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

    fun doPlaceOrder(model: PlaceOrderRequestModel?) {
        if (NetworkUtils.isNetworkConnected(mApplicationContext)) {
            FirebaseAuth.getInstance().currentUser?.getIdToken(false)?.addOnCompleteListener(OnCompleteListener { task ->
                if (task.isSuccessful) {
                    mShowProgress.postValue(Pair(true, mApplicationContext.getString(R.string.txt_loading_place_order)))
                    mPpsApiService.doPlaceOrderV2(task.result?.token, model).enqueue(object : Callback<PlaceOrderResponseModel> {
                        override fun onFailure(call: Call<PlaceOrderResponseModel>, t: Throwable) {
                            mShowProgress.postValue(Pair(false, ""))
                            mToastMessage.postValue(t.localizedMessage)
                        }

                        override fun onResponse(call: Call<PlaceOrderResponseModel>, response: Response<PlaceOrderResponseModel>) {
                            mPlaceOrderResponse.postValue(Pair(response.body()?.isSuccess, response.body()))
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

    fun doGetCustomFilterParameters() {
        if (NetworkUtils.isNetworkConnected(mApplicationContext)) {
            FirebaseAuth.getInstance().currentUser?.getIdToken(false)?.addOnCompleteListener(OnCompleteListener { task ->
                if (task.isSuccessful) {
                    mShowProgress.postValue(Pair(true, mApplicationContext.getString(R.string.txt_loading)))
                    mPpsApiService.doGetFilterData(task.result?.token).enqueue(object : Callback<GetFilterDataResponseModel> {
                        override fun onFailure(call: Call<GetFilterDataResponseModel>, t: Throwable) {
                            mShowProgress.postValue(Pair(false, ""))
                            mToastMessage.postValue(t.localizedMessage)
                        }

                        override fun onResponse(call: Call<GetFilterDataResponseModel>, response: Response<GetFilterDataResponseModel>) {
                            mCustomFilterData.postValue(response.body()?.data)
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

    fun doGetCustomFilterProviders(model : GetFilterProviderRequestModel) {
        if (NetworkUtils.isNetworkConnected(mApplicationContext)) {
            FirebaseAuth.getInstance().currentUser?.getIdToken(false)?.addOnCompleteListener(OnCompleteListener { task ->
                if (task.isSuccessful) {
                    mShowProgress.postValue(Pair(true, mApplicationContext.getString(R.string.txt_loading_searching_provider_detail)))
                    mPpsApiService.doFilterProviders(task.result?.token, model).enqueue(object : Callback<GetProvidersResponseModel> {
                        override fun onFailure(call: Call<GetProvidersResponseModel>, t: Throwable) {
                            mShowProgress.postValue(Pair(false, ""))
                            mToastMessage.postValue(t.localizedMessage)
                        }

                        override fun onResponse(call: Call<GetProvidersResponseModel>, response: Response<GetProvidersResponseModel>) {
                            mProvidersData.postValue(response.body()?.data)
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

    fun doGetReviews(id: GetReviewRequestModel?) {
        if (NetworkUtils.isNetworkConnected(mApplicationContext)) {
            FirebaseAuth.getInstance().currentUser?.getIdToken(false)?.addOnCompleteListener(OnCompleteListener { task ->
                if (task.isSuccessful) {
                    mShowProgress.postValue(Pair(true, ""))
                    mPpsApiService.doGetReview(task.result?.token, id).enqueue(object : Callback<GetReviewResponseModel> {
                        override fun onFailure(call: Call<GetReviewResponseModel>, t: Throwable) {
                            mShowProgress.postValue(Pair(false, ""))
                            mToastMessage.postValue(t.localizedMessage)
                        }

                        override fun onResponse(call: Call<GetReviewResponseModel>, response: Response<GetReviewResponseModel>) {
                            mReviewModel.postValue(response.body()?.data)
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

    fun doSetProviderFaviroute(businessId: String) {
        if (NetworkUtils.isNetworkConnected(mApplicationContext)) {
            FirebaseAuth.getInstance().currentUser?.getIdToken(false)?.addOnCompleteListener(OnCompleteListener { task ->
                if (task.isSuccessful) {
                    //  mShowProgress.postValue(Pair(true,""))
                    mPpsApiService.doAddToFavourite(task.result?.token, SetFavirouteProviderRequestModel(businessId)).enqueue(object : Callback<BaseResponseModel> {
                        override fun onFailure(call: Call<BaseResponseModel>, t: Throwable) {
                            //    mShowProgress.postValue(Pair(false,""))
                            mToastMessage.postValue("Network Issue: Failed to add this provider to favorite")
                        }

                        override fun onResponse(call: Call<BaseResponseModel>, response: Response<BaseResponseModel>) {
                            //   mReviewModel.postValue(response.body()?.data)
                            //    mShowProgress.postValue(Pair(false,""))
                        }

                    })
                } else {
                    // mToastMessage.postValue(task.exception?.localizedMessage)
                }

            })
        } else {
            mToastMessage.postValue(mApplicationContext.getString(R.string.txt_no_internet))
        }
    }

    fun doLoadFavorites() {
        if (NetworkUtils.isNetworkConnected(mApplicationContext)) {
            FirebaseAuth.getInstance().currentUser?.getIdToken(false)?.addOnCompleteListener(OnCompleteListener { task ->
                if (task.isSuccessful) {
                    mShowProgress.postValue(Pair(true, ""))
                    mPpsApiService.doGetFavorites(task.result?.token).enqueue(object : Callback<FavoriteResponseModel> {
                        override fun onFailure(call: Call<FavoriteResponseModel>, t: Throwable) {
                            mShowProgress.postValue(Pair(false, ""))
                            mToastMessage.postValue(t.localizedMessage)
                        }

                        override fun onResponse(call: Call<FavoriteResponseModel>, response: Response<FavoriteResponseModel>) {
                            mFavoriteData.postValue(response.body()?.data)
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

    fun doSubmitQuery(submitQueryRequestModel: SubmitQueryRequestModel) {
        if (NetworkUtils.isNetworkConnected(mApplicationContext)) {
            FirebaseAuth.getInstance().currentUser?.getIdToken(false)?.addOnCompleteListener(OnCompleteListener { task ->
                if (task.isSuccessful) {
                    mShowProgress.postValue(Pair(true, ""))
                    mPpsApiService.doSubmitQuery(task.result?.token, submitQueryRequestModel).enqueue(object : Callback<BaseResponseModel> {
                        override fun onFailure(call: Call<BaseResponseModel>, t: Throwable) {
                            mShowProgress.postValue(Pair(false, ""))
                            mToastMessage.postValue(t.localizedMessage)
                        }

                        override fun onResponse(call: Call<BaseResponseModel>, response: Response<BaseResponseModel>) {
                            mSubmitQueryResponse.postValue(Pair(response.body()?.isSuccess, response.body()?.message))
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

    fun doGetPaymentConfigs() {
        FirebaseAuth.getInstance().currentUser?.getIdToken(false)?.addOnCompleteListener(OnCompleteListener { task ->
            if (task.isSuccessful) {
                mShowProgress.postValue(Pair(true,""))
                mPpsApiService.doGetPaymentConfig(task.result?.token).enqueue(object : Callback<PaymentConfigResponseModel> {
                    override fun onFailure(call: Call<PaymentConfigResponseModel>, t: Throwable) {
                        mShowProgress.postValue(Pair(false,""))
                    }

                    override fun onResponse(call: Call<PaymentConfigResponseModel>, response: Response<PaymentConfigResponseModel>) {
                        if(response?.body() != null && response.isSuccessful) {
                            SharedPrefsUtils.setStringPreference(mApplicationContext, AppConstant.PAYMENT_CONFIG_INFO, Gson().toJson(response.body()?.data))
                            mIsPaymentConfigInfoUpdated.postValue(true)
                        } else {
                            mIsPaymentConfigInfoUpdated.postValue(false)
                        }
                        mShowProgress.postValue(Pair(false,""))
                    }
                })
            } else {
                mShowProgress.postValue(Pair(false,""))
                mToastMessage.postValue(task.exception?.localizedMessage)
            }

        })
    }


    private val mApplicationContext: Context = getApplication<Application>().applicationContext
}