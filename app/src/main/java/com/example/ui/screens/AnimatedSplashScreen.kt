package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.R
import com.example.ui.components.Rotating3DSNCubeEmblem
import com.example.ui.components.neonStudioBackground
import com.example.ui.theme.LocalStudioColors
import kotlinx.coroutines.delay

@Composable
fun AnimatedSplashScreen(
    onSplashCompleted: () -> Unit
) {
    val studioColors = LocalStudioColors.current
    var showText by remember { mutableStateOf(false) }
    var progressTarget by remember { mutableStateOf(0.08f) }
    var hasNavigated by remember { mutableStateOf(false) }

    val animatedProgress by animateFloatAsState(
        targetValue = progressTarget,
        animationSpec = tween(durationMillis = 2000, easing = FastOutSlowInEasing),
        label = "splash_loader"
    )

    LaunchedEffect(Unit) {
        delay(120)
        showText = true
        progressTarget = 1f
        delay(2250)
        if (!hasNavigated) {
            hasNavigated = true
            onSplashCompleted()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .neonStudioBackground()
            .windowInsetsPadding(WindowInsets.safeDrawing)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                if (!hasNavigated) {
                    hasNavigated = true
                    onSplashCompleted()
                }
            }
            .testTag("splash_screen_root"),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .widthIn(max = 460.dp)
                .padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Rotating3DSNCubeEmblem(
                emblemSize = 204.dp,
                modifier = Modifier.testTag("splash_3d_rotating_logo")
            )

            Spacer(modifier = Modifier.height(28.dp))

            AnimatedVisibility(
                visible = showText,
                enter = fadeIn(tween(550)) + slideInVertically(
                    animationSpec = tween(550, easing = FastOutSlowInEasing),
                    initialOffsetY = { 28 }
                )
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = stringResource(R.string.app_name),
                        style = MaterialTheme.typography.displayMedium,
                        color = MaterialTheme.colorScheme.onBackground,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = stringResource(R.string.splash_welcome),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.testTag("splash_welcome_text")
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = stringResource(R.string.splash_subtitle),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(28.dp))

                    LinearProgressIndicator(
                        progress = { animatedProgress },
                        color = MaterialTheme.colorScheme.primary,
                        trackColor = studioColors.cardBorder.copy(alpha = 0.35f),
                        strokeCap = StrokeCap.Round,
                        modifier = Modifier
                            .widthIn(max = 190.dp)
                            .fillMaxWidth(0.58f)
                            .height(6.dp)
                            .clip(RoundedCornerShape(50))
                            .background(
                                Brush.horizontalGradient(
                                    listOf(
                                        studioColors.accentGradientStart.copy(alpha = 0.2f),
                                        studioColors.accentGradientEnd.copy(alpha = 0.2f)
                                    )
                                )
                            )
                    )
                }
            }
        }
    }
}
