package com.breakneck.data.sharedpreference

import android.content.Context
import android.util.Base64
import com.breakneck.data.entity.IpAddressData
import com.breakneck.data.entity.MessageDestinationUrlData
import com.breakneck.data.entity.PortData
import com.breakneck.data.entity.RemainingAdsQuantityData
import com.breakneck.data.storage.SettingsStorage
import com.breakneck.domain.TOTAL_ADS_QUANTITY
import java.security.SecureRandom
import androidx.core.content.edit

const val SETTINGS_SHARED_PREFERENCES_NAME = "SETTINGS_SHARED_PREFERENCES"
const val API_SECURITY_SHARED_PREFERENCES_NAME = "SMS_API_SECURITY"
const val PORT = "PORT"
const val MESSAGE_DESTINATION_URL = "MESSAGE_DESTINATION_URL"
const val DEVICE_IP_ADDRESS = "DEVICE_IP_ADDRESS"
const val REMIND_NOTIFICATION = "REMIND_NOTIFICATION"
const val REMAINING_ADS = "REMAINING_ADS"
const val FIRST_TIME_APP_OPENED = "FIRST_TIME_APP_OPENED"
const val API_TOKEN = "API_TOKEN"

class SettingsSharedPreference(private val context: Context): SettingsStorage {

    private val sharedPreference = context.getSharedPreferences(SETTINGS_SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)
    private val apiSecurityPreferences = context.getSharedPreferences(
        API_SECURITY_SHARED_PREFERENCES_NAME,
        Context.MODE_PRIVATE
    )

    override fun savePort(port: PortData) {
        sharedPreference.edit().putInt(PORT, port.value).apply()
    }

    override fun getPort(): PortData {
        return PortData(sharedPreference.getInt(PORT, 5555))
    }

    @Synchronized
    override fun getApiToken(): String {
        return apiSecurityPreferences.getString(API_TOKEN, null) ?: createAndSaveApiToken()
    }

    @Synchronized
    override fun regenerateApiToken(): String {
        return createAndSaveApiToken()
    }

    private fun createAndSaveApiToken(): String {
        val randomBytes = ByteArray(32)
        SecureRandom().nextBytes(randomBytes)
        val token = Base64.encodeToString(
            randomBytes,
            Base64.URL_SAFE or Base64.NO_WRAP or Base64.NO_PADDING
        )
        apiSecurityPreferences.edit { putString(API_TOKEN, token) }
        return token
    }

    override fun saveMessageDestinationUrl(url: MessageDestinationUrlData) {
        sharedPreference.edit().putString(MESSAGE_DESTINATION_URL, url.value).apply()
    }

    override fun getMessageDestinationUrl(): MessageDestinationUrlData {
        return MessageDestinationUrlData(value = sharedPreference.getString(MESSAGE_DESTINATION_URL, "")!!)
    }

    override fun saveDeviceIpAddress(ipAddress: IpAddressData) {
        sharedPreference.edit().putString(DEVICE_IP_ADDRESS, ipAddress.value).apply()
    }

    override fun getDeviceIpAddress(): IpAddressData {
        return IpAddressData(value = sharedPreference.getString(DEVICE_IP_ADDRESS, "")!!)
    }

    override fun saveRemindNotificationTime(timeInMillis: Long) {
        sharedPreference.edit().putLong(REMIND_NOTIFICATION, timeInMillis).apply()
    }

    override fun getRemindNotificationTime(): Long {
        return sharedPreference.getLong(REMIND_NOTIFICATION, 0)
    }

    override fun getRemainingAdsQuantity(): RemainingAdsQuantityData {
        return RemainingAdsQuantityData(value = sharedPreference.getInt(REMAINING_ADS, TOTAL_ADS_QUANTITY))
    }

    override fun saveRemainingAdsQuantity(quantity: RemainingAdsQuantityData) {
        sharedPreference.edit().putInt(REMAINING_ADS, quantity.value).apply()
    }

    override fun getIsFirstTimeAppOpened(): Boolean {
        return sharedPreference.getBoolean(FIRST_TIME_APP_OPENED, false)
    }

    override fun saveIsFirstTimeAppOpened(isOpened: Boolean) {
        sharedPreference.edit().putBoolean(FIRST_TIME_APP_OPENED, isOpened).apply()
    }
}
