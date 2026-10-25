package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.ChatBubble
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Palette
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
import com.example.domain.ToolkitToolType
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
import com.example.viewmodel.ToolkitUiState
import kotlinx.coroutines.launch

@Composable
fun CreatorToolkitScreen(
    state: ToolkitUiState,
    onSelectTool: (ToolkitToolType) -> Unit,
    onTopicChange: (String) -> Unit,
    onPlatformChange: (String) -> Unit,
    onGenerate: (String) -> Unit,
    onBackClick: () -> Unit
) {
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val studioColors = LocalStudioColors.current
    val emptyErrorText = stringResource(R.string.error_empty_topic)
    val copiedMsg = stringResource(R.string.copied_to_clipboard)

    val platforms = listOf("YouTube & Shorts", "Instagram Reels", "TikTok Creators", "Multi-Platform")
    val starterTopics = listOf(
        "Minimalist Creator Desk Tour",
        "Color Grading Workflow Tips",
        "Building a Consistent Upload Habit"
    )

    Scaffold(
        contentWindowInsets = WindowInsets.navigationBars,
        containerColor = Color.Transparent,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            StudioTopAppBar(
                title = stringResource(R.string.section_toolkit_title),
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
                .testTag("creator_toolkit_screen"),
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
                    ToolkitSubToolBar(
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
                                label = "Content Niche, Upload Topic, or Project",
                                placeholder = state.selectedTool.inputHint,
                                errorMessage = state.errorMessage,
                                onClear = { onTopicChange("") },
                                testTag = "toolkit_topic_input"
                            )

                            StudioOptionSelectorRow(
                                label = "Platform Focus",
                                options = platforms,
                                selectedOption = state.platformFocus,
                                onOptionSelected = onPlatformChange,
                                testTagPrefix = "toolkit_platform"
                            )

                            StudioGenerateActionRow(
                                hasResults = state.results.isNotEmpty(),
                                isGenerating = state.isGenerating,
                                onGenerateClick = { onGenerate(emptyErrorText) },
                                onCopyAllClick = {
                                    val combined = state.results.joinToString("\n\n---\n\n") {
                                        "${it.title}:\n${it.content}"
                                    }
                                    if (ClipboardHelper.copyToClipboard(context, "Creator Toolkit Results", combined)) {
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
                            icon = Icons.Filled.AutoAwesome,
                            palette = Icon3DPalette.TOOLKIT,
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
private fun ToolkitSubToolBar(
    selectedTool: ToolkitToolType,
    onSelectTool: (ToolkitToolType) -> Unit
) {
    val studioColors = LocalStudioColors.current
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        ToolkitToolType.entries.forEach { tool ->
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
                    .width(122.dp)
                    .testTag("toolkit_tab_${tool.name.lowercase()}")
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp, horizontal = 8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Interactive3DIconBadge(
                        icon = tool.toToolkitIcon(),
                        contentDescription = tool.label,
                        palette = Icon3DPalette.TOOLKIT,
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

private fun ToolkitToolType.toToolkitIcon(): ImageVector = when (this) {
    ToolkitToolType.HASHTAGS -> Icons.Filled.Tag
    ToolkitToolType.CAPTIONS -> Icons.Filled.ChatBubble
    ToolkitToolType.DESCRIPTIONS -> Icons.Filled.Description
    ToolkitToolType.CREATIVE_PROMPTS -> Icons.Filled.Palette
    ToolkitToolType.CONTENT_PLANNING -> Icons.Filled.CalendarMonth
}
