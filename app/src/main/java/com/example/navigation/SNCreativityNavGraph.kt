package com.example.navigation

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.domain.StudioSection
import com.example.ui.screens.AnimatedSplashScreen
import com.example.ui.screens.CreatorToolkitScreen
import com.example.ui.screens.DashboardScreen
import com.example.ui.screens.GamingStudioScreen
import com.example.ui.screens.SettingsScreen
import com.example.ui.screens.ThumbnailPromptStudioScreen
import com.example.ui.screens.VoiceScriptStudioScreen
import com.example.viewmodel.StudioViewModel

object AppRoutes {
    const val SPLASH = "splash"
    const val DASHBOARD = "dashboard"
    const val GAMING_STUDIO = "gaming_studio"
    const val VOICE_SCRIPT_STUDIO = "voice_script_studio"
    const val THUMBNAIL_STUDIO = "thumbnail_prompt_studio"
    const val CREATOR_TOOLKIT = "creator_toolkit"
    const val SETTINGS = "settings"
}

@Composable
fun SNCreativityNavGraph(
    viewModel: StudioViewModel,
    navController: NavHostController = rememberNavController()
) {
    val themeMode by viewModel.themeMode.collectAsStateWithLifecycle()
    val gamingState by viewModel.gamingState.collectAsStateWithLifecycle()
    val voiceState by viewModel.voiceState.collectAsStateWithLifecycle()
    val thumbnailState by viewModel.thumbnailState.collectAsStateWithLifecycle()
    val toolkitState by viewModel.toolkitState.collectAsStateWithLifecycle()

    NavHost(
        navController = navController,
        startDestination = AppRoutes.SPLASH,
        enterTransition = {
            fadeIn(tween(260)) + slideInHorizontally(
                animationSpec = tween(260, easing = FastOutSlowInEasing),
                initialOffsetX = { it / 8 }
            )
        },
        exitTransition = {
            fadeOut(tween(200))
        },
        popEnterTransition = {
            fadeIn(tween(240))
        },
        popExitTransition = {
            fadeOut(tween(220)) + slideOutHorizontally(
                animationSpec = tween(220, easing = FastOutSlowInEasing),
                targetOffsetX = { it / 8 }
            )
        }
    ) {
        composable(
            route = AppRoutes.SPLASH,
            exitTransition = { fadeOut(tween(380)) }
        ) {
            AnimatedSplashScreen(
                onSplashCompleted = {
                    navController.navigate(AppRoutes.DASHBOARD) {
                        popUpTo(AppRoutes.SPLASH) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }

        composable(
            route = AppRoutes.DASHBOARD,
            enterTransition = {
                fadeIn(tween(380)) + scaleIn(
                    initialScale = 0.96f,
                    animationSpec = tween(380, easing = FastOutSlowInEasing)
                )
            }
        ) {
            DashboardScreen(
                onOpenSection = { section ->
                    val route = when (section) {
                        StudioSection.GAMING_STUDIO -> AppRoutes.GAMING_STUDIO
                        StudioSection.VOICE_SCRIPT_STUDIO -> AppRoutes.VOICE_SCRIPT_STUDIO
                        StudioSection.THUMBNAIL_PROMPT_STUDIO -> AppRoutes.THUMBNAIL_STUDIO
                        StudioSection.CREATOR_TOOLKIT -> AppRoutes.CREATOR_TOOLKIT
                    }
                    navController.navigate(route) {
                        launchSingleTop = true
                    }
                },
                onOpenSettings = {
                    navController.navigate(AppRoutes.SETTINGS) {
                        launchSingleTop = true
                    }
                }
            )
        }

        composable(route = AppRoutes.GAMING_STUDIO) {
            GamingStudioScreen(
                state = gamingState,
                onSelectTool = viewModel::selectGamingTool,
                onTopicChange = viewModel::updateGamingTopic,
                onStyleChange = viewModel::updateGamingStyle,
                onGenerate = viewModel::generateGamingResults,
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(route = AppRoutes.VOICE_SCRIPT_STUDIO) {
            VoiceScriptStudioScreen(
                state = voiceState,
                onSelectTool = viewModel::selectVoiceTool,
                onTopicChange = viewModel::updateVoiceTopic,
                onDurationChange = viewModel::updateScriptDuration,
                onToneChange = viewModel::updateScriptTone,
                onGenerate = viewModel::generateVoiceResults,
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(route = AppRoutes.THUMBNAIL_STUDIO) {
            ThumbnailPromptStudioScreen(
                state = thumbnailState,
                onCategoryChange = viewModel::updateThumbnailCategory,
                onAspectRatioChange = viewModel::updateThumbnailAspectRatio,
                onTopicChange = viewModel::updateThumbnailTopic,
                onStyleChange = viewModel::updateThumbnailStyle,
                onLightingChange = viewModel::updateThumbnailLighting,
                onCompositionChange = viewModel::updateThumbnailComposition,
                onTextInstructionChange = viewModel::updateThumbnailTextInstruction,
                onGenerate = viewModel::generateThumbnailResults,
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(route = AppRoutes.CREATOR_TOOLKIT) {
            CreatorToolkitScreen(
                state = toolkitState,
                onSelectTool = viewModel::selectToolkitTool,
                onTopicChange = viewModel::updateToolkitTopic,
                onPlatformChange = viewModel::updateToolkitPlatform,
                onGenerate = viewModel::generateToolkitResults,
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(route = AppRoutes.SETTINGS) {
            SettingsScreen(
                selectedTheme = themeMode,
                onSelectTheme = viewModel::setThemeMode,
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}
