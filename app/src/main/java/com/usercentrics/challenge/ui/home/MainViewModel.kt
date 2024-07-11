package com.usercentrics.challenge.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.usercentrics.challenge.di.DefaultDispatcher
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(@DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher) :
    ViewModel() {

    private val _totalConsentScore = MutableLiveData(0)
    val totalConsentScore: LiveData<Int>
        get() = _totalConsentScore

    private val _listGivenConsent = MutableLiveData<List<Pair<String, Int>>>()
    val listGivenConsent: LiveData<List<Pair<String, Int>>>
        get() = _listGivenConsent


}