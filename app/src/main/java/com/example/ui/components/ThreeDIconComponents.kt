package com.example.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.PhotoSizeSelectActual
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.domain.StudioSection
import com.example.ui.theme.LocalStudioColors
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin
import kotlinx.coroutines.launch

enum class Icon3DPalette(
    val topStart: Color,
    val bottomEnd: Color,
    val rimLight: Color,
    val baseShadow: Color
) {
    GAMING(
        topStart = Color(0xFFB45CFF),
        bottomEnd = Color(0xFF5D24D6),
        rimLight = Color(0xFFF3B8FF),
        baseShadow = Color(0xFF331080)
    ),
    VOICE_SCRIPT(
        topStart = Color(0xFFFF58C6),
        bottomEnd = Color(0xFF8C2CE6),
        rimLight = Color(0xFFFFC4EC),
        baseShadow = Color(0xFF4D1185)
    ),
    THUMBNAIL(
        topStart = Color(0xFF7B6BFF),
        bottomEnd = Color(0xFF3B28BA),
        rimLight = Color(0xFFC4DAFF),
        baseShadow = Color(0xFF1F1370)
    ),
    TOOLKIT(
        topStart = Color(0xFFD36BFF),
        bottomEnd = Color(0xFF7B2CBF),
        rimLight = Color(0xFFFFD6FF),
        baseShadow = Color(0xFF431273)
    ),
    ACTION(
        topStart = Color(0xFFA855F7),
        bottomEnd = Color(0xFF6B21A8),
        rimLight = Color(0xFFE9D5FF),
        baseShadow = Color(0xFF3B0764)
    )
}

fun StudioSection.toIconPalette(): Icon3DPalette = when (this) {
    StudioSection.GAMING_STUDIO -> Icon3DPalette.GAMING
    StudioSection.VOICE_SCRIPT_STUDIO -> Icon3DPalette.VOICE_SCRIPT
    StudioSection.THUMBNAIL_PROMPT_STUDIO -> Icon3DPalette.THUMBNAIL
    StudioSection.CREATOR_TOOLKIT -> Icon3DPalette.TOOLKIT
}

fun StudioSection.toGlyph(): ImageVector = when (this) {
    StudioSection.GAMING_STUDIO -> Icons.Filled.SportsEsports
    StudioSection.VOICE_SCRIPT_STUDIO -> Icons.Filled.GraphicEq
    StudioSection.THUMBNAIL_PROMPT_STUDIO -> Icons.Filled.PhotoSizeSelectActual
    StudioSection.CREATOR_TOOLKIT -> Icons.Filled.AutoAwesome
}

/**
 * Interactive 3D-inspired sculpted icon badge with genuine Compose touch physics:
 * - Press & click trigger spring-based 3D rotationX / rotationY tilt, scale pop, and neon halo pulse.
 * - Multi-layered isometric bevel base, specular glass top highlight, and crisp foreground glyph.
 */
@Composable
fun Interactive3DIconBadge(
    icon: ImageVector,
    contentDescription: String?,
    palette: Icon3DPalette,
    modifier: Modifier = Modifier,
    size: Dp = 58.dp,
    onClick: (() -> Unit)? = null,
    testTag: String? = null
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scope = rememberCoroutineScope()
    val clickSpinAnim = remember { Animatable(0f) }

    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.90f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "icon3d_scale"
    )

    val tiltX by animateFloatAsState(
        targetValue = if (isPressed) 16f else 0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "icon3d_tilt_x"
    )

    val tiltY by animateFloatAsState(
        targetValue = if (isPressed) -18f else 0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "icon3d_tilt_y"
    )

    val glowAlpha by animateFloatAsState(
        targetValue = if (isPressed) 0.72f else 0.34f,
        animationSpec = tween(durationMillis = 180),
        label = "icon3d_glow"
    )

    val density = LocalDensity.current.density
    val shape = RoundedCornerShape(size * 0.30f)

    val clickableModifier = if (onClick != null) {
        Modifier
            .minimumInteractiveComponentSize()
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = {
                    scope.launch {
                        clickSpinAnim.snapTo(0f)
                        clickSpinAnim.animateTo(
                            targetValue = 1f,
                            animationSpec = tween(durationMillis = 360, easing = FastOutSlowInEasing)
                        )
                        clickSpinAnim.snapTo(0f)
                    }
                    onClick()
                }
            )
    } else {
        Modifier
    }

    val pulseAngle = sin(clickSpinAnim.value * PI.toFloat()) * 20f
    val pulseScale = 1f + sin(clickSpinAnim.value * PI.toFloat()) * 0.12f

    Box(
        modifier = modifier
            .then(if (testTag != null) Modifier.testTag(testTag) else Modifier)
            .size(size)
            .then(clickableModifier)
            .graphicsLayer {
                scaleX = scale * pulseScale
                scaleY = scale * pulseScale
                rotationX = tiltX - pulseAngle * 0.4f
                rotationY = tiltY + pulseAngle
                cameraDistance = 12f * density
            }
            .drawBehind {
                // Soft outer neon glow + 3D extrusion base shadow
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            palette.topStart.copy(alpha = glowAlpha),
                            Color.Transparent
                        ),
                        radius = this.size.minDimension * 0.78f
                    ),
                    radius = this.size.minDimension * 0.78f
                )
                val corner = this.size.minDimension * 0.30f
                drawRoundRect(
                    color = palette.baseShadow,
                    topLeft = Offset(0f, this.size.height * 0.09f),
                    size = Size(this.size.width, this.size.height * 0.94f),
                    cornerRadius = CornerRadius(corner, corner)
                )
            },
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 3.dp)
                .clip(shape)
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(palette.topStart, palette.bottomEnd),
                        start = Offset.Zero,
                        end = Offset(180f, 180f)
                    )
                )
                .border(
                    width = 1.5.dp,
                    brush = Brush.linearGradient(
                        colors = listOf(
                            palette.rimLight.copy(alpha = 0.9f),
                            palette.bottomEnd.copy(alpha = 0.3f)
                        )
                    ),
                    shape = shape
                )
                .drawBehind {
                    // Glossy 3D top specular highlight arc
                    drawRoundRect(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color.White.copy(alpha = 0.34f),
                                Color.Transparent
                            ),
                            startY = 0f,
                            endY = this.size.height * 0.52f
                        ),
                        topLeft = Offset(this.size.width * 0.08f, this.size.height * 0.06f),
                        size = Size(this.size.width * 0.84f, this.size.height * 0.45f),
                        cornerRadius = CornerRadius(this.size.minDimension * 0.24f)
                    )
                },
            contentAlignment = Alignment.Center
        ) {
            // Subtle extruded shadow behind icon vector for 3D depth
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = Color.Black.copy(alpha = 0.36f),
                modifier = Modifier
                    .size(size * 0.48f)
                    .graphicsLayer {
                        translationY = 4f
                        translationX = 2f
                    }
            )
            Icon(
                imageVector = icon,
                contentDescription = contentDescription,
                tint = Color.White,
                modifier = Modifier.size(size * 0.48f)
            )
        }
    }
}

/**
 * Compact 3D action button icon (for Settings, Copy, Shuffle/Generate accents)
 * with tactile tilt, scale bounce, and neon rim illumination.
 */
@Composable
fun Interactive3DActionIconButton(
    icon: ImageVector,
    contentDescription: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    testTag: String? = null
) {
    Interactive3DIconBadge(
        icon = icon,
        contentDescription = contentDescription,
        palette = Icon3DPalette.ACTION,
        size = 48.dp,
        onClick = onClick,
        modifier = modifier,
        testTag = testTag
    )
}

/**
 * True perspective-projected continuously rotating 3D geometric prism & emblem
 * with dynamic face shading, specular lighting, orbital neon ring, and SN monogram.
 */
@Composable
fun Rotating3DSNCubeEmblem(
    modifier: Modifier = Modifier,
    emblemSize: Dp = 180.dp
) {
    val studioColors = LocalStudioColors.current
    val infiniteTransition = rememberInfiniteTransition(label = "sn_3d_logo_rotation")

    val rotationAngle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 7200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "y_axis_spin"
    )

    val bobOffset by infiniteTransition.animateFloat(
        initialValue = -6f,
        targetValue = 6f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "vertical_float"
    )

    val glowPulse by infiniteTransition.animateFloat(
        initialValue = 0.65f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "halo_pulse"
    )

    val density = LocalDensity.current.density

    Box(
        modifier = modifier.size(emblemSize),
        contentAlignment = Alignment.Center
    ) {
        // Real-time 3D perspective octa-prism / crystal wireframe + shaded faces on Canvas
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer {
                    translationY = bobOffset
                }
        ) {
            val cx = size.width / 2f
            val cy = size.height / 2f
            val radius = size.minDimension * 0.34f

            // Ambient neon-purple radial aura
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        Color(0xFFC06CFF).copy(alpha = 0.42f * glowPulse),
                        Color(0xFF7B2CBF).copy(alpha = 0.16f * glowPulse),
                        Color.Transparent
                    ),
                    center = Offset(cx, cy),
                    radius = size.minDimension * 0.50f
                ),
                radius = size.minDimension * 0.50f,
                center = Offset(cx, cy)
            )

            // Outer orbital energy ring
            drawOval(
                brush = Brush.sweepGradient(
                    colors = listOf(
                        Color(0x00C06CFF),
                        Color(0xFFC06CFF),
                        Color(0xFFFF59C7),
                        Color(0x00C06CFF)
                    ),
                    center = Offset(cx, cy)
                ),
                topLeft = Offset(cx - radius * 1.28f, cy - radius * 0.46f),
                size = Size(radius * 2.56f, radius * 0.92f),
                style = Stroke(width = 3.dp.toPx(), cap = StrokeCap.Round)
            )

            // 3D Cube/Crystal vertices rotated around Y and tilted on X
            val radY = (rotationAngle * PI / 180.0).toFloat()
            val radX = (22.0 * PI / 180.0).toFloat()
            val cosY = cos(radY)
            val sinY = sin(radY)
            val cosX = cos(radX)
            val sinX = sin(radX)

            val rawVertices = arrayOf(
                floatArrayOf(-1f, -1f, -1f),
                floatArrayOf(1f, -1f, -1f),
                floatArrayOf(1f, 1f, -1f),
                floatArrayOf(-1f, 1f, -1f),
                floatArrayOf(-1f, -1f, 1f),
                floatArrayOf(1f, -1f, 1f),
                floatArrayOf(1f, 1f, 1f),
                floatArrayOf(-1f, 1f, 1f)
            )

            val projected = Array(8) { Offset.Zero }
            val rotatedZ = FloatArray(8)
            val rotatedVerts = Array(8) { FloatArray(3) }

            for (i in rawVertices.indices) {
                val vx = rawVertices[i][0]
                val vy = rawVertices[i][1]
                val vz = rawVertices[i][2]

                // Rotate around Y
                val x1 = vx * cosY + vz * sinY
                val z1 = -vx * sinY + vz * cosY

                // Tilt around X
                val y2 = vy * cosX - z1 * sinX
                val z2 = vy * sinX + z1 * cosX

                rotatedVerts[i][0] = x1
                rotatedVerts[i][1] = y2
                rotatedVerts[i][2] = z2

                val perspective = 3.6f / (3.6f - z2 * 0.55f)
                projected[i] = Offset(
                    x = cx + x1 * radius * 0.58f * perspective,
                    y = cy + y2 * radius * 0.58f * perspective
                )
                rotatedZ[i] = z2
            }

            val faces = arrayOf(
                intArrayOf(4, 5, 6, 7), // Front
                intArrayOf(1, 0, 3, 2), // Back
                intArrayOf(5, 1, 2, 6), // Right
                intArrayOf(0, 4, 7, 3), // Left
                intArrayOf(3, 7, 6, 2), // Top
                intArrayOf(0, 1, 5, 4)  // Bottom
            )

            // Painter's algorithm sort back-to-front by average Z
            val sortedFaces = faces.sortedBy { face ->
                (rotatedZ[face[0]] + rotatedZ[face[1]] + rotatedZ[face[2]] + rotatedZ[face[3]]) / 4f
            }

            sortedFaces.forEach { face ->
                val avgZ = (rotatedZ[face[0]] + rotatedZ[face[1]] + rotatedZ[face[2]] + rotatedZ[face[3]]) / 4f
                // Dynamic lighting intensity based on face depth & orientation
                val lightFactor = ((avgZ + 1.4f) / 2.8f).coerceIn(0.18f, 1f)

                val path = Path().apply {
                    moveTo(projected[face[0]].x, projected[face[0]].y)
                    lineTo(projected[face[1]].x, projected[face[1]].y)
                    lineTo(projected[face[2]].x, projected[face[2]].y)
                    lineTo(projected[face[3]].x, projected[face[3]].y)
                    close()
                }

                val fillColor = Brush.linearGradient(
                    colors = listOf(
                        Color(0xFFC77DFF).copy(alpha = 0.22f + 0.48f * lightFactor),
                        Color(0xFF48168C).copy(alpha = 0.30f + 0.45f * lightFactor)
                    ),
                    start = projected[face[0]],
                    end = projected[face[2]]
                )

                drawPath(path = path, brush = fillColor)
                drawPath(
                    path = path,
                    color = Color(0xFFF3D1FF).copy(alpha = 0.35f + 0.55f * lightFactor),
                    style = Stroke(
                        width = 2.dp.toPx(),
                        cap = StrokeCap.Round,
                        join = StrokeJoin.Round
                    )
                )
            }
        }

        // Central 3D-tilted holographic SN badge suspended inside the rotating prism
        val badgeFlip = sin((rotationAngle * PI / 180.0).toFloat()) * 22f
        Box(
            modifier = Modifier
                .size(emblemSize * 0.36f)
                .graphicsLayer {
                    translationY = bobOffset
                    rotationY = badgeFlip
                    rotationX = 8f
                    cameraDistance = 14f * density
                }
                .clip(RoundedCornerShape(18.dp))
                .background(
                    Brush.linearGradient(
                        colors = listOf(
                            studioColors.accentGradientStart,
                            studioColors.accentGradientEnd
                        )
                    )
                )
                .border(
                    width = 1.5.dp,
                    color = Color.White.copy(alpha = 0.78f),
                    shape = RoundedCornerShape(18.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            androidx.compose.material3.Text(
                text = "SN",
                style = MaterialTheme.typography.headlineMedium,
                color = Color.White
            )
        }
    }
}

val QuickActionIcons = structIcons()

data class StudioActionIcons(
    val copy: ImageVector = Icons.Filled.ContentCopy,
    val shuffle: ImageVector = Icons.Filled.Refresh,
    val generate: ImageVector = Icons.Filled.Bolt,
    val settings: ImageVector = Icons.Filled.Settings
)

private fun structIcons() = StudioActionIcons()
