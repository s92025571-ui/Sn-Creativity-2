package com.example.domain

import java.util.Locale
import kotlin.math.abs

class LocalCreatorContentEngine : ContentGenerationProvider {

    private fun cleanTopic(raw: String): String {
        return raw.trim().replace(Regex("\\s+"), " ")
    }

    private fun titleCaseTopic(raw: String): String {
        val cleaned = cleanTopic(raw)
        return cleaned.split(" ").joinToString(" ") { word ->
            if (word.length <= 2 && word.uppercase(Locale.US) == word) {
                word
            } else {
                word.replaceFirstChar {
                    if (it.isLowerCase()) it.titlecase(Locale.US) else it.toString()
                }
            }
        }
    }

    private fun extractKeywords(topic: String): List<String> {
        val stopWords = setOf(
            "the", "and", "for", "with", "how", "why", "into", "from", "this", "that",
            "your", "best", "top", "new", "video", "game", "gaming", "using", "in", "on", "to", "of", "a"
        )
        val tokens = cleanTopic(topic)
            .lowercase(Locale.US)
            .replace(Regex("[^a-z0-9\\s]"), "")
            .split(" ")
            .filter { it.length >= 3 && it !in stopWords }
            .distinct()

        return if (tokens.isEmpty()) {
            listOf(cleanTopic(topic).lowercase(Locale.US).replace(Regex("[^a-z0-9]"), "").ifEmpty { "creator" })
        } else {
            tokens
        }
    }

    private fun <T> rotateList(items: List<T>, seed: Int, count: Int): List<T> {
        if (items.isEmpty()) return emptyList()
        val offset = abs(seed) % items.size
        val rotated = items.drop(offset) + items.take(offset)
        return rotated.take(count.coerceAtMost(items.size))
    }

    override fun generateGamingContent(
        toolType: GamingToolType,
        topic: String,
        styleFilter: String,
        seed: Int
    ): List<GeneratedItem> {
        val subject = titleCaseTopic(topic)
        val keywords = extractKeywords(topic)
        val primaryTag = keywords.firstOrNull()?.replaceFirstChar { it.uppercase() } ?: "Gaming"

        return when (toolType) {
            GamingToolType.TITLES -> {
                val templates = listOf(
                    GeneratedItem(
                        id = "gt_1_$seed",
                        categoryTag = "$styleFilter • High CTR",
                        title = "Curiosity Gap Hook",
                        content = "I Tried $subject for 24 Hours Straight… Nobody Expected This Ending",
                        metadataHint = "58–68 chars optimal • Strong intrigue trigger"
                    ),
                    GeneratedItem(
                        id = "gt_2_$seed",
                        categoryTag = "$styleFilter • Competitive",
                        title = "Pro Mastery Angle",
                        content = "The Secret $subject Strategy That Pro Players Never Share",
                        metadataHint = "Authority + Insider Knowledge"
                    ),
                    GeneratedItem(
                        id = "gt_3_$seed",
                        categoryTag = "$styleFilter • Challenge",
                        title = "Extreme Ruleset Title",
                        content = "Can You Actually Beat $subject Without Taking a Single Hit?",
                        metadataHint = "High retention challenge framing"
                    ),
                    GeneratedItem(
                        id = "gt_4_$seed",
                        categoryTag = "$styleFilter • Story Arc",
                        title = "Zero-to-Hero Progression",
                        content = "From Complete Beginner to Unstoppable in $subject (Full Breakdown)",
                        metadataHint = "Evergreen search + browse hybrid"
                    ),
                    GeneratedItem(
                        id = "gt_5_$seed",
                        categoryTag = "$styleFilter • Comparison",
                        title = "Myth-Busting Experiment",
                        content = "I Tested 7 Viral $subject Tricks So You Don't Have To",
                        metadataHint = "High save & share potential"
                    ),
                    GeneratedItem(
                        id = "gt_6_$seed",
                        categoryTag = "$styleFilter • Urgent Meta",
                        title = "Patch / Meta Update",
                        content = "Stop Doing This in $subject! Use This New $styleFilter Setup Instead",
                        metadataHint = "Direct callout for active players"
                    )
                )
                rotateList(templates, seed + topic.hashCode(), 4)
            }

            GamingToolType.VIDEO_IDEAS -> {
                val templates = listOf(
                    GeneratedItem(
                        id = "gvi_1_$seed",
                        categoryTag = "$styleFilter • Full Episode",
                        title = "100-Day / Escalating Stakes Concept",
                        content = "Concept: Start with the weakest gear in $subject and upgrade one tier every time you complete a viewer-style milestone.\n\nKey Pacing Beats:\n• 0:00–0:25 Show the final boss/clutch moment first, then rewind.\n• Midpoint twist: Add a sudden handicap rule halfway through.\n• Payoff: Final high-stakes showdown with live commentary.",
                        metadataHint = "High average view duration format"
                    ),
                    GeneratedItem(
                        id = "gvi_2_$seed",
                        categoryTag = "$styleFilter • Analytical",
                        title = "Ranked Climb & Mistake Audit",
                        content = "Concept: Break down 3 real matches of $subject where one subtle positioning or timing decision flipped a losing match into a win.\n\nVisual Layer:\n• Freeze-frame tactical callouts with neon purple arrows.\n• Side-by-side comparison of amateur vs. optimized play.",
                        metadataHint = "Strong subscriber conversion"
                    ),
                    GeneratedItem(
                        id = "gvi_3_$seed",
                        categoryTag = "$styleFilter • Experiment",
                        title = "Hidden Mechanics & Myth Lab",
                        content = "Concept: Gather the 10 most debated community rumors around $subject and test each one systematically with a scoreboard.\n\nHook Idea:\n• Open on the most surprising myth immediately in the first 8 seconds.",
                        metadataHint = "Great comment engagement"
                    ),
                    GeneratedItem(
                        id = "gvi_4_$seed",
                        categoryTag = "$styleFilter • Entertainment",
                        title = "Random Wheel Loadout Roulette",
                        content = "Concept: Every round in $subject, spin a custom constraint wheel ($styleFilter edition) that locks your playstyle, sensitivity, or item choices.\n\nEditing Tip:\n• Keep transitions snappy with sound-design risers before each spin.",
                        metadataHint = "Series-friendly repeatable format"
                    ),
                    GeneratedItem(
                        id = "gvi_5_$seed",
                        categoryTag = "$styleFilter • Documentary",
                        title = "The Untold Evolution of $subject",
                        content = "Concept: Trace how $subject evolved from early metas to today's $styleFilter strategies, highlighting iconic community moments and forgotten tactics.",
                        metadataHint = "Bingeable long-form storytelling"
                    )
                )
                rotateList(templates, seed + topic.length, 4)
            }

            GamingToolType.HASHTAGS -> {
                val baseSlug = keywords.joinToString("") { it }
                val firstWord = keywords.firstOrNull() ?: "gaming"
                val secondWord = keywords.getOrElse(1) { "clips" }
                val styleSlug = styleFilter.lowercase(Locale.US).replace(Regex("[^a-z0-9]"), "")

                val sets = listOf(
                    GeneratedItem(
                        id = "gh_1_$seed",
                        categoryTag = "Broad + Niche Mix",
                        title = "Balanced Discovery Pack (12 Tags)",
                        content = "#$baseSlug #${firstWord}gaming #${firstWord}${secondWord} #$styleSlug #gamingcommunity #gamerlife #gameplayclips #proplayer #gamingvideos #contentcreator #gaminghighlights #sncreativity",
                        metadataHint = "Ideal for YouTube Long-Form & Instagram"
                    ),
                    GeneratedItem(
                        id = "gh_2_$seed",
                        categoryTag = "Short-Form Velocity",
                        title = "Compact Shorts / Reels / TikTok Pack (6 Tags)",
                        content = "#$baseSlug #${firstWord}clips #gaming #shorts #${firstWord}tips #$styleSlug",
                        metadataHint = "Clean 5–6 tag cluster for vertical feeds"
                    ),
                    GeneratedItem(
                        id = "gh_3_$seed",
                        categoryTag = "Search & Tutorial",
                        title = "High-Intent Guide & Loadout Tags",
                        content = "#${firstWord}guide #${firstWord}meta #${baseSlug}tips #howtoplay${firstWord} #gamingtutorial #esportsready #competitivegaming #${secondWord}setup",
                        metadataHint = "Targets players actively searching tutorials"
                    ),
                    GeneratedItem(
                        id = "gh_4_$seed",
                        categoryTag = "Montage & Hype",
                        title = "Clutch & Highlight Reel Tags",
                        content = "#${firstWord}montage #${baseSlug}clutch #insaneplays #gamingmoments #epicgaming #streamhighlights #1v5clutch #$primaryTag",
                        metadataHint = "Best for fast-cut montage uploads"
                    )
                )
                rotateList(sets, seed, 4)
            }

            GamingToolType.SHORT_FORM -> {
                val templates = listOf(
                    GeneratedItem(
                        id = "gsf_1_$seed",
                        categoryTag = "0–30s Vertical Loop",
                        title = "Seamless Loop Secret Tip",
                        content = "Hook (0–2s): \"99% of players miss this hidden $subject spot…\"\nBody (3–22s): Show the exact movement input in 3 crisp steps with bold on-screen captions.\nLoop Ending (23–28s): \"…and that is the real reason—\" (cuts seamlessly back into the opening line).",
                        metadataHint = "Boosts replay percentage > 115%"
                    ),
                    GeneratedItem(
                        id = "gsf_2_$seed",
                        categoryTag = "Split-Screen Reaction",
                        title = "Expectation vs. Reality Clutch",
                        content = "Hook (0–2s): Flash the chaotic final 1.5 seconds of a $subject play.\nBody (3–18s): Rewind with a calm voiceover explaining what went through your head in slow motion, then unleash full-speed audio for the win.",
                        metadataHint = "Strong emotional spike"
                    ),
                    GeneratedItem(
                        id = "gsf_3_$seed",
                        categoryTag = "3-Second Challenge",
                        title = "Pick Your Loadout Quiz Short",
                        content = "Hook (0–3s): \"Pause right now—which $subject setup wins this 1v1?\"\nBody (4–20s): Play out Option A vs. Option B side-by-side in $styleFilter mode.\nCTA: \"Comment if you guessed right before the timer hit zero.\"",
                        metadataHint = "Drives rapid comments & pauses"
                    ),
                    GeneratedItem(
                        id = "gsf_4_$seed",
                        categoryTag = "Rapid Tier List",
                        title = "15-Second Brutally Honest Ranking",
                        content = "Hook (0–2s): \"Ranking the top 4 $subject choices in 15 seconds—no filler.\"\nBody (3–15s): Snap-zoom transitions on each pick (#4 to #1) with one controversial placement at #2 to spark healthy debate.",
                        metadataHint = "High share & debate trigger"
                    )
                )
                rotateList(templates, seed + 3, 4)
            }
        }
    }

    override fun generateVoiceScriptContent(
        toolType: VoiceScriptToolType,
        topic: String,
        duration: ScriptDuration,
        tone: ScriptTone,
        seed: Int
    ): List<GeneratedItem> {
        val subject = cleanTopic(topic)
        val tonePrefix = when (tone) {
            ScriptTone.HYPE -> "[Pace: Fast & Punchy | Energy: 9/10]"
            ScriptTone.CINEMATIC -> "[Pace: Deliberate & Atmospheric | Pause on beats]"
            ScriptTone.EDUCATIONAL -> "[Pace: Clear & Confident | Studio Authority]"
            ScriptTone.WITTY -> "[Pace: Conversational & Sharp | Dry comedic timing]"
        }

        return when (toolType) {
            VoiceScriptToolType.VOICE_OVER -> {
                val scripts = listOf(
                    GeneratedItem(
                        id = "vo_1_$seed",
                        categoryTag = "${duration.label} • ${tone.label}",
                        title = "Problem-Twist-Solution Voice-Over",
                        content = buildVoiceOverScriptA(subject, duration, tone, tonePrefix),
                        metadataHint = "Target length: ${duration.secondsEstimate}"
                    ),
                    GeneratedItem(
                        id = "vo_2_$seed",
                        categoryTag = "${duration.label} • ${tone.label}",
                        title = "Story-Driven Studio Narration",
                        content = buildVoiceOverScriptB(subject, duration, tone, tonePrefix),
                        metadataHint = "Includes B-roll & audio cue markers"
                    ),
                    GeneratedItem(
                        id = "vo_3_$seed",
                        categoryTag = "${duration.label} • ${tone.label}",
                        title = "Direct Authority Breakdown",
                        content = buildVoiceOverScriptC(subject, duration, tone, tonePrefix),
                        metadataHint = "Clean teleprompter-ready phrasing"
                    )
                )
                rotateList(scripts, seed + subject.hashCode(), 3)
            }

            VoiceScriptToolType.SHORT_VIDEO -> {
                val scripts = listOf(
                    GeneratedItem(
                        id = "svs_1_$seed",
                        categoryTag = "${duration.label} Vertical • ${tone.label}",
                        title = "3-Beat Retention Short Script",
                        content = "$tonePrefix\n\n[0:00 - HOOK] Stop scrolling if you care about $subject—because one tiny tweak changes everything.\n[0:04 - VISUAL SHIFT] Most people overcomplicate the setup and wonder why their results stall.\n[0:11 - CORE VALUE] Focus on clean execution first: strip away the distractions, lock in one repeatable workflow for $subject, and test it three times.\n[END BEAT] Watch what happens when you compare before and after.",
                        metadataHint = "Optimized for Reels, Shorts & TikTok captions"
                    ),
                    GeneratedItem(
                        id = "svs_2_$seed",
                        categoryTag = "${duration.label} Vertical • ${tone.label}",
                        title = "Myth vs. Reality Rapid Cut",
                        content = "$tonePrefix\n\n[SHOT 1 - CLOSE UP] Everyone tells you that mastering $subject takes expensive gear.\n[SHOT 2 - WHIP PAN] That's completely backwards.\n[SHOT 3 - DEMO] Here is the exact 3-step blueprint: start with clarity, nail the first three seconds of attention, and deliver one unforgettable takeaway on $subject.\n[LOOP OUTRO] Save this before your next session so you don't forget step two.",
                        metadataHint = "Built for quick 1.5s visual cuts"
                    ),
                    GeneratedItem(
                        id = "svs_3_$seed",
                        categoryTag = "${duration.label} Vertical • ${tone.label}",
                        title = "Countdown Reveal Script",
                        content = "$tonePrefix\n\n[0:00] Three things about $subject I wish I knew on day one—number one is a total game changer.\n[0:04] #3: Consistency beats marathon burnout.\n[0:09] #2: Small polish details separate amateurs from pros.\n[0:15] #1: Master the core fundamentals of $subject before chasing trends.",
                        metadataHint = "Numbered structure holds viewer curiosity to #1"
                    )
                )
                rotateList(scripts, seed, 3)
            }

            VoiceScriptToolType.INTRO_HOOK -> {
                val hooks = listOf(
                    GeneratedItem(
                        id = "ih_1_$seed",
                        categoryTag = "${tone.label} • Pattern Interrupt",
                        title = "Contrarian Opening Hook",
                        content = "\"Everything you've been told about $subject is missing one critical detail—and in the next ${duration.label}, I'm going to prove it side by side.\"",
                        metadataHint = "First 5-second retention anchor"
                    ),
                    GeneratedItem(
                        id = "ih_2_$seed",
                        categoryTag = "${tone.label} • High Stakes",
                        title = "In-Media-Res Hook",
                        content = "\"This single mistake in $subject almost ruined the entire project—until we discovered a smarter way to flip the outcome.\"",
                        metadataHint = "Opens directly inside the tension"
                    ),
                    GeneratedItem(
                        id = "ih_3_$seed",
                        categoryTag = "${tone.label} • Value Promise",
                        title = "Zero-Fluff Fast Value Hook",
                        content = "\"No 10-minute intro, no filler—here is the exact step-by-step system for $subject that works right now.\"",
                        metadataHint = "Builds immediate viewer trust"
                    ),
                    GeneratedItem(
                        id = "ih_4_$seed",
                        categoryTag = "${tone.label} • Visual Proof",
                        title = "Before & After Tease Hook",
                        content = "\"Look at the difference between attempt one and attempt ten of $subject. Here is the exact adjustment that made it click.\"",
                        metadataHint = "Pair with split-screen visual"
                    )
                )
                rotateList(hooks, seed + 2, 4)
            }

            VoiceScriptToolType.CTA_GENERATOR -> {
                val ctas = listOf(
                    GeneratedItem(
                        id = "cta_1_$seed",
                        categoryTag = "${tone.label} • Seamless Subscribe",
                        title = "Value-Linked Subscribe CTA",
                        content = "\"If this $subject breakdown saved you hours of trial and error, hit subscribe—next week we're pushing this workflow even further.\"",
                        metadataHint = "Natural transition without killing retention"
                    ),
                    GeneratedItem(
                        id = "cta_2_$seed",
                        categoryTag = "${tone.label} • Comment Trigger",
                        title = "Specific Question Comment CTA",
                        content = "\"Drop a comment below: which part of $subject are you testing next, Option A or Option B? I'm replying to the sharpest takes.\"",
                        metadataHint = "Sparks meaningful comment threads"
                    ),
                    GeneratedItem(
                        id = "cta_3_$seed",
                        categoryTag = "${tone.label} • Binge Bridge",
                        title = "End-Screen Bridge CTA",
                        content = "\"Now that you've locked in $subject, tap the video on screen right now because it covers the exact mistake that trips people up next.\"",
                        metadataHint = "Maximizes session watch time"
                    ),
                    GeneratedItem(
                        id = "cta_4_$seed",
                        categoryTag = "${tone.label} • Save & Share",
                        title = "Bookmark Utility CTA",
                        content = "\"Save this $subject checklist to your favorites right now so it's ready the moment you open your studio.\"",
                        metadataHint = "High save-to-view signal for short-form"
                    )
                )
                rotateList(ctas, seed + 5, 4)
            }
        }
    }

    private fun buildVoiceOverScriptA(
        subject: String,
        duration: ScriptDuration,
        tone: ScriptTone,
        tonePrefix: String
    ): String {
        val opener = when (tone) {
            ScriptTone.HYPE -> "Let's be real—when you're diving into $subject, average advice gets average results!"
            ScriptTone.CINEMATIC -> "Every great breakthrough in $subject starts the exact same way: a blank canvas and a bold question."
            ScriptTone.EDUCATIONAL -> "If you want predictable, repeatable results with $subject, you need a system rather than guesswork."
            ScriptTone.WITTY -> "We've all stared at $subject wondering why the tutorial looked ten times easier than real life."
        }
        val expansion = when (duration) {
            ScriptDuration.SHORT_15 -> "Focus on the core lever first, cut the extra noise, and execute cleanly."
            ScriptDuration.MEDIUM_30 -> "First, isolate the single highest-impact variable in $subject. Second, test it under real conditions without changing five settings at once. That clarity is where momentum happens."
            ScriptDuration.STANDARD_60 -> "Step one: strip away unnecessary complexity around $subject so your core idea stands out immediately. Step two: build visual and audio rhythm every five to seven seconds so attention never drops. Step three: measure what actually moved the needle and double down on your strongest angle."
            ScriptDuration.LONG_180 -> "Act I (0:00–0:45): Establish why $subject matters right now and preview the transformation.\nAct II (0:45–1:50): Walk through the 3 foundational pillars—preparation, execution, and refinement—with concrete examples and live demonstrations.\nAct III (1:50–3:00): Reveal the advanced optimization most people overlook, recap the action plan, and transition directly into your next recommended deep dive."
        }
        return "$tonePrefix\n\nVO: $opener\n\n[SFX: Subtle Low Sub Drop]\nVO: $expansion\n\nVO (Outro): Master that rhythm, and $subject goes from frustrating to effortless."
    }

    private fun buildVoiceOverScriptB(
        subject: String,
        duration: ScriptDuration,
        tone: ScriptTone,
        tonePrefix: String
    ): String {
        return "$tonePrefix\n\n[B-ROLL: Tight macro shot with purple rim lighting]\nVO: Behind every standout result in $subject is a detail nobody notices at first glance.\n\n[B-ROLL: Fast cut to workspace / gameplay timeline]\nVO: Instead of rushing the opening, anchor the viewer's curiosity in the first three seconds, deliver crisp value tailored to a ${duration.label} runtime, and keep your ${tone.label.lowercase(Locale.US)} energy consistent from first frame to final cut.\n\nVO: Dial this in once, and every future project levels up."
    }

    private fun buildVoiceOverScriptC(
        subject: String,
        duration: ScriptDuration,
        tone: ScriptTone,
        tonePrefix: String
    ): String {
        return "$tonePrefix\n\nVO: Here is your ${duration.label} studio gameplan for $subject.\n1. Lead with your sharpest visual proof.\n2. Explain the 'why' in plain, punchy language.\n3. Close with one unmistakable takeaway viewers can apply today.\n\n[Delivery Note: Emphasize key nouns and leave a 0.4s breath before point #3.]"
    }

    override fun generateThumbnailPrompts(
        request: ThumbnailPromptRequest,
        seed: Int
    ): List<GeneratedItem> {
        val subject = cleanTopic(request.topic)
        val ratio = request.aspectRatio.ratioCode
        val framingNote = if (request.aspectRatio == ThumbnailAspectRatio.VERTICAL_9_16) {
            "vertical 9:16 portrait framing optimized for Shorts/Reels cover safe zones, subject centered in middle-third"
        } else {
            "widescreen 16:9 YouTube thumbnail framing, bold focal hierarchy, clean negative space on one side for headline readability"
        }
        val textDirective = if (request.textOverlayInstruction.isBlank()) {
            "leave clean negative space for a 2–3 word bold headline overlay, no cluttered background text"
        } else {
            "include crisp, high-contrast 3D typographic text reading \"${request.textOverlayInstruction.trim()}\" with subtle glow separation"
        }
        val categoryFlavor = if (request.category == ThumbnailCategory.GAMING) {
            "dynamic esports/gaming energy, dramatic action depth, particle sparks, high-contrast rim highlights, ultra-sharp focal subject"
        } else {
            "flagship creator studio aesthetic, editorial depth of field, clean luxury surface reflections, commercial-grade color separation"
        }

        val variants = listOf(
            GeneratedItem(
                id = "tp_1_$seed",
                categoryTag = "${request.category.label} • $ratio",
                title = "Hero Spotlight Blueprint",
                content = "High-impact ${request.category.label.lowercase(Locale.US)} thumbnail featuring $subject, rendered in ${request.visualStyle} style. Composition: ${request.composition}, $framingNote. Lighting: ${request.lighting}, dramatic volumetric contrast, $categoryFlavor. Typography & layout: $textDirective. Ultra-clean edges, 8k studio clarity, instant scroll-stopping contrast --ar $ratio",
                metadataHint = "Copy-ready master prompt ($ratio)"
            ),
            GeneratedItem(
                id = "tp_2_$seed",
                categoryTag = "Split Contrast • $ratio",
                title = "Before vs. After / Dramatic Versus Concept",
                content = "Dynamic split-diopter thumbnail design about $subject in ${request.visualStyle} aesthetic. Left side shows understated muted starting state; right side explodes with vibrant ${request.lighting} and upgraded hero detail. Composition: ${request.composition}, $framingNote, $categoryFlavor. Layout instruction: $textDirective. Razor-sharp separation between foreground and background --ar $ratio",
                metadataHint = "High-curiosity comparison layout"
            ),
            GeneratedItem(
                id = "tp_3_$seed",
                categoryTag = "Macro Focal • $ratio",
                title = "Extreme Close-Up Curiosity Frame",
                content = "Cinematic macro hero shot centered on $subject, styled as ${request.visualStyle} with intense ${request.lighting} and glossy specular reflections. Framing: $framingNote, ${request.composition}, shallow f/1.8 depth of field blurring background distractions, $categoryFlavor. Text instruction: $textDirective. Vibrant color grading, unmistakable silhouette at small mobile preview size --ar $ratio",
                metadataHint = "Designed for small mobile screen legibility"
            )
        )
        return rotateList(variants, seed + subject.hashCode(), 3)
    }

    override fun generateToolkitContent(
        toolType: ToolkitToolType,
        topic: String,
        platformFocus: String,
        seed: Int
    ): List<GeneratedItem> {
        val subject = cleanTopic(topic)
        val titleSubject = titleCaseTopic(topic)
        val keywords = extractKeywords(topic)
        val mainTag = keywords.joinToString("")
        val primaryWord = keywords.firstOrNull() ?: "creator"

        return when (toolType) {
            ToolkitToolType.HASHTAGS -> {
                val packs = listOf(
                    GeneratedItem(
                        id = "tk_h1_$seed",
                        categoryTag = "$platformFocus • Tiered Mix",
                        title = "Niche + Mid-Volume Discovery Tags",
                        content = "#$mainTag #${primaryWord}tips #${primaryWord}community #contentcreator #creatoreconomy #digitalcreator #studioflow #creativeprocess #viralstrategy #sncreativity",
                        metadataHint = "Balanced reach + topical relevance"
                    ),
                    GeneratedItem(
                        id = "tk_h2_$seed",
                        categoryTag = "$platformFocus • Minimalist",
                        title = "Algorithm-Focused 5-Tag Set",
                        content = "#$mainTag #${primaryWord} #creatorstudio #contenttips #${platformFocus.lowercase(Locale.US).replace(" ", "")}",
                        metadataHint = "Clean caption footer without visual clutter"
                    ),
                    GeneratedItem(
                        id = "tk_h3_$seed",
                        categoryTag = "$platformFocus • Community",
                        title = "Behind-the-Scenes & Workflow Tags",
                        content = "#buildinpublic #$mainTag #creatorsetup #editingworkflow #storytelling #creativelife #dailycreation #contentplanning",
                        metadataHint = "Great for studio updates & reels"
                    )
                )
                rotateList(packs, seed, 3)
            }

            ToolkitToolType.CAPTIONS -> {
                val captions = listOf(
                    GeneratedItem(
                        id = "tk_c1_$seed",
                        categoryTag = "$platformFocus • Hook + Value",
                        title = "Scroll-Stopping Micro-Story Caption",
                        content = "Most people only see the finished upload—not the 14 small iterations behind $subject.\n\nHere's what actually made the difference today:\n• Focused on clarity over complexity\n• Trimmed every second of dead air\n• Let the core visual speak first\n\nWhich step are you dialing in this week?",
                        metadataHint = "High read-more expansion rate"
                    ),
                    GeneratedItem(
                        id = "tk_c2_$seed",
                        categoryTag = "$platformFocus • Punchy One-Liner",
                        title = "Minimal Aesthetic Caption",
                        content = "Quiet focus. Sharp execution. Turning $subject into something unforgettable. ✨\n\nDrop a 🔥 if you're building something new tonight.",
                        metadataHint = "Fast-reading visual-first caption"
                    ),
                    GeneratedItem(
                        id = "tk_c3_$seed",
                        categoryTag = "$platformFocus • Saveable Tip",
                        title = "Bookmark-Worthy Mini Guide",
                        content = "Save this before your next $subject session:\n1. Start with a bold 3-second visual hook.\n2. Keep one dominant color mood.\n3. End with a clear reason to comment or rewatch.\n\nSimple rules, massive upgrade.",
                        metadataHint = "Encourages saves & shares"
                    )
                )
                rotateList(captions, seed + 1, 3)
            }

            ToolkitToolType.DESCRIPTIONS -> {
                val descriptions = listOf(
                    GeneratedItem(
                        id = "tk_d1_$seed",
                        categoryTag = "$platformFocus • SEO Structured",
                        title = "Complete Video Description Template",
                        content = "In this video, we break down $titleSubject step by step—from initial concept to polished final execution so you can apply the exact workflow immediately.\n\n⏱️ Chapters:\n00:00 — Why $titleSubject Matters\n01:15 — Core Setup & Essential Adjustments\n03:40 — Common Mistakes to Avoid\n06:10 — Pro Optimization & Final Reveal\n\n💡 Key Takeaway:\nMastering $subject comes down to intentional pacing, clean presentation, and repeatable habits.\n\n#$mainTag #${primaryWord} #CreatorToolkit",
                        metadataHint = "Includes search summary + timestamp skeleton"
                    ),
                    GeneratedItem(
                        id = "tk_d2_$seed",
                        categoryTag = "$platformFocus • Concise",
                        title = "Short-Form / Podcast Show Notes",
                        content = "Everything you need to know about $titleSubject in one streamlined breakdown. We cover practical setup choices, real-world testing, and the fastest way to elevate your output without extra fluff.",
                        metadataHint = "Clean above-the-fold summary"
                    ),
                    GeneratedItem(
                        id = "tk_d3_$seed",
                        categoryTag = "$platformFocus • Series Episode",
                        title = "Episode / Series Description",
                        content = "Welcome back to the studio! Today's episode dives deep into $titleSubject—exploring what works, what fails under pressure, and how to customize this approach for your own creative style.\n\nLet me know in the comments what topic we should tackle in Episode Next!",
                        metadataHint = "Designed for recurring series uploads"
                    )
                )
                rotateList(descriptions, seed + 2, 3)
            }

            ToolkitToolType.CREATIVE_PROMPTS -> {
                val prompts = listOf(
                    GeneratedItem(
                        id = "tk_cp1_$seed",
                        categoryTag = "$platformFocus • Visual Direction",
                        title = "Futuristic Studio Concept Prompt",
                        content = "Design a cohesive creative piece around \"$subject\" using deep obsidian shadows, neon-violet key lighting, floating geometric accents, and tactile glass textures. Contrast sleek modern minimalism with one bold energetic focal element.",
                        metadataHint = "Great for moodboards, B-roll & art direction"
                    ),
                    GeneratedItem(
                        id = "tk_cp2_$seed",
                        categoryTag = "$platformFocus • Storytelling Challenge",
                        title = "60-Second Narrative Constraint Prompt",
                        content = "Explain or showcase \"$subject\" to a complete newcomer in 60 seconds using only 3 distinct camera angles: one wide establishing shot, one extreme close-up detail shot, and one dynamic POV action shot.",
                        metadataHint = "Sharpens filming & editing discipline"
                    ),
                    GeneratedItem(
                        id = "tk_cp3_$seed",
                        categoryTag = "$platformFocus • Remix Angle",
                        title = "Unexpected Perspective Remix",
                        content = "Flip the standard approach to \"$subject\": start with the finished result in frame one, then tell the story backwards through the three hardest decisions that shaped it.",
                        metadataHint = "Breaks repetitive upload formulas"
                    )
                )
                rotateList(prompts, seed + 3, 3)
            }

            ToolkitToolType.CONTENT_PLANNING -> {
                val plans = listOf(
                    GeneratedItem(
                        id = "tk_pl1_$seed",
                        categoryTag = "$platformFocus • 7-Day Sprint",
                        title = "One-Topic Multi-Format Week Plan",
                        content = "• Mon (Hero Pillar): Deep-dive breakdown on $titleSubject.\n• Wed (Short #1): 25s myth-buster extracted from your best insight.\n• Fri (Short #2): Behind-the-scenes mistake & fix during $subject.\n• Sun (Community Post): Two-option poll asking followers which $primaryWord experiment comes next.",
                        metadataHint = "Turns 1 core idea into 4 touchpoints"
                    ),
                    GeneratedItem(
                        id = "tk_pl2_$seed",
                        categoryTag = "$platformFocus • 3-Part Funnel",
                        title = "Discovery → Trust → Fanbase Trilogy",
                        content = "Part 1 (Broad Appeal): \"I Tested the Most Popular $titleSubject Method\"\nPart 2 (Actionable Depth): \"The Complete Step-by-Step $titleSubject Blueprint\"\nPart 3 (Advanced Mastery): \"3 Subtle Upgrades That Take $titleSubject to Pro Level\"",
                        metadataHint = "Structured 3-upload series arc"
                    ),
                    GeneratedItem(
                        id = "tk_pl3_$seed",
                        categoryTag = "$platformFocus • Batch Session",
                        title = "2-Hour Studio Batching Checklist",
                        content = "1. Research & outline 5 hooks for $titleSubject (20 min).\n2. Record A-roll voice/camera in one lighting setup (45 min).\n3. Capture 8 crisp B-roll detail clips of $subject (25 min).\n4. Draft thumbnail headline + 2 caption variants before closing session (30 min).",
                        metadataHint = "Eliminates context-switching fatigue"
                    )
                )
                rotateList(plans, seed + 4, 3)
            }
        }
    }
}
