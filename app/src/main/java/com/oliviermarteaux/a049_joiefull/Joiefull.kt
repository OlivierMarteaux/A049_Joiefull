package com.oliviermarteaux.a049_joiefull

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.oliviermarteaux.a049_joiefull.ui.navigation.JoiefullNavHost
import com.oliviermarteaux.shared.composables.startup.DismissKeyboardOnTapOutside
import com.oliviermarteaux.shared.utils.SharedContentType

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
        JoiefullNavHost(
            navController = navController,
            contentType = contentType
        )
    }
}