package com.francescoalessi.sagai.api.interceptors

import com.francescoalessi.sagai.data.TextGenerationHost
import com.francescoalessi.sagai.data.isValidHost
import com.francescoalessi.sagai.repositories.SettingsRepository
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class DynamicUrlInterceptor @Inject constructor(
    private val settingsRepository: SettingsRepository
) : Interceptor {
    // TODO: Also let user choose between http and https
    override fun intercept(chain: Interceptor.Chain): Response {
        val dynamicBaseUrl = getDynamicBaseUrl()
        val originalRequest = chain.request()
        if (dynamicBaseUrl != null) {
            val newUrl = originalRequest.url.newBuilder()
                .scheme(originalRequest.url.scheme)
                .host(dynamicBaseUrl.ipAddress)
                .port(dynamicBaseUrl.ipPort.toInt())
                .build()
            val newRequest = originalRequest.newBuilder()
                .url(newUrl)
                .build()
            return chain.proceed(newRequest)
        } else {
            return chain.proceed(originalRequest)
        }

    }

    private fun getDynamicBaseUrl(): TextGenerationHost? {
        val textGenerationHost = runBlocking { settingsRepository.getTextGenerationHost() }

        if (textGenerationHost != null) {
            if (textGenerationHost.isValidHost()) {
                return textGenerationHost
            }
        }
        return null
    }
}