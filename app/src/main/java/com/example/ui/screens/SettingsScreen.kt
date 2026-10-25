package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.OfflineBolt
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.R
import com.example.data.AppThemeMode
import com.example.ui.components.Icon3DPalette
import com.example.ui.components.Interactive3DIconBadge
import com.example.ui.components.StudioTopAppBar
import com.example.ui.components.neonStudioBackground
import com.example.ui.theme.LocalStudioColors

@Composable
fun SettingsScreen(
    selectedTheme: AppThemeMode,
    onSelectTheme: (AppThemeMode) -> Unit,
    onBackClick: () -> Unit
) {
    val studioColors = LocalStudioColors.current

    Scaffold(
        contentWindowInsets = WindowInsets.navigationBars,
        containerColor = Color.Transparent,
        topBar = {
            StudioTopAppBar(
                title = stringResource(R.string.settings_title),
                subtitle = "Appearance & Local Studio Preferences",
                onBackClick = onBackClick
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .neonStudioBackground()
                .padding(innerPadding)
                .testTag("settings_screen_root"),
            contentAlignment = Alignment.TopCenter
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .widthIn(max = 680.dp),
                contentPadding = PaddingValues(horizontal = 20.dp, vertical = 14.dp),
                verticalArrangement = Arrangement.spacedBy(18.dp)
            ) {
                item {
                    Text(
                        text = stringResource(R.string.settings_theme_section),
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }

                item {
                    ThemeOptionCard(
                        title = stringResource(R.string.settings_theme_neon_dark),
                        description = stringResource(R.string.settings_theme_neon_dark_desc),
                        icon = Icons.Filled.DarkMode,
                        palette = Icon3DPalette.GAMING,
                        previewColors = listOf(
                            Color(0xFF07050E),
                            Color(0xFF1B142E),
                            Color(0xFFC06CFF),
                            Color(0xFFFF59C7)
                        ),
                        isSelected = selectedTheme == AppThemeMode.NEON_DARK,
                        onSelect = { onSelectTheme(AppThemeMode.NEON_DARK) },
                        testTag = "theme_option_neon_dark"
                    )
                }

                item {
                    ThemeOptionCard(
                        title = stringResource(R.string.settings_theme_creative_light),
                        description = stringResource(R.string.settings_theme_creative_light_desc),
                        icon = Icons.Filled.LightMode,
                        palette = Icon3DPalette.TOOLKIT,
                        previewColors = listOf(
                            Color(0xFFF8F6FC),
                            Color(0xFFFFFFFF),
                            Color(0xFF6B2FD9),
                            Color(0xFFEADDFF)
                        ),
                        isSelected = selectedTheme == AppThemeMode.CREATIVE_LIGHT,
                        onSelect = { onSelectTheme(AppThemeMode.CREATIVE_LIGHT) },
                        testTag = "theme_option_creative_light"
                    )
                }

                item {
                    Card(
                        shape = RoundedCornerShape(24.dp),
                        colors = CardDefaults.cardColors(containerColor = studioColors.elevatedCardSurface),
                        border = BorderStroke(1.dp, studioColors.cardBorder),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(20.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Surface(
                                color = studioColors.badgeSurface,
                                shape = RoundedCornerShape(50)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.OfflineBolt,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = stringResource(R.string.settings_offline_badge),
                                        style = MaterialTheme.typography.labelMedium,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                }
                            }

                            Text(
                                text = stringResource(R.string.settings_about_title),
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onSurface
                            )

                            Text(
                                text = stringResource(R.string.settings_about_desc),
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ThemeOptionCard(
    title: String,
    description: String,
    icon: ImageVector,
    palette: Icon3DPalette,
    previewColors: List<Color>,
    isSelected: Boolean,
    onSelect: () -> Unit,
    testTag: String
) {
    val studioColors = LocalStudioColors.current

    Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) {
                MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.42f)
            } else {
                studioColors.elevatedCardSurface
            }
        ),
        border = BorderStroke(
            width = if (isSelected) 2.dp else 1.dp,
            color = if (isSelected) MaterialTheme.colorScheme.primary else studioColors.cardBorder
        ),
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .clickable(onClick = onSelect)
            .testTag(testTag)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Interactive3DIconBadge(
                    icon = icon,
                    contentDescription = title,
                    palette = palette,
                    size = 54.dp,
                    onClick = onSelect
                )

                Spacer(modifier = Modifier.width(16.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = title,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        if (isSelected) {
                            Spacer(modifier = Modifier.width(8.dp))
                            Icon(
                                imageVector = Icons.Filled.CheckCircle,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                RadioButton(
                    selected = isSelected,
                    onClick = onSelect
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Palette Swatches:",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                previewColors.forEach { swatch ->
                    Box(
                        modifier = Modifier
                            .size(22.dp)
                            .clip(CircleShape)
                            .background(swatch)
                            .border(1.dp, MaterialTheme.colorScheme.outline, CircleShape)
                    )
                }
            }
        }
    }
}
