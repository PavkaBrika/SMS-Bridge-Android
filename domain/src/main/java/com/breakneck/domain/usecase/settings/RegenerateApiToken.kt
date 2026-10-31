package com.breakneck.domain.usecase.settings

import com.breakneck.domain.repository.SettingsRepository

class RegenerateApiToken(private val settingsRepository: SettingsRepository) {

    fun execute(): String {
        return settingsRepository.regenerateApiToken()
    }
}
