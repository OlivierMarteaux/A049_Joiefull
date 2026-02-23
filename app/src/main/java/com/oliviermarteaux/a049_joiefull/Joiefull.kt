package com.oliviermarteaux.a049_joiefull

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.oliviermarteaux.a049_joiefull.ui.navigation.JoiefullNavHost
import com.oliviermarteaux.a049_joiefull.ui.screens.home.HomeScreen
import com.oliviermarteaux.a049_joiefull.ui.screens.home.HomeViewModel
import com.oliviermarteaux.a049_joiefull.ui.screens.item.PaymentUiState
import com.oliviermarteaux.shared.composables.startup.DismissKeyboardOnTapOutside
import com.oliviermarteaux.shared.utils.SharedContentType
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun Joiefull(
    navController: NavHostController = rememberNavController(),
    windowSize: WindowWidthSizeClass,
    payUiState: PaymentUiState = PaymentUiState.NotStarted,
    onGooglePayButtonClick: () -> Unit,
) {

    val contentType: SharedContentType = when (windowSize){
        WindowWidthSizeClass.Compact -> { SharedContentType.LIST_ONLY }
        WindowWidthSizeClass.Medium -> { SharedContentType.LIST_AND_DETAIL }
        WindowWidthSizeClass.Expanded -> { SharedContentType.LIST_AND_DETAIL }
        else -> { SharedContentType.LIST_AND_DETAIL }
    }

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        DismissKeyboardOnTapOutside {
            val homeViewModel: HomeViewModel = koinViewModel()
            if (contentType == SharedContentType.LIST_ONLY) {
                JoiefullNavHost(
                    navController = navController,
                    contentType = contentType,
                    homeViewModel = homeViewModel,
                    innerPadding = innerPadding,
                    payUiState = payUiState,
                    onGooglePayButtonClick = onGooglePayButtonClick,
                )
            } else {
                HomeScreen(
                    viewModel = homeViewModel,
                    contentType = contentType,
                    navigateToItem = {},
                    modifier = Modifier.padding(innerPadding),
                    payUiState = payUiState,
                    onGooglePayButtonClick = onGooglePayButtonClick
                )
            }
        }
    }
}