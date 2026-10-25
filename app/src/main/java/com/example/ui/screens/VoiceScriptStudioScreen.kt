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
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.SmartDisplay
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
import com.example.domain.ScriptDuration
import com.example.domain.ScriptTone
import com.example.domain.VoiceScriptToolType
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
import com.example.viewmodel.VoiceScriptUiState
import kotlinx.coroutines.launch

@Composable
fun VoiceScriptStudioScreen(
    state: VoiceScriptUiState,
    onSelectTool: (VoiceScriptToolType) -> Unit,
    onTopicChange: (String) -> Unit,
    onDurationChange: (ScriptDuration) -> Unit,
    onToneChange: (ScriptTone) -> Unit,
    onGenerate: (String) -> Unit,
    onBackClick: () -> Unit
) {
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val studioColors = LocalStudioColors.current
    val emptyErrorText = stringResource(R.string.error_empty_topic)
    val copiedMsg = stringResource(R.string.copied_to_clipboard)

    val starterTopics = listOf(
        "Budget Desk Audio Upgrade",
        "How Fast Pacing Keeps Viewers Watching",
        "3 Smartphone Filming Tricks"
    )

    Scaffold(
        contentWindowInsets = WindowInsets.navigationBars,
        containerColor = Color.Transparent,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            StudioTopAppBar(
                title = stringResource(R.string.section_voice_title),
                subtitle = "${state.selectedTool.label} • ${state.duration.label}",
                onBackClick = onBackClick
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .neonStudioBackground()
                .padding(innerPadding)
                .testTag("voice_script_studio_screen"),
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
                    VoiceSubToolBar(
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
                                label = "Video Topic, Hook Idea, or Message",
                                placeholder = state.selectedTool.inputHint,
                                errorMessage = state.errorMessage,
                                onClear = { onTopicChange("") },
                                testTag = "voice_topic_input"
                            )

                            StudioOptionSelectorRow(
                                label = "Target Script Duration (${state.duration.secondsEstimate})",
                                options = ScriptDuration.entries.map { it.label },
                                selectedOption = state.duration.label,
                                onOptionSelected = { label ->
                                    ScriptDuration.entries.firstOrNull { it.label == label }
                                        ?.let(onDurationChange)
                                },
                                testTagPrefix = "voice_duration"
                            )

                            StudioOptionSelectorRow(
                                label = "Delivery Tone",
                                options = ScriptTone.entries.map { it.label },
                                selectedOption = state.tone.label,
                                onOptionSelected = { label ->
                                    ScriptTone.entries.firstOrNull { it.label == label }
                                        ?.let(onToneChange)
                                },
                                testTagPrefix = "voice_tone"
                            )

                            StudioGenerateActionRow(
                                hasResults = state.results.isNotEmpty(),
                                isGenerating = state.isGenerating,
                                onGenerateClick = { onGenerate(emptyErrorText) },
                                onCopyAllClick = {
                                    val combined = state.results.joinToString("\n\n---\n\n") {
                                        "${it.title} (${it.categoryTag})\n${it.content}"
                                    }
                                    if (ClipboardHelper.copyToClipboard(context, "Voice & Script Results", combined)) {
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
                            icon = Icons.Filled.GraphicEq,
                            palette = Icon3DPalette.VOICE_SCRIPT,
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
private fun VoiceSubToolBar(
    selectedTool: VoiceScriptToolType,
    onSelectTool: (VoiceScriptToolType) -> Unit
) {
    val studioColors = LocalStudioColors.current
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        VoiceScriptToolType.entries.forEach { tool ->
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
                    .testTag("voice_tab_${tool.name.lowercase()}")
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp, horizontal = 6.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Interactive3DIconBadge(
                        icon = tool.toVoiceIcon(),
                        contentDescription = tool.label,
                        palette = Icon3DPalette.VOICE_SCRIPT,
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

private fun VoiceScriptToolType.toVoiceIcon(): ImageVector = when (this) {
    VoiceScriptToolType.VOICE_OVER -> Icons.Filled.Mic
    VoiceScriptToolType.SHORT_VIDEO -> Icons.Filled.SmartDisplay
    VoiceScriptToolType.INTRO_HOOK -> Icons.Filled.FlashOn
    VoiceScriptToolType.CTA_GENERATOR -> Icons.Filled.Campaign
}
