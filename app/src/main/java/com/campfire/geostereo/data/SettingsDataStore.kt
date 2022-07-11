package com.campfire.geostereo.data

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.io.IOException


data class UserSettings(
    val deviceHasGPS: Boolean,
    val isOnBoardingFinished: Boolean
)
/**
 *  DataStore for containing onboarding details.
 */
data class UserPreferences(private val dataStore: DataStore<Preferences>) {

    private val TAG: String = "UserPreferences::class.java"

    private object PreferenceKeys {
        val DEVICE_HAS_GPS = booleanPreferencesKey("has_gps")
        val ONBOARDING_FINISHED = booleanPreferencesKey("onboarding_finished")
    }

    /**
     * Get the user preferences flow.
     */
    val userPreferencesFlow: Flow<UserSettings> = dataStore.data
        .catch { exception ->
            // dataStore.data throws an IOException when an error is encountered when reading data
            if (exception is IOException) {
                Log.e(TAG, "Error reading preferences.", exception)
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }.map { preferences ->
            mapUserPreferences(preferences)
        }

    suspend fun updateDeviceGPS(deviceHasGPS: Boolean) {
        dataStore.edit { preferences ->
            preferences[PreferenceKeys.DEVICE_HAS_GPS] = deviceHasGPS
        }
    }

    suspend fun updateOnBoardingFinished(onBoardingFinished: Boolean) {
        dataStore.edit { preferences ->
            preferences[PreferenceKeys.ONBOARDING_FINISHED] = onBoardingFinished
        }
    }

    suspend fun fetchInitialPreferences() =
        mapUserPreferences(dataStore.data.first().toPreferences())

    private fun mapUserPreferences(preferences: Preferences): UserSettings {
        val deviceHasGPS = preferences[PreferenceKeys.DEVICE_HAS_GPS] ?: false
        val onBoardingFinished = preferences[PreferenceKeys.ONBOARDING_FINISHED] ?: false

        return UserSettings(deviceHasGPS, onBoardingFinished)
    }
}


