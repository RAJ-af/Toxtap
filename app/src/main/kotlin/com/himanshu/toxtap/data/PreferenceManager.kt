package com.himanshu.toxtap.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.himanshu.toxtap.model.ActionType
import com.himanshu.toxtap.model.GestureAction
import com.himanshu.toxtap.model.GestureType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "toxtap_prefs")

class PreferenceManager(private val context: Context) {

    companion object {
        val OVERLAY_ENABLED = booleanPreferencesKey("overlay_enabled")

        fun getGestureKey(type: GestureType) = stringPreferencesKey("gesture_${type.name}")
        fun getGestureDataKey(type: GestureType) = stringPreferencesKey("gesture_data_${type.name}")
        fun getGestureLabelKey(type: GestureType) = stringPreferencesKey("gesture_label_${type.name}")
    }

    val isOverlayEnabled: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[OVERLAY_ENABLED] ?: false
    }

    suspend fun setOverlayEnabled(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[OVERLAY_ENABLED] = enabled
        }
    }

    fun getGestureAction(type: GestureType): Flow<GestureAction> = context.dataStore.data.map { preferences ->
        val actionTypeName = preferences[getGestureKey(type)] ?: ActionType.NONE.name
        val actionType = try { ActionType.valueOf(actionTypeName) } catch (e: Exception) { ActionType.NONE }
        val data = preferences[getGestureDataKey(type)]
        val label = preferences[getGestureLabelKey(type)] ?: "None"

        GestureAction(actionType, data, label)
    }

    suspend fun saveGestureAction(type: GestureType, action: GestureAction) {
        context.dataStore.edit { preferences ->
            preferences[getGestureKey(type)] = action.type.name
            action.data?.let { preferences[getGestureDataKey(type)] = it } ?: preferences.remove(getGestureDataKey(type))
            preferences[getGestureLabelKey(type)] = action.label
        }
    }
}
