package com.oliviermarteaux.a049_joiefull.data.repository

import com.oliviermarteaux.a049_joiefull.data.network.api.CbCheckoutApiService

class CbCheckoutRepository(
    private val cbCheckoutApiService: CbCheckoutApiService
) {
    suspend fun getPaymentIntentClientSecret(amount: Double): String? {
        return cbCheckoutApiService.fetchPaymentIntentClientSecret(amount)
    }
}
