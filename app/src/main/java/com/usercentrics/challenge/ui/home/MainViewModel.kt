package com.usercentrics.challenge.ui.home

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.usercentrics.challenge.consentlib.ConsentBanner
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor() : ViewModel() {

    private val _totalConsentScore = MutableStateFlow(0)
    val totalConsentScore: StateFlow<Int>
        get() = _totalConsentScore.asStateFlow()

    fun showConsentBanner(context: Context) {
        viewModelScope.launch A@{
            ConsentBanner.showConsentBanner(context = context) { score ->
                viewModelScope.launch B@{
                    _totalConsentScore.emit(score)
                }
            }
        }
    }
}