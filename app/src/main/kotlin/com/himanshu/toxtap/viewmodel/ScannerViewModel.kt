package com.himanshu.toxtap.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.himanshu.toxtap.model.ScannedSetting
import com.himanshu.toxtap.util.SettingsScanner
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ScannerViewModel(application: Application) : AndroidViewModel(application) {
    private val _settings = MutableStateFlow<List<ScannedSetting>>(emptyList())
    val settings: StateFlow<List<ScannedSetting>> = _settings

    fun scan() {
        viewModelScope.launch {
            _settings.value = SettingsScanner.scanSettings(getApplication())
        }
    }
}
