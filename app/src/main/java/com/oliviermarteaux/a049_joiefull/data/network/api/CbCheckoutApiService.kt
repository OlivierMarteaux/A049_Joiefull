package com.oliviermarteaux.a049_joiefull.data.network.api

import com.oliviermarteaux.secrets.STRIPE_SECRET_KEY
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.forms.submitForm
import io.ktor.client.request.header
import io.ktor.http.Parameters
import io.ktor.http.isSuccess
import kotlinx.serialization.Serializable

interface CbCheckoutApiService {
    suspend fun fetchPaymentIntentClientSecret(amount: Double): String?
}

@Serializable
data class StripePaymentIntentResponse(
    val client_secret: String
)

class KtorCbCheckoutApiService(private val client: HttpClient) : CbCheckoutApiService {

    private val secretKey = STRIPE_SECRET_KEY

    override suspend fun fetchPaymentIntentClientSecret(amount: Double): String? {
        return try {
            val amountInCents = (amount * 100).toInt().toString()
            val piResponse = client.submitForm(
                url = "https://api.stripe.com/v1/payment_intents",
                formParameters = Parameters.build {
                    append("amount", amountInCents)
                    append("currency", "usd")
                    // automatic_payment_methods is required by newer PaymentSheet APIs
                    append("automatic_payment_methods[enabled]", "true")
                }
            ) {
                header("Authorization", "Bearer $secretKey")
            }

            if (piResponse.status.isSuccess()) {
                piResponse.body<StripePaymentIntentResponse>().client_secret
            } else {
                null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}