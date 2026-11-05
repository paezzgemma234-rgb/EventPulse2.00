package com.seminario2026.eventpulse.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.foundation.Image
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.seminario2026.eventpulse.R
import com.seminario2026.eventpulse.ui.theme.AcidLime
import com.seminario2026.eventpulse.ui.theme.CyberCyan
import com.seminario2026.eventpulse.ui.theme.DarkCharcoal
import com.seminario2026.eventpulse.ui.theme.DeepSpace
import com.seminario2026.eventpulse.ui.theme.DigitalOrange
import com.seminario2026.eventpulse.ui.theme.ElectricViolet
import com.seminario2026.eventpulse.ui.theme.EventPulseTheme
import com.seminario2026.eventpulse.ui.theme.MidnightVoid
import com.seminario2026.eventpulse.ui.theme.MutedSilver
import com.seminario2026.eventpulse.ui.theme.NeonMagenta
import com.seminario2026.eventpulse.ui.theme.SoftWhite
import com.seminario2026.eventpulse.ui.theme.glassmorphic
import com.seminario2026.eventpulse.ui.theme.pulsingGlow

@Composable
fun CyberCard(
    modifier: Modifier = Modifier,
    isLive: Boolean = false,
    onClick: (() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    val baseModifier = if (onClick != null) modifier.clickable { onClick() } else modifier
    val cardShape = RoundedCornerShape(20.dp)
    val cardModifier = if (isLive) {
        baseModifier
            .clip(cardShape)
            .background(DeepSpace.copy(alpha = 0.85f))
            .pulsingGlow(colorPrimary = CyberCyan, colorSecondary = NeonMagenta, shape = cardShape)
    } else {
        baseModifier.glassmorphic(shape = cardShape, backgroundColor = DeepSpace.copy(alpha = 0.8f))
    }

    Surface(
        modifier = cardModifier,
        color = Color.Transparent
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            content = content
        )
    }
}

@Composable
fun PulseBadge(
    modifier: Modifier = Modifier,
    text: String = "EN VIVO",
    isLive: Boolean = true,
    accentColor: Color = if (isLive) NeonMagenta else CyberCyan
) {
    val infiniteTransition = rememberInfiniteTransition(label = "badgePulse")
    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "dotAlpha"
    )

    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(50),
        color = accentColor.copy(alpha = 0.15f),
        border = androidx.compose.foundation.BorderStroke(1.dp, accentColor.copy(alpha = 0.6f))
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 7.dp, vertical = 2.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(5.dp)
                    .clip(CircleShape)
                    .background(accentColor.copy(alpha = if (isLive) alpha else 1f))
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = text.uppercase(),
                style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp),
                color = accentColor,
                fontWeight = FontWeight.Bold,
                maxLines = 1
            )
        }
    }
}

@Composable
fun NeonButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    gradientColors: List<Color> = listOf(CyberCyan, ElectricViolet),
    enabled: Boolean = true
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier
            .height(37.dp)
            .width(300.dp)
            .clip(RoundedCornerShape(10.dp)),
        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
        contentPadding = PaddingValues(0.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .background(
                    brush = Brush.horizontalGradient(gradientColors)
                )
                .padding(horizontal = 14.dp, vertical = 2.dp),
            contentAlignment = Alignment.Center
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                if (icon != null) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = MidnightVoid,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                }
                Text(
                    text = text,
                    style = MaterialTheme.typography.labelLarge,
                    color = MidnightVoid,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun EventPulseLogoHeader(
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        AudioPulseVisualizer(
            barCount = 5,
            activeColor = NeonMagenta,
            height = 28.dp
        )
        Spacer(modifier = Modifier.width(10.dp))
        Column {
            Text(
                text = "EventPulse",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.ExtraBold,
                color = SoftWhite,
                fontSize = 22.sp
            )
            Text(
                text = "Festivales de Arte Digital",
                style = MaterialTheme.typography.labelSmall,
                color = MutedSilver,
                fontSize = 11.sp
            )
        }
    }
}

@Composable
fun DateBadge(
    month: String,
    day: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .background(NeonMagenta)
            .padding(horizontal = 8.dp, vertical = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = month.uppercase(),
                style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp),
                color = SoftWhite,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = day,
                style = MaterialTheme.typography.titleSmall.copy(fontSize = 13.sp),
                color = SoftWhite,
                fontWeight = FontWeight.ExtraBold
            )
        }
    }
}

@Composable
fun GlassChip(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    accentColor: Color = NeonMagenta
) {
    val shape = RoundedCornerShape(50)
    val background = if (isSelected) accentColor else DeepSpace.copy(alpha = 0.8f)
    val border = if (isSelected) accentColor else MutedSilver.copy(alpha = 0.3f)
    val textColor = if (isSelected) SoftWhite else MutedSilver

    Box(
        modifier = modifier
            .clip(shape)
            .background(background)
            .border(1.dp, border, shape)
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelMedium,
            color = textColor,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
fun CyberSearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "Buscar experiencias, artistas o festivales..."
) {
    val shape = RoundedCornerShape(16.dp)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(52.dp)
            .clip(shape)
            .background(DarkCharcoal.copy(alpha = 0.8f))
            .border(1.dp, CyberCyan.copy(alpha = 0.4f), shape)
            .padding(horizontal = 16.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Canvas(modifier = Modifier.size(20.dp)) {
                val strokeWidth = 2.dp.toPx()
                val radius = size.width * 0.35f
                val centerX = size.width * 0.4f
                val centerY = size.height * 0.4f

                drawCircle(
                    color = CyberCyan,
                    radius = radius,
                    center = androidx.compose.ui.geometry.Offset(centerX, centerY),
                    style = androidx.compose.ui.graphics.drawscope.Stroke(width = strokeWidth)
                )

                drawLine(
                    color = CyberCyan,
                    start = androidx.compose.ui.geometry.Offset(centerX + radius * 0.707f, centerY + radius * 0.707f),
                    end = androidx.compose.ui.geometry.Offset(size.width * 0.9f, size.height * 0.9f),
                    strokeWidth = strokeWidth
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            BasicTextField(
                value = query,
                onValueChange = onQueryChange,
                singleLine = true,
                cursorBrush = SolidColor(CyberCyan),
                textStyle = MaterialTheme.typography.bodyLarge.copy(color = SoftWhite),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(onSearch = {}),
                modifier = Modifier.fillMaxWidth(),
                decorationBox = { innerTextField ->
                    if (query.isEmpty()) {
                        Text(
                            text = placeholder,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MutedSilver.copy(alpha = 0.6f)
                        )
                    }
                    innerTextField()
                }
            )
        }
    }
}

/**
 * Animated frequency bar visualizer for live performances
 */
@Composable
fun AudioPulseVisualizer(
    modifier: Modifier = Modifier,
    barCount: Int = 8,
    activeColor: Color = NeonMagenta,
    height: Dp = 24.dp
) {
    val infiniteTransition = rememberInfiniteTransition(label = "audioPulse")

    Row(
        modifier = modifier.height(height),
        horizontalArrangement = Arrangement.spacedBy(3.dp),
        verticalAlignment = Alignment.Bottom
    ) {
        for (i in 0 until barCount) {
            val duration = 300 + (i * 120) % 500
            val targetFactor = 0.2f + ((i * 37) % 80) / 100f

            val barHeightFactor by infiniteTransition.animateFloat(
                initialValue = 0.15f,
                targetValue = targetFactor.coerceIn(0.2f, 1.0f),
                animationSpec = infiniteRepeatable(
                    animation = tween(durationMillis = duration, easing = FastOutSlowInEasing),
                    repeatMode = RepeatMode.Reverse
                ),
                label = "bar_$i"
            )

            Box(
                modifier = Modifier
                    .width(4.dp)
                    .fillMaxHeight(barHeightFactor)
                    .clip(RoundedCornerShape(2.dp))
                    .background(activeColor)
            )
        }
    }
}

// ------------------- MAPA DE LA SEDE -------------------

data class MapZoneSymbol(
    val icon: String,
    val name: String,
    val color: Color,
    val description: String
)

val defaultVenueMapZones = listOf(
    MapZoneSymbol("⚡", "Escenario Principal", NeonMagenta, "Sonido modular, láseres y shows en vivo"),
    MapZoneSymbol("📡", "Espacios AV & Dome 360°", CyberCyan, "Cúpula geodésica con audio tridimensional"),
    MapZoneSymbol("🎨", "Exposiciones & VR Cube", ElectricViolet, "Galería de arte generativo e instalaciones VR"),
    MapZoneSymbol("🛋️", "Áreas de Descanso", DigitalOrange, "Sillas ergonómicas y música ambiente chillout"),
    MapZoneSymbol("🚪", "Accesos y Salidas", AcidLime, "Acreditaciones y torniquetes de flujo continuo"),
    MapZoneSymbol("🚻", "Sanitarios & Bio-Pods", SoftWhite, "Puntos de hidratación y baños ecológicos")
)

@Composable
fun VenueMapCard(
    modifier: Modifier = Modifier,
    onExpandMapClick: () -> Unit
) {
    val cardShape = RoundedCornerShape(20.dp)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(cardShape)
            .glassmorphic(shape = cardShape, backgroundColor = DarkCharcoal.copy(alpha = 0.7f))
            .padding(18.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "MAPA DE LA SEDE",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = CyberCyan
                    )
                    Text(
                        text = "Centro de Arte Sonoro & Dome 360°",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MutedSilver
                    )
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(AcidLime.copy(alpha = 0.2f))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "PLANO PROVISIONAL",
                        style = MaterialTheme.typography.labelSmall,
                        color = AcidLime,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Venue Blueprint Vector Container
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(MidnightVoid)
                    .border(1.dp, CyberCyan.copy(alpha = 0.5f), RoundedCornerShape(16.dp))
                    .clickable { onExpandMapClick() },
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_venue_map),
                    contentDescription = "Mapa ilustrativo de la sede",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Fit
                )

                // Overlay Button to Zoom/Expand
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(12.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(MidnightVoid.copy(alpha = 0.85f))
                        .border(1.dp, NeonMagenta, RoundedCornerShape(12.dp))
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = "🔍 AMPLIAR MAPA",
                        style = MaterialTheme.typography.labelSmall,
                        color = SoftWhite,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Leyenda de Símbolos
            Text(
                text = "LEYENDA DE ZONAS Y SIMBOLOS",
                style = MaterialTheme.typography.labelMedium,
                color = SoftWhite,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                defaultVenueMapZones.chunked(2).forEach { rowZones ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        rowZones.forEach { zone ->
                            Row(
                                modifier = Modifier.weight(1f),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(10.dp)
                                        .clip(CircleShape)
                                        .background(zone.color)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "${zone.icon} ${zone.name}",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MutedSilver,
                                    maxLines = 1
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun VenueMapZoomDialog(
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            NeonButton(
                text = "CERRAR MAPA",
                onClick = onDismiss,
                modifier = Modifier.fillMaxWidth()
            )
        },
        containerColor = DeepSpace,
        shape = RoundedCornerShape(24.dp),
        title = {
            Text(
                text = "🗺️ MAPA DETALLADO DE LA SEDE",
                style = MaterialTheme.typography.titleLarge,
                color = CyberCyan,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(380.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(MidnightVoid)
                        .border(1.dp, CyberCyan, RoundedCornerShape(16.dp))
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_venue_map),
                        contentDescription = "Mapa expandido de la sede",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Fit
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(defaultVenueMapZones) { zone ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .background(DarkCharcoal)
                                .padding(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(12.dp)
                                    .clip(CircleShape)
                                    .background(zone.color)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(
                                    text = "${zone.icon} ${zone.name}",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = SoftWhite,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = zone.description,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MutedSilver
                                )
                            }
                        }
                    }
                }
            }
        }
    )
}

// ------------------- UI ESTADOS (LOADING & ERROR) -------------------

@Composable
fun CyberLoadingState(
    modifier: Modifier = Modifier,
    message: String = "Cargando eventos...",
    subtitle: String = "Estamos preparando la mejor experiencia para vos."
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(72.dp)
                    .clip(CircleShape)
                    .background(
                        brush = Brush.sweepGradient(
                            listOf(NeonMagenta, CyberCyan, ElectricViolet, NeonMagenta)
                        )
                    )
                    .padding(6.dp)
                    .clip(CircleShape)
                    .background(MidnightVoid),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    color = CyberCyan,
                    strokeWidth = 3.dp,
                    modifier = Modifier.size(52.dp)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = message,
                style = MaterialTheme.typography.titleMedium,
                color = SoftWhite,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodyMedium,
                color = MutedSilver,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )

            Spacer(modifier = Modifier.height(30.dp))

            AudioPulseVisualizer(barCount = 12, activeColor = ElectricViolet, height = 24.dp)
        }
    }
}

@Composable
fun CyberErrorState(
    modifier: Modifier = Modifier,
    message: String = "Mostramos el último cronograma guardado en tu dispositivo.",
    onRetry: (() -> Unit)? = null
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Disconnected Wi-Fi icon in Neon Magenta
            Text(
                text = "📶",
                fontSize = 54.sp,
                color = NeonMagenta
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Sin conexión a internet",
                style = MaterialTheme.typography.titleLarge,
                color = SoftWhite,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium,
                color = MutedSilver,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            if (onRetry != null) {
                Spacer(modifier = Modifier.height(24.dp))
                NeonButton(
                    text = "🔄  Reintentar",
                    onClick = onRetry,
                    modifier = Modifier
                        .height(44.dp)
                        .width(180.dp),
                    gradientColors = listOf(NeonMagenta, ElectricViolet)
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Footer Card: Last Update Time
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .background(DeepSpace.copy(alpha = 0.8f))
                    .border(1.dp, MutedSilver.copy(alpha = 0.2f), RoundedCornerShape(16.dp))
                    .padding(horizontal = 16.dp, vertical = 10.dp)
            ) {
                Text(
                    text = "🕒  Última actualización: 24/10/2026 - 19:45 hs",
                    style = MaterialTheme.typography.labelSmall,
                    color = MutedSilver
                )
            }
        }
    }
}

// ------------------- PREVIEWS -------------------

@Preview(name = "Cyber Card Preview", showBackground = true, backgroundColor = 0xFF080B18)
@Composable
fun CyberCardPreview() {
    EventPulseTheme {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.padding(16.dp)) {
            CyberCard(isLive = true) {
                Text(text = "Cyber Card Live", style = MaterialTheme.typography.titleMedium, color = SoftWhite)
            }
            CyberCard(isLive = false) {
                Text(text = "Cyber Card Normal", style = MaterialTheme.typography.titleMedium, color = SoftWhite)
            }
        }
    }
}

@Preview(name = "Pulse Badge Preview", showBackground = true, backgroundColor = 0xFF080B18)
@Composable
fun PulseBadgePreview() {
    EventPulseTheme {
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.padding(16.dp)) {
            PulseBadge(text = "EN VIVO", isLive = true)
            PulseBadge(text = "PRÓXIMO", isLive = false)
        }
    }
}

@Preview(name = "Neon Button Preview", showBackground = true, backgroundColor = 0xFF080B18)
@Composable
fun NeonButtonPreview() {
    EventPulseTheme {
        Box(modifier = Modifier.padding(16.dp)) {
            NeonButton(text = "CONSEGUIR PASS", onClick = {})
        }
    }
}

@Preview(name = "Glass Chip Preview", showBackground = true, backgroundColor = 0xFF080B18)
@Composable
fun GlassChipPreview() {
    EventPulseTheme {
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.padding(16.dp)) {
            GlassChip(text = "Instalaciones Inmersivas", isSelected = true, onClick = {})
            GlassChip(text = "Música Electrónica", isSelected = false, onClick = {})
        }
    }
}

@Preview(name = "Cyber Search Bar Preview", showBackground = true, backgroundColor = 0xFF080B18)
@Composable
fun CyberSearchBarPreview() {
    EventPulseTheme {
        Box(modifier = Modifier.padding(16.dp)) {
            CyberSearchBar(query = "MUTEK", onQueryChange = {})
        }
    }
}

@Preview(name = "Audio Pulse Visualizer Preview", showBackground = true, backgroundColor = 0xFF080B18)
@Composable
fun AudioPulseVisualizerPreview() {
    EventPulseTheme {
        Box(modifier = Modifier.padding(16.dp)) {
            AudioPulseVisualizer(barCount = 10, activeColor = NeonMagenta, height = 30.dp)
        }
    }
}

@Preview(name = "Venue Map Card Preview", showBackground = true, backgroundColor = 0xFF080B18)
@Composable
fun VenueMapCardPreview() {
    EventPulseTheme {
        Box(modifier = Modifier.padding(16.dp)) {
            VenueMapCard(onExpandMapClick = {})
        }
    }
}

@Preview(name = "Cyber Loading State Preview", showBackground = true, backgroundColor = 0xFF080B18)
@Composable
fun CyberLoadingStatePreview() {
    EventPulseTheme {
        Box(modifier = Modifier.padding(16.dp)) {
            CyberLoadingState()
        }
    }
}

@Preview(name = "Cyber Error State Preview", showBackground = true, backgroundColor = 0xFF080B18)
@Composable
fun CyberErrorStatePreview() {
    EventPulseTheme {
        Box(modifier = Modifier.padding(16.dp)) {
            CyberErrorState(
                message = "Error al conectar con la red de transmisores CyberPulse. Verifica tu conexión.",
                onRetry = {}
            )
        }
    }
}
