package com.himanshu.toxtap.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.himanshu.toxtap.data.PreferenceManager
import com.himanshu.toxtap.model.GestureAction
import com.himanshu.toxtap.model.GestureType
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class GestureViewModel(application: Application) : AndroidViewModel(application) {
    private val prefManager = PreferenceManager(application)

    val isOverlayEnabled: StateFlow<Boolean> = prefManager.isOverlayEnabled
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    fun setOverlayEnabled(enabled: Boolean) {
        viewModelScope.launch {
            prefManager.setOverlayEnabled(enabled)
        }
    }

    fun getGestureAction(type: GestureType) = prefManager.getGestureAction(type)

    fun saveGestureAction(type: GestureType, action: GestureAction) {
        viewModelScope.launch {
            prefManager.saveGestureAction(type, action)
        }
    }
}
