package com.novahypnose.ancrage.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.novahypnose.ancrage.data.database.AncrageDatabase
import com.novahypnose.ancrage.data.repository.AnchorRepository
import com.novahypnose.ancrage.data.repository.SettingsRepository
import com.novahypnose.ancrage.ui.screens.creation.CreateAnchorScreen
import com.novahypnose.ancrage.ui.screens.creation.CreationViewModel
import com.novahypnose.ancrage.ui.screens.faq.FAQScreen
import com.novahypnose.ancrage.ui.screens.home.HomeScreen
import com.novahypnose.ancrage.ui.screens.home.HomeViewModel
import com.novahypnose.ancrage.ui.screens.reactivation.ReactivationScreen
import com.novahypnose.ancrage.ui.screens.reactivation.ReactivationViewModel
import com.novahypnose.ancrage.ui.screens.settings.SettingsScreen
import com.novahypnose.ancrage.ui.screens.settings.SettingsViewModel

/**
 * Routes de navigation
 */
sealed class Screen(val route: String) {
    object Home : Screen("home")
    object CreateAnchor : Screen("create_anchor")
    object Reactivation : Screen("reactivation/{anchorId}") {
        fun createRoute(anchorId: Long) = "reactivation/$anchorId"
    }
    object Settings : Screen("settings")
    object FAQ : Screen("faq")
}

/**
 * Graphe de navigation principal
 */
@Composable
fun AppNavigation(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    // Initialiser les repositories
    val database = AncrageDatabase.getInstance(context)
    val anchorRepository = AnchorRepository(
        anchorDao = database.anchorDao(),
        reactivationDao = database.reactivationDao()
    )
    val settingsRepository = SettingsRepository(context)

    NavHost(
        navController = navController,
        startDestination = Screen.Home.route,
        modifier = modifier
    ) {
        // Écran d'accueil
        composable(Screen.Home.route) {
            val viewModel: HomeViewModel = viewModel(
                factory = HomeViewModelFactory(anchorRepository)
            )

            HomeScreen(
                viewModel = viewModel,
                onCreateAnchor = {
                    navController.navigate(Screen.CreateAnchor.route)
                },
                onAnchorClick = { anchorId ->
                    navController.navigate(Screen.Reactivation.createRoute(anchorId))
                },
                onSettingsClick = {
                    navController.navigate(Screen.Settings.route)
                },
                onFAQClick = {
                    navController.navigate(Screen.FAQ.route)
                }
            )
        }

        // Écran de création d'ancrage
        composable(Screen.CreateAnchor.route) {
            val viewModel: CreationViewModel = viewModel(
                factory = CreationViewModelFactory(anchorRepository)
            )

            CreateAnchorScreen(
                viewModel = viewModel,
                onNavigateBack = { navController.popBackStack() },
                onAnchorCreated = { anchorId ->
                    navController.popBackStack()
                    // Optionnel : naviguer directement vers la réactivation
                    // navController.navigate(Screen.Reactivation.createRoute(anchorId))
                }
            )
        }

        // Écran de réactivation
        composable(
            route = Screen.Reactivation.route,
            arguments = listOf(
                navArgument("anchorId") { type = NavType.LongType }
            )
        ) { backStackEntry ->
            val anchorId = backStackEntry.arguments?.getLong("anchorId") ?: return@composable

            val viewModel: ReactivationViewModel = viewModel(
                factory = ReactivationViewModelFactory(anchorRepository, anchorId)
            )

            ReactivationScreen(
                viewModel = viewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        // Écran de paramètres
        composable(Screen.Settings.route) {
            val viewModel: SettingsViewModel = viewModel(
                factory = SettingsViewModelFactory(settingsRepository)
            )

            SettingsScreen(
                viewModel = viewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        // Écran FAQ
        composable(Screen.FAQ.route) {
            FAQScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}

// ViewModelFactories pour l'injection de dépendances
class HomeViewModelFactory(
    private val anchorRepository: AnchorRepository
) : androidx.lifecycle.ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
        return HomeViewModel(anchorRepository) as T
    }
}

class CreationViewModelFactory(
    private val anchorRepository: AnchorRepository
) : androidx.lifecycle.ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
        return CreationViewModel(anchorRepository) as T
    }
}

class ReactivationViewModelFactory(
    private val anchorRepository: AnchorRepository,
    private val anchorId: Long
) : androidx.lifecycle.ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
        return ReactivationViewModel(anchorRepository, anchorId) as T
    }
}

class SettingsViewModelFactory(
    private val settingsRepository: SettingsRepository
) : androidx.lifecycle.ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
        return SettingsViewModel(settingsRepository) as T
    }
}
