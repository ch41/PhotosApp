package com.example.photosapp.common.base

import android.net.ConnectivityManager
import android.net.Network
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.photosapp.common.utils.NetworkMonitor
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

abstract class BaseViewModel(
    private val networkMonitor: NetworkMonitor
) : ViewModel() {


    private val networkCallback = object : ConnectivityManager.NetworkCallback() {
        override fun onLost(network: Network) {
            super.onLost(network)
            _networkState.value = false
        }

        override fun onAvailable(network: Network) {
            super.onAvailable(network)
            _networkState.value = true
        }
    }
    private val _networkState = MutableStateFlow(networkMonitor.isNetworkAvailable())
    val networkState: StateFlow<Boolean> get() = _networkState

    init {
        networkMonitor.registerNetworkCallback(networkCallback)
    }

    override fun onCleared() {
        super.onCleared()
        networkMonitor.unregisterNetworkCallback(networkCallback)
    }

}