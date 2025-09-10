package com.oliviermarteaux.a049_joiefull

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.oliviermarteaux.a049_joiefull.ui.navigation.JoiefullNavHost
import com.oliviermarteaux.a049_joiefull.ui.screens.home.HomeScreen
import com.oliviermarteaux.a049_joiefull.ui.screens.home.HomeViewModel
import com.oliviermarteaux.a049_joiefull.ui.screens.item.ItemDestination
import com.oliviermarteaux.shared.composables.startup.DismissKeyboardOnTapOutside
import com.oliviermarteaux.shared.utils.SharedContentType
import com.oliviermarteaux.shared.utils.debugLog
import org.koin.compose.viewmodel.koinViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Joiefull(
    navController: NavHostController = rememberNavController(),
    windowSize: WindowWidthSizeClass,
) {
    val contentType: SharedContentType

    when (windowSize){
        WindowWidthSizeClass.Compact -> {
            contentType = SharedContentType.LIST_ONLY
        }
        WindowWidthSizeClass.Medium -> {
            contentType = SharedContentType.LIST_AND_DETAIL
        }
        WindowWidthSizeClass.Expanded -> {
            contentType = SharedContentType.LIST_AND_DETAIL
        }
        else -> {
            contentType = SharedContentType.LIST_AND_DETAIL
        }
    }
    DismissKeyboardOnTapOutside {
        val homeViewModel: HomeViewModel = koinViewModel()
        if (contentType == SharedContentType.LIST_ONLY) {
            JoiefullNavHost(
                navController = navController,
                contentType = contentType,
                homeViewModel = homeViewModel,
            )
        } else {
            HomeScreen(
                viewModel = homeViewModel,
                contentType = contentType,
                navigateToItem = {}
            )
        }
    }
}