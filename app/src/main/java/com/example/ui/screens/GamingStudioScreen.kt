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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.MotionPhotosOn
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material.icons.filled.Tag
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.R
import com.example.domain.GamingToolType
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
import com.example.viewmodel.GamingUiState
import kotlinx.coroutines.launch

@Composable
fun GamingStudioScreen(
    state: GamingUiState,
    onSelectTool: (GamingToolType) -> Unit,
    onTopicChange: (String) -> Unit,
    onStyleChange: (String) -> Unit,
    onGenerate: (String) -> Unit,
    onBackClick: () -> Unit
) {
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val studioColors = LocalStudioColors.current
    val emptyErrorText = stringResource(R.string.error_empty_topic)
    val copiedMsg = stringResource(R.string.copied_to_clipboard)

    val gamingStyles = listOf("Competitive", "Funny & Chaos", "Hardcore Challenge", "Pro Guide", "Story Lore")
    val starterTopics = listOf("Valorant Clutch Guide", "Minecraft 100 Days", "GTA 6 Theories", "Elden Ring Solo")

    Scaffold(
        contentWindowInsets = WindowInsets.navigationBars,
        containerColor = Color.Transparent,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            StudioTopAppBar(
                title = stringResource(R.string.section_gaming_title),
                subtitle = state.selectedTool.label,
                onBackClick = onBackClick
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .neonStudioBackground()
                .padding(innerPadding)
                .testTag("gaming_studio_screen"),
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
                    GamingSubToolBar(
                        selectedTool = state.selectedTool,
                        onSelectTool = onSelectTool
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
                                label = "Game Title, Challenge, or Topic",
                                placeholder = state.selectedTool.inputHint,
                                errorMessage = state.errorMessage,
                                onClear = { onTopicChange("") },
                                testTag = "gaming_topic_input"
                            )

                            StudioOptionSelectorRow(
                                label = "Gaming Content Angle",
                                options = gamingStyles,
                                selectedOption = state.styleFilter,
                                onOptionSelected = onStyleChange,
                                testTagPrefix = "gaming_style"
                            )

                            StudioGenerateActionRow(
                                hasResults = state.results.isNotEmpty(),
                                isGenerating = state.isGenerating,
                                onGenerateClick = { onGenerate(emptyErrorText) },
                                onCopyAllClick = {
                                    val combined = state.results.joinToString("\n\n") {
                                        "${it.title}:\n${it.content}"
                                    }
                                    if (ClipboardHelper.copyToClipboard(context, "Gaming Studio Results", combined)) {
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
                            icon = Icons.Filled.SportsEsports,
                            palette = Icon3DPalette.GAMING,
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
private fun GamingSubToolBar(
    selectedTool: GamingToolType,
    onSelectTool: (GamingToolType) -> Unit
) {
    val studioColors = LocalStudioColors.current
    val tools = GamingToolType.entries

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        tools.forEach { tool ->
            val selected = tool == selectedTool
            Card(
                onClick = { onSelectTool(tool) },
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (selected) {
                        MaterialTheme.colorScheme.primaryContainer
                    } else {
                        studioColors.elevatedCardSurface
                    }
                ),
                border = BorderStroke(
                    width = if (selected) 1.5.dp else 1.dp,
                    color = if (selected) MaterialTheme.colorScheme.primary else studioColors.cardBorder
                ),
                modifier = Modifier
                    .weight(1f)
                    .testTag("gaming_tab_${tool.name.lowercase()}")
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp, horizontal = 6.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Interactive3DIconBadge(
                        icon = tool.toToolIcon(),
                        contentDescription = tool.label,
                        palette = Icon3DPalette.GAMING,
                        size = 42.dp,
                        onClick = { onSelectTool(tool) }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = tool.label,
                        style = MaterialTheme.typography.labelSmall,
                        color = if (selected) {
                            MaterialTheme.colorScheme.onPrimaryContainer
                        } else {
                            MaterialTheme.colorScheme.onSurfaceVariant
                        },
                        maxLines = 1
                    )
                }
            }
        }
    }
}

private fun GamingToolType.toToolIcon(): ImageVector = when (this) {
    GamingToolType.TITLES -> Icons.Filled.EmojiEvents
    GamingToolType.VIDEO_IDEAS -> Icons.Filled.Lightbulb
    GamingToolType.HASHTAGS -> Icons.Filled.Tag
    GamingToolType.SHORT_FORM -> Icons.Filled.MotionPhotosOn
}
