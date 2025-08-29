package com.oliviermarteaux.a049_joiefull

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.oliviermarteaux.a049_joiefull.ui.navigation.JoiefullNavHost
import com.oliviermarteaux.shared.composables.startup.DismissKeyboardOnTapOutside

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Joiefull(
    navController: NavHostController = rememberNavController()
) {
    DismissKeyboardOnTapOutside { JoiefullNavHost(navController = navController) }
}