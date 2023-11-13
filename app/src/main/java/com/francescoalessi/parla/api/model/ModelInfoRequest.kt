package com.francescoalessi.parla.api.model

import kotlinx.serialization.Serializable

@Serializable
data class ModelInfoRequest
(
    val action: String = "info"
)