package com.oliviermarteaux.a049_joiefull.ui.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.oliviermarteaux.a049_joiefull.ui.screens.home.HomeDestination
import com.oliviermarteaux.a049_joiefull.ui.screens.home.HomeScreen
import com.oliviermarteaux.a049_joiefull.ui.screens.home.HomeViewModel
import com.oliviermarteaux.a049_joiefull.ui.screens.item.ItemDestination
import com.oliviermarteaux.a049_joiefull.ui.screens.item.ItemScreen
import com.oliviermarteaux.shared.utils.SharedContentType
import com.oliviermarteaux.shared.utils.debugLog
import org.koin.compose.viewmodel.koinViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun JoiefullNavHost(
    navController: NavHostController,
    contentType: SharedContentType,
    homeViewModel: HomeViewModel,
    modifier: Modifier = Modifier,
    innerPadding: PaddingValues
) {
    NavHost(
        navController = navController,
        startDestination = HomeDestination.route,
        modifier = modifier
    ) {

        // Home Screen
        composable(route = HomeDestination.route) {
//            val homeViewModel: HomeViewModel = koinViewModel()
            HomeScreen(
                viewModel = homeViewModel,
                contentType = contentType,
                modifier = Modifier.padding(innerPadding),
                navigateToItem = {
                    if (contentType == SharedContentType.LIST_ONLY) {
                        navController.navigate("${ItemDestination.route}/${it}")
                        debugLog("NavHost: HomeScreen: Navigating to ${ItemDestination.route}/$it")
                    } /*else {homeViewModel.selectItem(it)}*/
                }
            )
        }

        // Item Detail Screen
        composable(
            route = ItemDestination.routeWithArgs,
            arguments = listOf(navArgument(ItemDestination.ITEM_ID) {
                type = NavType.IntType
            })
        ) { backStackEntry ->
            val itemId = backStackEntry.arguments?.getInt("itemId")!!
            ItemScreen(
                itemId = itemId,
                modifier = Modifier.padding(innerPadding),
                navigateBack =
                    // fixed: replace popbackstack by popupto to avoid remanent ghost buttons from
                    //  previous screen
//                    {
//                        navController.popBackStack()
//                        debugLog("NavHost: DetailScreen: Navigating back")
//                    },
                    {
                        navController.navigate(HomeDestination.route) {
                            popUpTo(ItemDestination.route) { inclusive = true }
                        }
                    }
            )
        }
    }
}