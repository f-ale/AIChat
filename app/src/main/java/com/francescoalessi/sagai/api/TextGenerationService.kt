package com.francescoalessi.sagai.api

import com.francescoalessi.sagai.api.generate.GenerateRequest
import com.francescoalessi.sagai.api.generate.GenerateResponse
import com.francescoalessi.sagai.api.model.ModelInfoRequest
import com.francescoalessi.sagai.api.model.ModelInfoResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface TextGenerationService
{
    /*
     *  Defines the queries to retrieve results from the HN API
     */
    @POST("/api/v1/generate")
    suspend fun generateText(
        @Body request: GenerateRequest
    ): GenerateResponse

    @POST("/api/v1/model")
    suspend fun getModelInfo(
        @Body request: ModelInfoRequest
    ): ModelInfoResponse
}