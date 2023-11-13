package com.francescoalessi.parla.repositories

import com.francescoalessi.parla.data.TextGenerationHost
import com.francescoalessi.parla.data.dao.SettingsDao
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SettingsRepository @Inject constructor(
    private val settingsDao: SettingsDao,
) {
    suspend fun getTextGenerationHost(): TextGenerationHost? =
        settingsDao.getTextGenerationHost()

    suspend fun saveTextGenerationHost(textGenerationHost: TextGenerationHost) =
        settingsDao.insert(textGenerationHost)

    fun getTextGenerationHostAsFlow(): Flow<TextGenerationHost> =
        settingsDao.getTextGenerationHostAsFlow()
}