package com.example.expense.viewmodel

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ThemeViewModelFactory(private val prefs: SharedPreferences) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        require(modelClass.isAssignableFrom(ThemeViewModel::class.java)) {
            "Unknown ViewModel class: ${modelClass.name}"
        }
        return ThemeViewModel(prefs) as T
    }
}
