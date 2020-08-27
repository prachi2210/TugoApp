package com.tugoapp.mobile.ui.orderdetail

import android.app.Application
import android.content.Context
import bolts.Bolts
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.tugoapp.mobile.data.remote.MerchantApiService
import com.tugoapp.mobile.data.remote.model.request.AddReviewRequestModel
import com.tugoapp.mobile.data.remote.model.request.ResumeOrderRequestModel
import com.tugoapp.mobile.data.remote.model.response.BaseResponseModel
import com.tugoapp.mobile.data.remote.model.response.OrdersResponseModel
import com.tugoapp.mobile.ui.base.BaseViewModel
import com.tugoapp.mobile.ui.base.SingleLiveEvent
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OrderDetailsViewModel(application: Application?, private val mPpsApiService: MerchantApiService) : BaseViewModel(application) {
    var mToastMessage: SingleLiveEvent<String> = SingleLiveEvent()
    var mShowProgress: SingleLiveEvent<Pair<Boolean, String>> = SingleLiveEvent()
    var mIsPausePlanDone: SingleLiveEvent<Pair<Int?,String?>> = SingleLiveEvent()
    var mIsCancelPlanDone: SingleLiveEvent<Pair<Int?,String?>> = SingleLiveEvent()
    var mIsResumePlanDone: SingleLiveEvent<Pair<Int?,String?>> = SingleLiveEvent()
    var mIsReviewAdded: SingleLiveEvent<Int> = SingleLiveEvent()
    
    fun doPausePlan(model: ResumeOrderRequestModel) {
        FirebaseAuth.getInstance().currentUser?.getIdToken(false)?.addOnCompleteListener(OnCompleteListener { task ->
            if (task.isSuccessful) {
                mShowProgress.postValue(Pair(true,""))
                mPpsApiService.doPauseOrder(task.result?.token,model).enqueue(object : Callback<BaseResponseModel> {
                    override fun onFailure(call: Call<BaseResponseModel>, t: Throwable) {
                        mShowProgress.postValue(Pair(false,""))
                        mToastMessage.postValue(t.localizedMessage)
                    }

                    override fun onResponse(call: Call<BaseResponseModel>, response: Response<BaseResponseModel>) {
                        mIsPausePlanDone.postValue(Pair(response.body()?.isSuccess,response?.body()?.message))
                        mShowProgress.postValue(Pair(false,""))
                    }

                })
            } else {
                mToastMessage.postValue(task.exception?.localizedMessage)
            }
        })
    }

    fun doResumePlan(model: ResumeOrderRequestModel) {
        FirebaseAuth.getInstance().currentUser?.getIdToken(false)?.addOnCompleteListener(OnCompleteListener { task ->
            if (task.isSuccessful) {
                mShowProgress.postValue(Pair(true,""))
                mPpsApiService.doResumeOrder(task.result?.token,model).enqueue(object : Callback<BaseResponseModel> {
                    override fun onFailure(call: Call<BaseResponseModel>, t: Throwable) {
                        mShowProgress.postValue(Pair(false,""))
                        mToastMessage.postValue(t.localizedMessage)
                    }

                    override fun onResponse(call: Call<BaseResponseModel>, response: Response<BaseResponseModel>) {
                        mIsResumePlanDone.postValue(Pair(response.body()?.isSuccess,response?.body()?.message))
                        mShowProgress.postValue(Pair(false,""))
                    }

                })
            } else {
                mToastMessage.postValue(task.exception?.localizedMessage)
            }
        })
    }

    fun doCancelPlan(model: ResumeOrderRequestModel) {
        FirebaseAuth.getInstance().currentUser?.getIdToken(false)?.addOnCompleteListener(OnCompleteListener { task ->
            if (task.isSuccessful) {
                mShowProgress.postValue(Pair(true,""))
                mPpsApiService.doCancelPlan(task.result?.token,model).enqueue(object : Callback<BaseResponseModel> {
                    override fun onFailure(call: Call<BaseResponseModel>, t: Throwable) {
                        mShowProgress.postValue(Pair(false,""))
                        mToastMessage.postValue(t.localizedMessage)
                    }

                    override fun onResponse(call: Call<BaseResponseModel>, response: Response<BaseResponseModel>) {
                        mIsCancelPlanDone.postValue(Pair(response.body()?.isSuccess,response?.body()?.message))
                        mShowProgress.postValue(Pair(false,""))
                    }

                })
            } else {
                mToastMessage.postValue(task.exception?.localizedMessage)
            }
        })
    }

    fun doSubmitReview(model: AddReviewRequestModel) {
        FirebaseAuth.getInstance().currentUser?.getIdToken(false)?.addOnCompleteListener(OnCompleteListener { task ->
            if (task.isSuccessful) {
                mShowProgress.postValue(Pair(true,""))
                mPpsApiService.doAddReview(task.result?.token,model).enqueue(object : Callback<BaseResponseModel> {
                    override fun onFailure(call: Call<BaseResponseModel>, t: Throwable) {
                        mShowProgress.postValue(Pair(false,""))
                        mToastMessage.postValue(t.localizedMessage)
                    }

                    override fun onResponse(call: Call<BaseResponseModel>, response: Response<BaseResponseModel>) {
                        mIsReviewAdded.postValue(response.body()?.isSuccess)
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