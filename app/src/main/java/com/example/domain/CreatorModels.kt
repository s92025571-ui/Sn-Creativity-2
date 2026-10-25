package com.example.domain

enum class StudioSection(
    val id: String,
    val title: String,
    val subtitle: String,
    val badge: String
) {
    GAMING_STUDIO(
        id = "gaming_studio",
        title = "Gaming Studio",
        subtitle = "Titles, video ideas, hashtags & short-form concepts",
        badge = "4 TOOLS"
    ),
    VOICE_SCRIPT_STUDIO(
        id = "voice_script_studio",
        title = "Voice & Script Studio",
        subtitle = "Voice-overs, short scripts, hooks & CTAs with tone/duration",
        badge = "4 TOOLS"
    ),
    THUMBNAIL_PROMPT_STUDIO(
        id = "thumbnail_prompt_studio",
        title = "Thumbnail Prompt Studio",
        subtitle = "16:9 & 9:16 visual prompt blueprints with lighting & text",
        badge = "PRO PROMPTS"
    ),
    CREATOR_TOOLKIT(
        id = "creator_toolkit",
        title = "Creator Toolkit",
        subtitle = "Hashtags, captions, descriptions, prompts & planners",
        badge = "5 TOOLS"
    )
}

enum class GamingToolType(val label: String, val inputHint: String) {
    TITLES("Gaming Titles", "e.g., Valorant Immortal Clutch, Minecraft Hardcore Survival"),
    VIDEO_IDEAS("Video Ideas", "e.g., Elden Ring Boss Challenge, GTA RP Heist"),
    HASHTAGS("Gaming Hashtags", "e.g., Fortnite Zero Build, Mobile Legends"),
    SHORT_FORM("Shorts / Reels", "e.g., Warzone Sniper Loadout, Roblox Obby Speedrun")
}

enum class VoiceScriptToolType(val label: String, val inputHint: String) {
    VOICE_OVER("Voice-Over Scripts", "e.g., Top 5 Mistakes New Creators Make in Editing"),
    SHORT_VIDEO("Short-Video Scripts", "e.g., Why 120Hz Displays Change Competitive Gaming"),
    INTRO_HOOK("Intro & Hooks", "e.g., Testing the Weirdest Budget Studio Microphone"),
    CTA_GENERATOR("Call-to-Action", "e.g., Weekly Creator Tutorials & Preset Breakdowns")
}

enum class ScriptDuration(val label: String, val secondsEstimate: String) {
    SHORT_15("15s", "~35–45 words"),
    MEDIUM_30("30s", "~70–85 words"),
    STANDARD_60("60s", "~135–160 words"),
    LONG_180("3 Min", "~380+ words outline")
}

enum class ScriptTone(val label: String) {
    HYPE("High-Energy Hype"),
    CINEMATIC("Cinematic Story"),
    EDUCATIONAL("Pro Breakdown"),
    WITTY("Witty & Punchy")
}

enum class ThumbnailCategory(val label: String) {
    PROFESSIONAL("Professional Creator"),
    GAMING("High-Impact Gaming")
}

enum class ThumbnailAspectRatio(val label: String, val ratioCode: String) {
    HORIZONTAL_16_9("Horizontal 16:9", "16:9"),
    VERTICAL_9_16("Vertical 9:16", "9:16")
}

data class ThumbnailPromptRequest(
    val topic: String,
    val category: ThumbnailCategory = ThumbnailCategory.PROFESSIONAL,
    val aspectRatio: ThumbnailAspectRatio = ThumbnailAspectRatio.HORIZONTAL_16_9,
    val visualStyle: String = "3D Hyper-Real Render",
    val lighting: String = "Neon Purple & Rim Glow",
    val composition: String = "Expressive Close-Up + Rule of Thirds",
    val textOverlayInstruction: String = ""
)

enum class ToolkitToolType(val label: String, val inputHint: String) {
    HASHTAGS("Hashtag Generator", "e.g., Desk Setup Tour, Tech Minimalist Workflow"),
    CAPTIONS("Social Captions", "e.g., Behind the Scenes of Filming at 2 AM"),
    DESCRIPTIONS("Content Descriptions", "e.g., Complete Mobile Video Editing Masterclass"),
    CREATIVE_PROMPTS("Creative Prompts", "e.g., Futuristic Cyberpunk Creator Room Concept"),
    CONTENT_PLANNING("Content Planner", "e.g., Growing a Tech & Productivity Channel")
}

data class GeneratedItem(
    val id: String,
    val categoryTag: String,
    val title: String,
    val content: String,
    val metadataHint: String = ""
)

/**
 * Clean abstraction for content generation so an external or on-device provider
 * can be swapped or chained in future releases while running 100% locally today.
 */
interface ContentGenerationProvider {
    fun generateGamingContent(
        toolType: GamingToolType,
        topic: String,
        styleFilter: String,
        seed: Int
    ): List<GeneratedItem>

    fun generateVoiceScriptContent(
        toolType: VoiceScriptToolType,
        topic: String,
        duration: ScriptDuration,
        tone: ScriptTone,
        seed: Int
    ): List<GeneratedItem>

    fun generateThumbnailPrompts(
        request: ThumbnailPromptRequest,
        seed: Int
    ): List<GeneratedItem>

    fun generateToolkitContent(
        toolType: ToolkitToolType,
        topic: String,
        platformFocus: String,
        seed: Int
    ): List<GeneratedItem>
}
