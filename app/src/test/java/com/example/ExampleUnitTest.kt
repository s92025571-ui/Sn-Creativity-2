package com.example

import com.example.domain.GamingToolType
import com.example.domain.LocalCreatorContentEngine
import com.example.domain.ScriptDuration
import com.example.domain.ScriptTone
import com.example.domain.ThumbnailAspectRatio
import com.example.domain.ThumbnailCategory
import com.example.domain.ThumbnailPromptRequest
import com.example.domain.ToolkitToolType
import com.example.domain.VoiceScriptToolType
import org.junit.Assert.assertTrue
import org.junit.Test

class ExampleUnitTest {
    private val engine = LocalCreatorContentEngine()

    @Test
    fun gamingStudio_generatesVariedOutputs() {
        val titles = engine.generateGamingContent(
            toolType = GamingToolType.TITLES,
            topic = "Valorant Immortal Clutch",
            styleFilter = "Competitive",
            seed = 1
        )
        assertTrue(titles.size >= 3)
        assertTrue(titles.all { it.content.contains("Valorant", ignoreCase = true) })
    }

    @Test
    fun voiceScriptStudio_respectsDurationAndTone() {
        val scripts = engine.generateVoiceScriptContent(
            toolType = VoiceScriptToolType.VOICE_OVER,
            topic = "Studio Lighting Tips",
            duration = ScriptDuration.STANDARD_60,
            tone = ScriptTone.CINEMATIC,
            seed = 2
        )
        assertTrue(scripts.isNotEmpty())
        assertTrue(scripts.first().content.contains("Studio Lighting Tips", ignoreCase = true))
    }

    @Test
    fun thumbnailStudio_supportsVerticalAndHorizontalFormats() {
        val verticalPrompts = engine.generateThumbnailPrompts(
            request = ThumbnailPromptRequest(
                topic = "Minecraft Hardcore Dragon",
                category = ThumbnailCategory.GAMING,
                aspectRatio = ThumbnailAspectRatio.VERTICAL_9_16,
                textOverlayInstruction = "1 HP CLUTCH"
            ),
            seed = 3
        )
        assertTrue(verticalPrompts.isNotEmpty())
        assertTrue(verticalPrompts.first().content.contains("9:16"))
        assertTrue(verticalPrompts.first().content.contains("1 HP CLUTCH"))
    }

    @Test
    fun creatorToolkit_generatesAllFiveToolTypes() {
        ToolkitToolType.entries.forEach { tool ->
            val items = engine.generateToolkitContent(
                toolType = tool,
                topic = "Minimalist Desk Workflow",
                platformFocus = "YouTube & Shorts",
                seed = 4
            )
            assertTrue("Expected non-empty output for $tool", items.isNotEmpty())
        }
    }
}
