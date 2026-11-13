package com.example.foodverse.presentation.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.foodverse.presentation.auth.AuthScreen
import com.example.foodverse.presentation.discovery.DiscoveryScreen
import com.example.foodverse.presentation.discovery.DiscoveryViewModel
import com.example.foodverse.presentation.discovery.DonationDetailScreen
import com.example.foodverse.presentation.donation.AiRecognitionScreen
import com.example.foodverse.presentation.donation.DonationFormScreen
import com.example.foodverse.presentation.donation.DonationViewModel
import com.example.foodverse.presentation.home.HomeScreen
import com.example.foodverse.presentation.mydonations.MyDonationsScreen
import com.example.foodverse.presentation.onboarding.OnboardingScreen
import com.example.foodverse.presentation.profile.ProfileScreen
import com.example.foodverse.presentation.splash.SplashScreen

@Composable
fun FoodVerseNavGraph(
    navController: NavHostController = rememberNavController(),
    donationViewModel: DonationViewModel = viewModel(),
    discoveryViewModel: DiscoveryViewModel = viewModel()
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val bottomBarRoutes = listOf(
        Screen.Home.route,
        Screen.Discovery.route,
        Screen.MyDonations.route,
        Screen.Profile.route
    )
    val shouldShowBottomBar = currentRoute in bottomBarRoutes

    Scaffold(
        bottomBar = {
            if (shouldShowBottomBar) {
                FoodVerseBottomBar(
                    currentRoute = currentRoute,
                    onNavigate = { route ->
                        navController.navigate(route) {
                            popUpTo(Screen.Home.route) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    onDonateClick = {
                        navController.navigate(Screen.AiRecognition.route)
                    }
                )
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Splash.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            // Splash
            composable(Screen.Splash.route) {
                SplashScreen(
                    onSplashFinished = {
                        navController.navigate(Screen.Onboarding.route) {
                            popUpTo(Screen.Splash.route) { inclusive = true }
                        }
                    }
                )
            }

            // Onboarding
            composable(Screen.Onboarding.route) {
                OnboardingScreen(
                    onFinish = {
                        navController.navigate(Screen.Auth.route) {
                            popUpTo(Screen.Onboarding.route) { inclusive = true }
                        }
                    }
                )
            }

            // Auth
            composable(Screen.Auth.route) {
                AuthScreen(
                    onAuthSuccess = {
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.Auth.route) { inclusive = true }
                        }
                    }
                )
            }

            // Home
            composable(Screen.Home.route) {
                HomeScreen(
                    onNavigateToAiDonate = {
                        navController.navigate(Screen.AiRecognition.route)
                    },
                    onNavigateToDiscovery = { categoryName ->
                        if (categoryName != null) {
                            try {
                                val cat = com.example.foodverse.data.model.FoodCategory.valueOf(categoryName)
                                discoveryViewModel.onCategorySelected(cat)
                            } catch (_: Exception) {}
                        }
                        navController.navigate(Screen.Discovery.route)
                    },
                    onDonationClick = { donationId ->
                        navController.navigate(Screen.DonationDetail.createRoute(donationId))
                    },
                    onProfileClick = {
                        navController.navigate(Screen.Profile.route)
                    }
                )
            }

            // Feature 1: AI Recognition Screen
            composable(Screen.AiRecognition.route) {
                AiRecognitionScreen(
                    onNavigateBack = { navController.popBackStack() },
                    onContinueToForm = { imageUri, foodName, category, description, servings, freshness ->
                        donationViewModel.populateFromNavigationArgs(
                            imageUri, foodName, category, description, servings, freshness
                        )
                        navController.navigate(
                            Screen.DonationForm.createRoute(
                                imageUri, foodName, category, description, servings, freshness
                            )
                        )
                    },
                    viewModel = donationViewModel
                )
            }

            // Feature 1: Donation Form Screen
            composable(
                route = Screen.DonationForm.route,
                arguments = listOf(
                    navArgument("imageUri") { type = NavType.StringType; defaultValue = "" },
                    navArgument("foodName") { type = NavType.StringType; defaultValue = "" },
                    navArgument("category") { type = NavType.StringType; defaultValue = "" },
                    navArgument("description") { type = NavType.StringType; defaultValue = "" },
                    navArgument("servings") { type = NavType.StringType; defaultValue = "" },
                    navArgument("freshness") { type = NavType.StringType; defaultValue = "" }
                )
            ) { backStackEntry ->
                val imageUri = backStackEntry.arguments?.getString("imageUri") ?: ""
                val foodName = backStackEntry.arguments?.getString("foodName") ?: ""
                val category = backStackEntry.arguments?.getString("category") ?: ""
                val description = backStackEntry.arguments?.getString("description") ?: ""
                val servings = backStackEntry.arguments?.getString("servings") ?: ""
                val freshness = backStackEntry.arguments?.getString("freshness") ?: ""

                donationViewModel.populateFromNavigationArgs(
                    imageUri, foodName, category, description, servings, freshness
                )

                DonationFormScreen(
                    onNavigateBack = { navController.popBackStack() },
                    onPublishSuccess = {
                        navController.navigate(Screen.MyDonations.route) {
                            popUpTo(Screen.Home.route)
                        }
                    },
                    viewModel = donationViewModel
                )
            }

            // Feature 2: Discovery Screen
            composable(Screen.Discovery.route) {
                DiscoveryScreen(
                    onDonationClick = { donationId ->
                        navController.navigate(Screen.DonationDetail.createRoute(donationId))
                    },
                    viewModel = discoveryViewModel
                )
            }

            // Feature 2: Donation Detail Screen
            composable(
                route = Screen.DonationDetail.route,
                arguments = listOf(
                    navArgument("donationId") { type = NavType.StringType }
                )
            ) { backStackEntry ->
                val donationId = backStackEntry.arguments?.getString("donationId") ?: ""
                DonationDetailScreen(
                    donationId = donationId,
                    onNavigateBack = { navController.popBackStack() },
                    viewModel = discoveryViewModel
                )
            }

            // My Donations
            composable(Screen.MyDonations.route) {
                MyDonationsScreen(
                    onNavigateToDonate = {
                        navController.navigate(Screen.AiRecognition.route)
                    },
                    onDonationClick = { donationId ->
                        navController.navigate(Screen.DonationDetail.createRoute(donationId))
                    }
                )
            }

            // User Profile
            composable(Screen.Profile.route) {
                ProfileScreen(
                    onSignOut = {
                        navController.navigate(Screen.Auth.route) {
                            popUpTo(Screen.Home.route) { inclusive = true }
                        }
                    }
                )
            }
        }
    }
}
