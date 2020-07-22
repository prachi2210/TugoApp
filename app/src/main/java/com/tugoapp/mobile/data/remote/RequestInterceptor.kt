package com.tugoapp.mobile.data.remote

import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class RequestInterceptor : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val originalHttpUrl = originalRequest.url()
        val url = originalHttpUrl.newBuilder() //                .addQueryParameter("api-key", ApiConstants.API_KEY)
                .build()
        val request = originalRequest.newBuilder().url(url).build()
        return chain.proceed(request)
    }
}