package com.francescoalessi.sagai.api.generate

import kotlinx.serialization.Serializable

@Serializable
data class GenerateRequest(
    val prompt: String = "",
    val max_new_tokens: Int = 550, // Max new tokens: TODO Let the user set it?
    val do_sample: Boolean = true,
    val temperature: Double = 0.44,
    val top_p: Double = 1.0,
    val typical_p: Double = 1.0,
    val repetition_penalty: Double = 1.15,
    val repetition_penalty_range: Int = 0,
    val encoder_repetition_penalty: Int = 1,
    val top_k: Int = 0,
    val min_length: Int = 0,
    val no_repeat_ngram_size: Int = 0,
    val num_beams: Int = 1,
    val penalty_alpha: Int = 0,
    val length_penalty: Int = 1,
    val early_stopping: Boolean = false,
    val guidance_scale: Double = 1.0,
    val negative_prompt: String = "",
    val seed: Int = -1,
    val add_bos_token: Boolean = true,
    val stopping_strings: List<String> = listOf("", ""),
    val truncation_length: Int = 4096, // Max context size: TODO can we derive this from a network call?
    val ban_eos_token: Boolean = false,
    val skip_special_tokens: Boolean = true,
    val top_a: Int = 0,
    val tfs: Int = 1,
    val epsilon_cutoff: Int = 0,
    val eta_cutoff: Int = 0,
    val mirostat_mode: Int = 0,
    val mirostat_tau: Int = 5,
    val mirostat_eta: Double = 0.1,
    val use_mancer: Boolean = false
)