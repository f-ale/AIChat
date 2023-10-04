package com.francescoalessi.sagai.api.interceptors

import com.francescoalessi.sagai.data.TextGenerationHost
import com.francescoalessi.sagai.repositories.SettingsRepository
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class DynamicUrlInterceptor @Inject constructor(
    private val settingsRepository: SettingsRepository
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val dynamicBaseUrl = getDynamicBaseUrl()
        val originalRequest = chain.request()
        val newUrl = originalRequest.url.newBuilder()
            .scheme(originalRequest.url.scheme)
            .host(dynamicBaseUrl.ip4address)
            .port(dynamicBaseUrl.ip4port.toInt())
            .build()
        val newRequest = originalRequest.newBuilder()
            .url(newUrl)
            .build()
        return chain.proceed(newRequest)
    }

    private fun getDynamicBaseUrl(): TextGenerationHost
    {
        return runBlocking { settingsRepository.getTextGenerationHost() }
        // TODO: Verify data is valid
    }
}