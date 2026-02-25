package com.oliviermarteaux.a049_joiefull

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.material3.Surface
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.gms.wallet.contract.TaskResultContracts
import com.oliviermarteaux.a049_joiefull.ui.screens.item.GPayCheckoutViewModel
import com.oliviermarteaux.a049_joiefull.ui.screens.item.PaymentUiState
import com.oliviermarteaux.a049_joiefull.ui.theme.A049_JoiefullTheme
import kotlin.getValue

class MainActivity : ComponentActivity() {

    private val model: GPayCheckoutViewModel by viewModels()
    private val paymentDataLauncher = registerForActivityResult(TaskResultContracts.GetPaymentDataResult()) { taskResult ->
        when (taskResult.status.statusCode) {
            CommonStatusCodes.SUCCESS -> {
                taskResult.result?.let {
                    Log.i("Google Pay result:", it.toJson())
                    model.setPaymentData(it)
                }
            }
            CommonStatusCodes.CANCELED -> {
                Log.w("Google Pay", "Payment was canceled by user")
                model.handleError(CommonStatusCodes.CANCELED, "Canceled by user")
            }
            CommonStatusCodes.ERROR -> {
                model.handleError(CommonStatusCodes.ERROR, taskResult.status.statusMessage)
            }
            CommonStatusCodes.INTERNAL_ERROR -> {
                model.handleError(CommonStatusCodes.INTERNAL_ERROR, "Internal Error")
            }
            else -> {
                model.handleError(taskResult.status.statusCode, "Unknown error")
            }
        }
    }

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        // Install splash screen
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            A049_JoiefullTheme {
                // info: layout config for Adaptive layout
                val layoutDirection = LocalLayoutDirection.current
                val payState: PaymentUiState by model.paymentUiState.collectAsStateWithLifecycle()
                Surface(
                    modifier = Modifier.padding(
                        start = WindowInsets.safeDrawing.asPaddingValues()
                            .calculateStartPadding(layoutDirection),
                        end = WindowInsets.safeDrawing.asPaddingValues()
                            .calculateEndPadding(layoutDirection),
                    )
                ) {
                    val windowSize = calculateWindowSizeClass(this)
                    Joiefull(
                        windowSize = windowSize.widthSizeClass,
                        payUiState = payState,
                        onGooglePayButtonClick = this::requestPayment,
                    )
                }
            }
        }
    }

    private fun requestPayment(price: Double) {
        val priceCents = (price * 100).toLong()
        val task = model.getLoadPaymentDataTask(priceCents = priceCents)
        task.addOnCompleteListener(paymentDataLauncher::launch)
    }

}