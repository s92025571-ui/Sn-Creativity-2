package com.example.ui.screens

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.R
import com.example.domain.StudioSection
import com.example.ui.components.Interactive3DActionIconButton
import com.example.ui.components.Interactive3DIconBadge
import com.example.ui.components.StudioTopAppBar
import com.example.ui.components.neonStudioBackground
import com.example.ui.components.toGlyph
import com.example.ui.components.toIconPalette
import com.example.ui.theme.LocalStudioColors

@Composable
fun DashboardScreen(
    onOpenSection: (StudioSection) -> Unit,
    onOpenSettings: () -> Unit
) {
    val sections = StudioSection.entries

    Scaffold(
        contentWindowInsets = WindowInsets.navigationBars,
        containerColor = Color.Transparent,
        topBar = {
            StudioTopAppBar(
                title = stringResource(R.string.dashboard_title),
                subtitle = "Native Creator Suite",
                trailingAction = {
                    Interactive3DActionIconButton(
                        icon = Icons.Filled.Settings,
                        contentDescription = stringResource(R.string.action_settings),
                        onClick = onOpenSettings,
                        testTag = "settings_button"
                    )
                }
            )
        }
    ) { innerPadding ->
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .neonStudioBackground()
                .padding(innerPadding)
                .testTag("dashboard_screen_root"),
            contentAlignment = Alignment.TopCenter
        ) {
            val isWideScreen = maxWidth >= 640.dp

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .widthIn(max = 880.dp),
                contentPadding = PaddingValues(horizontal = 20.dp, vertical = 14.dp),
                verticalArrangement = Arrangement.spacedBy(18.dp)
            ) {
                item {
                    DashboardHeroBanner()
                }

                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = stringResource(R.string.dashboard_sections_heading),
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        Surface(
                            color = LocalStudioColors.current.badgeSurface,
                            shape = RoundedCornerShape(50)
                        ) {
                            Text(
                                text = "4 STUDIOS",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 5.dp)
                            )
                        }
                    }
                }

                if (isWideScreen) {
                    // Responsive 2x2 grid rows for tablets / unfolded foldables / landscape
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            StudioSectionCard(
                                section = sections[0],
                                onClick = { onOpenSection(sections[0]) },
                                modifier = Modifier.weight(1f)
                            )
                            StudioSectionCard(
                                section = sections[1],
                                onClick = { onOpenSection(sections[1]) },
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            StudioSectionCard(
                                section = sections[2],
                                onClick = { onOpenSection(sections[2]) },
                                modifier = Modifier.weight(1f)
                            )
                            StudioSectionCard(
                                section = sections[3],
                                onClick = { onOpenSection(sections[3]) },
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                } else {
                    items(sections.size, key = { sections[it].id }) { index ->
                        val section = sections[index]
                        StudioSectionCard(
                            section = section,
                            onClick = { onOpenSection(section) },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
        }
    }
}

@Composable
private fun DashboardHeroBanner() {
    val studioColors = LocalStudioColors.current

    Card(
        shape = RoundedCornerShape(26.dp),
        border = BorderStroke(1.dp, studioColors.cardBorder),
        elevation = CardDefaults.cardElevation(defaultElevation = if (studioColors.isDark) 8.dp else 3.dp),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("dashboard_hero_card")
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(196.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.img_hero_studio_banner_1792939784598),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.horizontalGradient(
                            colors = listOf(
                                Color(0xEA0B0617),
                                Color(0xCC160B2E),
                                Color(0x771E0E3D)
                            )
                        )
                    )
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(22.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Surface(
                    color = Color(0x4DC06CFF),
                    border = BorderStroke(1.dp, Color(0x99E5B8FF)),
                    shape = RoundedCornerShape(50)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 5.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Bolt,
                            contentDescription = null,
                            tint = Color(0xFFF3D1FF),
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(5.dp))
                        Text(
                            text = stringResource(R.string.dashboard_hero_badge),
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.White
                        )
                    }
                }

                Column {
                    Text(
                        text = stringResource(R.string.dashboard_hero_headline),
                        style = MaterialTheme.typography.headlineMedium,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = stringResource(R.string.dashboard_hero_subtext),
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFFE0D4FA)
                    )
                }
            }
        }
    }
}

@Composable
private fun StudioSectionCard(
    section: StudioSection,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val studioColors = LocalStudioColors.current
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val density = LocalDensity.current.density

    val cardScale by animateFloatAsState(
        targetValue = if (isPressed) 0.965f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "section_card_scale"
    )

    val cardTiltX by animateFloatAsState(
        targetValue = if (isPressed) 6f else 0f,
        animationSpec = spring(stiffness = Spring.StiffnessLow),
        label = "section_card_tilt"
    )

    Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = studioColors.elevatedCardSurface),
        border = BorderStroke(
            width = if (isPressed) 1.6.dp else 1.dp,
            color = if (isPressed) MaterialTheme.colorScheme.primary else studioColors.cardBorder
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (studioColors.isDark) 8.dp else 2.dp
        ),
        modifier = modifier
            .graphicsLayer {
                scaleX = cardScale
                scaleY = cardScale
                rotationX = cardTiltX
                cameraDistance = 16f * density
            }
            .clip(RoundedCornerShape(24.dp))
            .clickable(
                interactionSource = interactionSource,
                indication = androidx.compose.material3.ripple(),
                onClick = onClick
            )
            .testTag("section_card_${section.id}")
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Interactive3DIconBadge(
                icon = section.toGlyph(),
                contentDescription = section.title,
                palette = section.toIconPalette(),
                size = 64.dp,
                onClick = onClick,
                testTag = "section_3d_icon_${section.id}"
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Surface(
                    color = studioColors.badgeSurface,
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = section.badge,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = section.title,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = section.subtitle,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.width(10.dp))

            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}
