package com.oliviermarteaux.a049_joiefull.ui.screens.item

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oliviermarteaux.a049_joiefull.data.repository.CbCheckoutRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CbCheckoutViewModel(
    private val repository: CbCheckoutRepository
) : ViewModel() {

    private val _paymentIntentClientSecret = MutableStateFlow<String?>(null)
    val paymentIntentClientSecret: StateFlow<String?> = _paymentIntentClientSecret.asStateFlow()

    private val _paymentResult = MutableStateFlow<Boolean?>(null)
    val paymentResult: StateFlow<Boolean?> = _paymentResult.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

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
