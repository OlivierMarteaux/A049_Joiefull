package com.oliviermarteaux.a049_joiefull.ui.screens.item

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oliviermarteaux.a049_joiefull.data.repository.CbCheckoutRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

class CbCheckoutViewModel(
    private val repository: CbCheckoutRepository
) : ViewModel() {

    private val _paymentIntentClientSecret = MutableStateFlow<String?>(null)
    val paymentIntentClientSecret: StateFlow<String?> = _paymentIntentClientSecret.asStateFlow()

    private val _paymentResult = MutableStateFlow<Boolean?>(null)
    val paymentResult: StateFlow<Boolean?> = _paymentResult.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    var paymentToast: Boolean by mutableStateOf(false)
        private set

    fun showPaymentToast() = viewModelScope.launch {
        paymentToast = true
        delay(3000)
        paymentToast = false
    }

    fun prepareCheckout(amount: Double) {
        viewModelScope.launch {
            _isLoading.value = true
            val secret = repository.getPaymentIntentClientSecret(amount)
            _paymentIntentClientSecret.value = secret
            _isLoading.value = false
        }
    }

    fun onCheckoutLaunched() {
        _paymentIntentClientSecret.value = null
    }

    fun onPaymentResult(success: Boolean) {
        _paymentResult.value = success
    }

    fun resetPaymentResult() {
        _paymentResult.value = null
    }
}
