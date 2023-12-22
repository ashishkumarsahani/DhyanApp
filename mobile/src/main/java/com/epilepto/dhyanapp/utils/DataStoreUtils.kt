package com.epilepto.dhyanapp.utils

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

//While using proguard, mention this file so that it can't be removed.

class DataStoreUtils(
    private val context: Context
) {

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_details")

    suspend fun saveUsersFirstTime(isFirstTime: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[IS_FIRST_TIME] = isFirstTime
        }
    }

    fun getIsFirstTime(): Flow<Boolean> {
        return context.dataStore.data.map { preferences ->
            preferences[IS_FIRST_TIME] ?: true
        }
    }

    suspend fun setAdditionalDetailsFilled(isFilled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[IS_DETAILS_FILLED] = isFilled
        }
    }

    fun isAdditionalDetailsFilled(): Flow<Boolean> {
        return context.dataStore.data.map { preferences ->
            preferences[IS_DETAILS_FILLED] ?: false
        }
    }


    companion object {

        private val IS_FIRST_TIME = booleanPreferencesKey("is_first_time")
        private val IS_DETAILS_FILLED = booleanPreferencesKey("is_details_filled")
    }
}