package com.mkandeel.actionmemo.Helper

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.mkandeel.actionmemo.Helper.Constants.LANG
import com.mkandeel.actionmemo.Helper.Constants.MODE
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import kotlin.properties.Delegates

class DataStoreManager(context: Context) {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
    private val dataStore = context.dataStore

    // set attributes for each value we want to store
    companion object {
        val theme = booleanPreferencesKey(MODE)
        val lang = stringPreferencesKey(LANG)
    }

    suspend fun setTheme(isDark: Boolean) {
        dataStore.edit { prefs ->
            prefs[theme] = isDark
        }
    }

    fun getTheme(): Flow<Boolean> {
        return dataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }
            .map { prefs ->
                val uiMode = prefs[theme] ?: false
                uiMode
            }
    }
}