package com.francescoalessi.parla.api

import com.francescoalessi.parla.api.generate.GenerateRequest
import com.francescoalessi.parla.api.generate.GenerateResponse
import com.francescoalessi.parla.api.model.ModelInfoRequest
import com.francescoalessi.parla.api.model.ModelInfoResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface TextGenerationService
{
    /*
     *  Defines the queries to retrieve results from the HN API
     */
    @POST("/v1/completions")
    suspend fun generateText(
        @Body request: GenerateRequest
    ): GenerateResponse

    @POST("/v1/model")
    suspend fun getModelInfo(
        @Body request: ModelInfoRequest
    ): ModelInfoResponse
}