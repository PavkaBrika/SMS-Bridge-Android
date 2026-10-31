package com.breakneck.sms_modem.service.api.dto

internal data class SendSmsApiRequest(
    val phoneNumber: String? = null,
    val message: String? = null
)
