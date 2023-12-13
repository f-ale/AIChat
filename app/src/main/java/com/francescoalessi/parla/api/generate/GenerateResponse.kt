package com.francescoalessi.parla.api.generate

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class GenerateResponse(
    val id: String,
    @SerialName("object")
    val objectType: String,
    val created: Long,
    val model: String,
    val choices: List<Choice>,
    val usage: Usage
)

@Serializable
data class Choice(
    val index: Int,
    val finish_reason: String,
    val text: String,
    val logprobs: LogProbs // Assuming you need this part as well
)

@Serializable
data class LogProbs(
    val top_logprobs: List<Map<String, Double>> // Update this based on the actual structure
)

@Serializable
data class Usage(
    val prompt_tokens: Int,
    val completion_tokens: Int,
    val total_tokens: Int
)