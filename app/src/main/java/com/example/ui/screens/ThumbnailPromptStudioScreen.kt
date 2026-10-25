package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CropLandscape
import androidx.compose.material.icons.filled.CropPortrait
import androidx.compose.material.icons.filled.PhotoSizeSelectActual
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.R
import com.example.domain.ThumbnailAspectRatio
import com.example.domain.ThumbnailCategory
import com.example.ui.components.GeneratedResultCard
import com.example.ui.components.Icon3DPalette
import com.example.ui.components.Interactive3DIconBadge
import com.example.ui.components.StudioEmptyResultsState
import com.example.ui.components.StudioGenerateActionRow
import com.example.ui.components.StudioOptionSelectorRow
import com.example.ui.components.StudioTopAppBar
import com.example.ui.components.StudioTopicInputField
import com.example.ui.components.neonStudioBackground
import com.example.ui.theme.LocalStudioColors
import com.example.util.ClipboardHelper
import com.example.viewmodel.ThumbnailUiState
import kotlinx.coroutines.launch

@Composable
fun ThumbnailPromptStudioScreen(
    state: ThumbnailUiState,
    onCategoryChange: (ThumbnailCategory) -> Unit,
    onAspectRatioChange: (ThumbnailAspectRatio) -> Unit,
    onTopicChange: (String) -> Unit,
    onStyleChange: (String) -> Unit,
    onLightingChange: (String) -> Unit,
    onCompositionChange: (String) -> Unit,
    onTextInstructionChange: (String) -> Unit,
    onGenerate: (String) -> Unit,
    onBackClick: () -> Unit
) {
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val studioColors = LocalStudioColors.current
    val emptyErrorText = stringResource(R.string.error_empty_topic)
    val copiedMsg = stringResource(R.string.copied_to_clipboard)

    val styles = listOf(
        "3D Hyper-Real Render",
        "Photorealistic Cinema",
        "Vibrant Stylized Art",
        "Clean Tech Minimal"
    )
    val lightings = listOf(
        "Neon Purple & Rim Glow",
        "Dramatic Studio Key Light",
        "Cyberpunk Dual-Tone",
        "Golden Contrast Spotlight"
    )
    val compositions = listOf(
        "Expressive Close-Up + Rule of Thirds",
        "Hero Center Silhouette",
        "Split Before / After Contrast",
        "Dynamic Over-Shoulder Depth"
    )
    val starterTopics = listOf(
        "Ultimate $500 vs $5000 Gaming Setup",
        "Surviving Final Boss at 1 HP",
        "Creator Desk Transformation"
    )

    Scaffold(
        contentWindowInsets = WindowInsets.navigationBars,
        containerColor = Color.Transparent,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            StudioTopAppBar(
                title = stringResource(R.string.section_thumbnail_title),
                subtitle = "${state.category.label} • ${state.aspectRatio.ratioCode}",
                onBackClick = onBackClick
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .neonStudioBackground()
                .padding(innerPadding)
                .testTag("thumbnail_studio_screen"),
            contentAlignment = Alignment.TopCenter
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .widthIn(max = 760.dp),
                contentPadding = PaddingValues(horizontal = 20.dp, vertical = 14.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    AspectRatioAndCategoryBar(
                        selectedCategory = state.category,
                        selectedRatio = state.aspectRatio,
                        onCategoryChange = onCategoryChange,
                        onAspectRatioChange = onAspectRatioChange
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
                                .padding(18.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            StudioTopicInputField(
                                value = state.topic,
                                onValueChange = onTopicChange,
                                label = "Thumbnail Subject or Video Concept",
                                placeholder = "e.g., Futuristic Neon Controller Breakdown, Ranked 1v5 Final Round",
                                errorMessage = state.errorMessage,
                                onClear = { onTopicChange("") },
                                testTag = "thumbnail_topic_input"
                            )

                            StudioOptionSelectorRow(
                                label = "Visual Rendering Style",
                                options = styles,
                                selectedOption = state.visualStyle,
                                onOptionSelected = onStyleChange,
                                testTagPrefix = "thumb_style"
                            )

                            StudioOptionSelectorRow(
                                label = "Studio Lighting Mood",
                                options = lightings,
                                selectedOption = state.lighting,
                                onOptionSelected = onLightingChange,
                                testTagPrefix = "thumb_lighting"
                            )

                            StudioOptionSelectorRow(
                                label = "Camera Framing & Composition",
                                options = compositions,
                                selectedOption = state.composition,
                                onOptionSelected = onCompositionChange,
                                testTagPrefix = "thumb_composition"
                            )

                            OutlinedTextField(
                                value = state.textOverlayInstruction,
                                onValueChange = onTextInstructionChange,
                                label = { Text("Headline Text Instruction (Optional)") },
                                placeholder = { Text("e.g., SECRET META, LEVEL 100, DO NOT BUY") },
                                singleLine = true,
                                shape = RoundedCornerShape(16.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                                    unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                                    focusedContainerColor = MaterialTheme.colorScheme.surface,
                                    unfocusedContainerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.85f)
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("thumbnail_text_overlay_input")
                            )

                            StudioGenerateActionRow(
                                hasResults = state.results.isNotEmpty(),
                                isGenerating = state.isGenerating,
                                onGenerateClick = { onGenerate(emptyErrorText) },
                                onCopyAllClick = {
                                    val combined = state.results.joinToString("\n\n") {
                                        "${it.title} [${it.categoryTag}]:\n${it.content}"
                                    }
                                    if (ClipboardHelper.copyToClipboard(context, "Thumbnail Prompts", combined)) {
                                        scope.launch { snackbarHostState.showSnackbar(copiedMsg) }
                                    }
                                }
                            )
                        }
                    }
                }

                if (state.results.isEmpty()) {
                    item {
                        StudioEmptyResultsState(
                            icon = Icons.Filled.PhotoSizeSelectActual,
                            palette = Icon3DPalette.THUMBNAIL,
                            quickSuggestions = starterTopics,
                            onSuggestionClick = { sample ->
                                onTopicChange(sample)
                                onGenerate(emptyErrorText)
                            }
                        )
                    }
                } else {
                    itemsIndexed(state.results, key = { _, item -> item.id }) { index, item ->
                        GeneratedResultCard(
                            item = item,
                            index = index,
                            onCopy = { text ->
                                if (ClipboardHelper.copyToClipboard(context, item.title, text)) {
                                    scope.launch { snackbarHostState.showSnackbar(copiedMsg) }
                                }
                            }
                        )
                    }
                }

                item { Spacer(modifier = Modifier.height(16.dp)) }
            }
        }
    }
}

@Composable
private fun AspectRatioAndCategoryBar(
    selectedCategory: ThumbnailCategory,
    selectedRatio: ThumbnailAspectRatio,
    onCategoryChange: (ThumbnailCategory) -> Unit,
    onAspectRatioChange: (ThumbnailAspectRatio) -> Unit
) {
    val studioColors = LocalStudioColors.current

    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        StudioOptionSelectorRow(
            label = "Thumbnail Mode",
            options = ThumbnailCategory.entries.map { it.label },
            selectedOption = selectedCategory.label,
            onOptionSelected = { label ->
                ThumbnailCategory.entries.firstOrNull { it.label == label }?.let(onCategoryChange)
            },
            testTagPrefix = "thumb_category"
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            ThumbnailAspectRatio.entries.forEach { ratio ->
                val isSelected = ratio == selectedRatio
                val icon = if (ratio == ThumbnailAspectRatio.HORIZONTAL_16_9) {
                    Icons.Filled.CropLandscape
                } else {
                    Icons.Filled.CropPortrait
                }

                Card(
                    onClick = { onAspectRatioChange(ratio) },
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isSelected) {
                            MaterialTheme.colorScheme.primaryContainer
                        } else {
                            studioColors.elevatedCardSurface
                        }
                    ),
                    border = BorderStroke(
                        width = if (isSelected) 1.5.dp else 1.dp,
                        color = if (isSelected) MaterialTheme.colorScheme.primary else studioColors.cardBorder
                    ),
                    modifier = Modifier
                        .weight(1f)
                        .testTag("aspect_ratio_${ratio.ratioCode.replace(":", "_")}")
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Interactive3DIconBadge(
                            icon = icon,
                            contentDescription = ratio.label,
                            palette = Icon3DPalette.THUMBNAIL,
                            size = 42.dp,
                            onClick = { onAspectRatioChange(ratio) }
                        )
                        Column {
                            Text(
                                text = ratio.label,
                                style = MaterialTheme.typography.titleSmall,
                                color = if (isSelected) {
                                    MaterialTheme.colorScheme.onPrimaryContainer
                                } else {
                                    MaterialTheme.colorScheme.onSurface
                                }
                            )
                            Text(
                                text = if (ratio == ThumbnailAspectRatio.HORIZONTAL_16_9) {
                                    "YouTube Widescreen"
                                } else {
                                    "Shorts / Reels Cover"
                                },
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }
    }
}
