package com.example.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.AppThemeMode
import com.example.data.ThemePreferencesRepository
import com.example.domain.ContentGenerationProvider
import com.example.domain.GamingToolType
import com.example.domain.GeneratedItem
import com.example.domain.LocalCreatorContentEngine
import com.example.domain.ScriptDuration
import com.example.domain.ScriptTone
import com.example.domain.ThumbnailAspectRatio
import com.example.domain.ThumbnailCategory
import com.example.domain.ThumbnailPromptRequest
import com.example.domain.ToolkitToolType
import com.example.domain.VoiceScriptToolType
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class GamingUiState(
    val selectedTool: GamingToolType = GamingToolType.TITLES,
    val topic: String = "",
    val styleFilter: String = "Competitive",
    val errorMessage: String? = null,
    val isGenerating: Boolean = false,
    val results: List<GeneratedItem> = emptyList(),
    val generationSeed: Int = 1
)

data class VoiceScriptUiState(
    val selectedTool: VoiceScriptToolType = VoiceScriptToolType.VOICE_OVER,
    val topic: String = "",
    val duration: ScriptDuration = ScriptDuration.MEDIUM_30,
    val tone: ScriptTone = ScriptTone.HYPE,
    val errorMessage: String? = null,
    val isGenerating: Boolean = false,
    val results: List<GeneratedItem> = emptyList(),
    val generationSeed: Int = 1
)

data class ThumbnailUiState(
    val category: ThumbnailCategory = ThumbnailCategory.GAMING,
    val aspectRatio: ThumbnailAspectRatio = ThumbnailAspectRatio.HORIZONTAL_16_9,
    val topic: String = "",
    val visualStyle: String = "3D Hyper-Real Render",
    val lighting: String = "Neon Purple & Rim Glow",
    val composition: String = "Expressive Close-Up + Rule of Thirds",
    val textOverlayInstruction: String = "",
    val errorMessage: String? = null,
    val isGenerating: Boolean = false,
    val results: List<GeneratedItem> = emptyList(),
    val generationSeed: Int = 1
)

data class ToolkitUiState(
    val selectedTool: ToolkitToolType = ToolkitToolType.CAPTIONS,
    val topic: String = "",
    val platformFocus: String = "YouTube & Shorts",
    val errorMessage: String? = null,
    val isGenerating: Boolean = false,
    val results: List<GeneratedItem> = emptyList(),
    val generationSeed: Int = 1
)

class StudioViewModel(
    application: Application,
    private val themeRepository: ThemePreferencesRepository,
    private val generatorEngine: ContentGenerationProvider
) : AndroidViewModel(application) {

    val themeMode: StateFlow<AppThemeMode> = themeRepository.themeModeFlow.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = AppThemeMode.NEON_DARK
    )

    private val _gamingState = MutableStateFlow(GamingUiState())
    val gamingState: StateFlow<GamingUiState> = _gamingState.asStateFlow()

    private val _voiceState = MutableStateFlow(VoiceScriptUiState())
    val voiceState: StateFlow<VoiceScriptUiState> = _voiceState.asStateFlow()

    private val _thumbnailState = MutableStateFlow(ThumbnailUiState())
    val thumbnailState: StateFlow<ThumbnailUiState> = _thumbnailState.asStateFlow()

    private val _toolkitState = MutableStateFlow(ToolkitUiState())
    val toolkitState: StateFlow<ToolkitUiState> = _toolkitState.asStateFlow()

    fun setThemeMode(mode: AppThemeMode) {
        viewModelScope.launch {
            themeRepository.setThemeMode(mode)
        }
    }

    // --- Gaming Studio Actions ---
    fun selectGamingTool(tool: GamingToolType) {
        _gamingState.update {
            it.copy(selectedTool = tool, errorMessage = null, results = emptyList())
        }
    }

    fun updateGamingTopic(input: String) {
        _gamingState.update {
            it.copy(topic = input, errorMessage = if (input.isNotBlank()) null else it.errorMessage)
        }
    }

    fun updateGamingStyle(style: String) {
        _gamingState.update { it.copy(styleFilter = style) }
    }

    fun generateGamingResults(emptyErrorMsg: String) {
        val current = _gamingState.value
        if (current.topic.isBlank()) {
            _gamingState.update { it.copy(errorMessage = emptyErrorMsg) }
            return
        }
        viewModelScope.launch {
            _gamingState.update { it.copy(isGenerating = true, errorMessage = null) }
            delay(180)
            val nextSeed = current.generationSeed + 1
            val generated = generatorEngine.generateGamingContent(
                toolType = current.selectedTool,
                topic = current.topic,
                styleFilter = current.styleFilter,
                seed = nextSeed
            )
            _gamingState.update {
                it.copy(
                    isGenerating = false,
                    results = generated,
                    generationSeed = nextSeed
                )
            }
        }
    }

    // --- Voice & Script Studio Actions ---
    fun selectVoiceTool(tool: VoiceScriptToolType) {
        _voiceState.update {
            it.copy(selectedTool = tool, errorMessage = null, results = emptyList())
        }
    }

    fun updateVoiceTopic(input: String) {
        _voiceState.update {
            it.copy(topic = input, errorMessage = if (input.isNotBlank()) null else it.errorMessage)
        }
    }

    fun updateScriptDuration(duration: ScriptDuration) {
        _voiceState.update { it.copy(duration = duration) }
    }

    fun updateScriptTone(tone: ScriptTone) {
        _voiceState.update { it.copy(tone = tone) }
    }

    fun generateVoiceResults(emptyErrorMsg: String) {
        val current = _voiceState.value
        if (current.topic.isBlank()) {
            _voiceState.update { it.copy(errorMessage = emptyErrorMsg) }
            return
        }
        viewModelScope.launch {
            _voiceState.update { it.copy(isGenerating = true, errorMessage = null) }
            delay(180)
            val nextSeed = current.generationSeed + 1
            val generated = generatorEngine.generateVoiceScriptContent(
                toolType = current.selectedTool,
                topic = current.topic,
                duration = current.duration,
                tone = current.tone,
                seed = nextSeed
            )
            _voiceState.update {
                it.copy(
                    isGenerating = false,
                    results = generated,
                    generationSeed = nextSeed
                )
            }
        }
    }

    // --- Thumbnail Prompt Studio Actions ---
    fun updateThumbnailCategory(category: ThumbnailCategory) {
        _thumbnailState.update { it.copy(category = category) }
    }

    fun updateThumbnailAspectRatio(ratio: ThumbnailAspectRatio) {
        _thumbnailState.update { it.copy(aspectRatio = ratio) }
    }

    fun updateThumbnailTopic(input: String) {
        _thumbnailState.update {
            it.copy(topic = input, errorMessage = if (input.isNotBlank()) null else it.errorMessage)
        }
    }

    fun updateThumbnailStyle(style: String) {
        _thumbnailState.update { it.copy(visualStyle = style) }
    }

    fun updateThumbnailLighting(lighting: String) {
        _thumbnailState.update { it.copy(lighting = lighting) }
    }

    fun updateThumbnailComposition(composition: String) {
        _thumbnailState.update { it.copy(composition = composition) }
    }

    fun updateThumbnailTextInstruction(textInstruction: String) {
        _thumbnailState.update { it.copy(textOverlayInstruction = textInstruction) }
    }

    fun generateThumbnailResults(emptyErrorMsg: String) {
        val current = _thumbnailState.value
        if (current.topic.isBlank()) {
            _thumbnailState.update { it.copy(errorMessage = emptyErrorMsg) }
            return
        }
        viewModelScope.launch {
            _thumbnailState.update { it.copy(isGenerating = true, errorMessage = null) }
            delay(180)
            val nextSeed = current.generationSeed + 1
            val request = ThumbnailPromptRequest(
                topic = current.topic,
                category = current.category,
                aspectRatio = current.aspectRatio,
                visualStyle = current.visualStyle,
                lighting = current.lighting,
                composition = current.composition,
                textOverlayInstruction = current.textOverlayInstruction
            )
            val generated = generatorEngine.generateThumbnailPrompts(request, nextSeed)
            _thumbnailState.update {
                it.copy(
                    isGenerating = false,
                    results = generated,
                    generationSeed = nextSeed
                )
            }
        }
    }

    // --- Creator Toolkit Actions ---
    fun selectToolkitTool(tool: ToolkitToolType) {
        _toolkitState.update {
            it.copy(selectedTool = tool, errorMessage = null, results = emptyList())
        }
    }

    fun updateToolkitTopic(input: String) {
        _toolkitState.update {
            it.copy(topic = input, errorMessage = if (input.isNotBlank()) null else it.errorMessage)
        }
    }

    fun updateToolkitPlatform(platform: String) {
        _toolkitState.update { it.copy(platformFocus = platform) }
    }

    fun generateToolkitResults(emptyErrorMsg: String) {
        val current = _toolkitState.value
        if (current.topic.isBlank()) {
            _toolkitState.update { it.copy(errorMessage = emptyErrorMsg) }
            return
        }
        viewModelScope.launch {
            _toolkitState.update { it.copy(isGenerating = true, errorMessage = null) }
            delay(180)
            val nextSeed = current.generationSeed + 1
            val generated = generatorEngine.generateToolkitContent(
                toolType = current.selectedTool,
                topic = current.topic,
                platformFocus = current.platformFocus,
                seed = nextSeed
            )
            _toolkitState.update {
                it.copy(
                    isGenerating = false,
                    results = generated,
                    generationSeed = nextSeed
                )
            }
        }
    }

    companion object {
        fun provideFactory(application: Application): ViewModelProvider.Factory {
            return object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return StudioViewModel(
                        application = application,
                        themeRepository = ThemePreferencesRepository(application),
                        generatorEngine = LocalCreatorContentEngine()
                    ) as T
                }
            }
        }
    }
}
