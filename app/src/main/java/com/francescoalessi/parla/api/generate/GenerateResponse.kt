package com.francescoalessi.parla.api.generate

import kotlinx.serialization.Serializable

@Serializable
data class GenerateResponse(
    val results: List<GenerateResultItem>
)

@Serializable
data class GenerateResultItem(
    val text: String
)