package com.francescoalessi.sagai.api.model

import kotlinx.serialization.Serializable

@Serializable
data class ModelInfoRequest
(
    val action: String = "info"
)