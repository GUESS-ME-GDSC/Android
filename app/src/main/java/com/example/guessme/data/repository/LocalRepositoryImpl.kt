package com.example.guessme.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.guessme.data.repository.LocalRepositoryImpl.PreferencesKeys.TOKEN
import com.example.guessme.domain.repository.LocalRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

@AndroidEntryPoint
class LocalRepositoryImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>
): LocalRepository {
    override suspend fun saveToken(token: String) {
        dataStore.edit { prefs ->
            prefs[TOKEN] = token
        }
    }

    override suspend fun getToken(): Flow<String> {
        return dataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    exception.printStackTrace()
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }
            .map { prefs ->
                prefs[TOKEN] ?: ""
            }
    }

    private object PreferencesKeys {
        val TOKEN: Preferences.Key<String> = stringPreferencesKey("token")
    }
}