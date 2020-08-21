package com.tugoapp.mobile.ui.orders

import android.app.Application
import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.tugoapp.mobile.data.remote.MerchantApiService
import com.tugoapp.mobile.data.remote.model.response.GetCategoryResponseModel
import com.tugoapp.mobile.data.remote.model.response.GetProviderDetailsData
import com.tugoapp.mobile.data.remote.model.response.OrderModel
import com.tugoapp.mobile.data.remote.model.response.OrdersResponseModel
import com.tugoapp.mobile.ui.base.BaseViewModel
import com.tugoapp.mobile.ui.base.SingleLiveEvent
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OrdersViewModel(application: Application?, private val mPpsApiService: MerchantApiService) : BaseViewModel(application) {

    private val mApplicationContext: Context = getApplication<Application>().applicationContext
    val mSelectedHistoryOrder = SingleLiveEvent<Pair<Boolean,OrderModel>>()
    var mHistoryOrderData: SingleLiveEvent<ArrayList<OrderModel>> = SingleLiveEvent()
    var mOngoingOrderData: SingleLiveEvent<ArrayList<OrderModel>> = SingleLiveEvent()
    var mToastMessage: SingleLiveEvent<String> = SingleLiveEvent()
    var mShowProgress: SingleLiveEvent<Pair<Boolean,String>> = SingleLiveEvent()

    val mExploreFood = SingleLiveEvent<Boolean>()


    fun setSelectedHistoryOrder(data: OrderModel,isHistoryTab : Boolean) {
        mSelectedHistoryOrder.postValue(Pair(isHistoryTab,data))
    }

    fun doExploreFood() {
        mExploreFood.postValue(true)
    }

    fun doGetHistoryOrders() {
        FirebaseAuth.getInstance().currentUser?.getIdToken(false)?.addOnCompleteListener(OnCompleteListener { task ->
            if (task.isSuccessful) {
                mShowProgress.postValue(Pair(true,""))
                mPpsApiService.doGetHistoryOrders(task.result?.token).enqueue(object : Callback<OrdersResponseModel> {
                    override fun onFailure(call: Call<OrdersResponseModel>, t: Throwable) {
                        mShowProgress.postValue(Pair(false,""))
                        mToastMessage.postValue(t.localizedMessage)
                    }

                    override fun onResponse(call: Call<OrdersResponseModel>, response: Response<OrdersResponseModel>) {
                        mHistoryOrderData.postValue(response.body()?.data)
                        mShowProgress.postValue(Pair(false,""))
                    }

                })
            } else {
                mToastMessage.postValue(task.exception?.localizedMessage)
            }
        })
    }

    fun doGetOngoingOrders() {
        FirebaseAuth.getInstance().currentUser?.getIdToken(false)?.addOnCompleteListener(OnCompleteListener { task ->
            if (task.isSuccessful) {
                mShowProgress.postValue(Pair(true,""))
                mPpsApiService.doGetOngoingOrders(task.result?.token).enqueue(object : Callback<OrdersResponseModel> {
                    override fun onFailure(call: Call<OrdersResponseModel>, t: Throwable) {
                        mShowProgress.postValue(Pair(false,""))
                        mToastMessage.postValue(t.localizedMessage)
                    }

                    override fun onResponse(call: Call<OrdersResponseModel>, response: Response<OrdersResponseModel>) {
                        mOngoingOrderData.postValue(response.body()?.data)
                        mShowProgress.postValue(Pair(false,""))
                    }

                })
            } else {
                mToastMessage.postValue(task.exception?.localizedMessage)
            }
        })
    }
}