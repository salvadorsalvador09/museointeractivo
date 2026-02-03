package com.kmobile.museointeractivo.ui.components

import android.net.Uri
import android.view.ViewGroup
import androidx.annotation.OptIn
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView
import kotlinx.coroutines.delay
import kotlin.math.max

@OptIn(UnstableApi::class)
@Composable
fun EgyptVideoPlayer(
    url: String,
    modifier: Modifier = Modifier,
    autoPlay: Boolean = true,
    containerColor: Color = Color.Transparent,
    overlayColor: Color = Color.Black.copy(alpha = 0.35f),
    iconTint: Color = Color.White,
) {
    val context = LocalContext.current

    val player = remember(url) {
        ExoPlayer.Builder(context).build().apply {
            setMediaItem(MediaItem.fromUri(Uri.parse(url)))
            prepare()
            playWhenReady = autoPlay
        }
    }

    DisposableEffect(player) {
        onDispose { player.release() }
    }

    var isPlaying by remember { mutableStateOf(player.isPlaying) }
    var durationMs by remember { mutableStateOf(0L) }
    var positionMs by remember { mutableStateOf(0L) }
    var bufferedMs by remember { mutableStateOf(0L) }

    // Listener para play/pause y duración
    DisposableEffect(player) {
        val listener = object : Player.Listener {
            override fun onIsPlayingChanged(isPlayingNow: Boolean) {
                isPlaying = isPlayingNow
            }

            override fun onPlaybackStateChanged(state: Int) {
                durationMs = max(0L, player.duration)
            }
        }
        player.addListener(listener)
        onDispose { player.removeListener(listener) }
    }

    // “Ticker” para ir refrescando tiempos
    LaunchedEffect(player) {
        while (true) {
            positionMs = max(0L, player.currentPosition)
            bufferedMs = max(0L, player.bufferedPosition)
            durationMs = max(0L, player.duration)
            delay(250)
        }
    }

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(18.dp))
            .background(containerColor)
    ) {
        // Video (PlayerView sin controles)
        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = { ctx ->
                PlayerView(ctx).apply {
                    useController = false
                    resizeMode = AspectRatioFrameLayout.RESIZE_MODE_ZOOM
                    this.player = player
                    layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                }
            }
        )

        // Overlay con controles
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(overlayColor)
                .padding(12.dp)
        ) {
            // Botones centro: prev / play / next
            Row(
                modifier = Modifier
                    .align(Alignment.Center)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Previous “en segundo” (más pequeño)


                // Play/Pause principal
                BigControlButton(
                    onClick = {
                        if (player.isPlaying) player.pause() else player.play()
                    },
                    tint = iconTint
                ) {
                    Text(if (isPlaying) "⏸" else "▶", color = iconTint)
                }


            }

            // Barra de progreso + tiempos (abajo)
            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
            ) {

                val safeDuration = max(1L, durationMs)

                val playedProgress = (positionMs.toFloat() / safeDuration.toFloat()).coerceIn(0f, 1f)
                val bufferedProgress = (bufferedMs.toFloat() / safeDuration.toFloat()).coerceIn(0f, 1f)

                var isScrubbing by remember { mutableStateOf(false) }
                var scrubProgress by remember { mutableStateOf(playedProgress) }

                LaunchedEffect(playedProgress) {
                    if (!isScrubbing) scrubProgress = playedProgress
                }

                VideoSeekBar(
                    value = scrubProgress,
                    buffered = bufferedProgress,
                    onValueChange = {
                        isScrubbing = true
                        scrubProgress = it
                    },
                    onValueChangeFinished = {
                        player.seekTo((scrubProgress * safeDuration).toLong())
                        isScrubbing = false
                    },
                    modifier = Modifier.fillMaxWidth(),
                    tint = iconTint
                )



                // Tiempos: transcurrido y restante
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = formatTime(positionMs),
                        style = MaterialTheme.typography.labelMedium,
                        color = iconTint.copy(alpha = 0.95f)
                    )
                    Text(
                        text = "-" + formatTime(max(0L, durationMs - positionMs)),
                        style = MaterialTheme.typography.labelMedium,
                        color = iconTint.copy(alpha = 0.95f)
                    )
                }
            }
        }
    }
}

@Composable
private fun BigControlButton(
    onClick: () -> Unit,
    tint: Color,
    content: @Composable () -> Unit,
) {
    Surface(
        shape = CircleShape,
        color = Color.Black.copy(alpha = 0.25f),
        tonalElevation = 0.dp,
        shadowElevation = 0.dp
    ) {
        IconButton(
            onClick = onClick,
            modifier = Modifier.size(72.dp)
        ) {
            // Texto usado como icono (para no depender de icon packs)
            CompositionLocalProvider(LocalContentColor provides tint) { content() }
        }
    }
}



private fun formatTime(ms: Long): String {
    val totalSeconds = (ms / 1000).toInt().coerceAtLeast(0)
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60
    return "%d:%02d".format(minutes, seconds)
}


@kotlin.OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun VideoSeekBar(
    value: Float,
    buffered: Float,
    onValueChange: (Float) -> Unit,
    onValueChangeFinished: () -> Unit,
    modifier: Modifier = Modifier,
    tint: Color = Color.White,
) {
    val trackHeight = 4.dp
    val thumbSize = 10.dp

    Box(modifier = modifier.height(28.dp)) {
        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxWidth()
                .height(trackHeight)
                .clip(RoundedCornerShape(999.dp))
                .background(tint.copy(alpha = 0.20f))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(buffered.coerceIn(0f, 1f))
                    .background(tint.copy(alpha = 0.35f))
            )
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(value.coerceIn(0f, 1f))
                    .background(tint.copy(alpha = 0.95f))
            )
        }

        Box(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .offset(x = ((value.coerceIn(0f, 1f)) * 1f).let { 0.dp }) // el thumb real lo “mueve” el Slider invisible
        )

        Slider(
            value = value.coerceIn(0f, 1f),
            onValueChange = onValueChange,
            onValueChangeFinished = onValueChangeFinished,
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center),
            colors = SliderDefaults.colors(
                activeTrackColor = Color.Transparent,
                inactiveTrackColor = Color.Transparent,
                disabledActiveTrackColor = Color.Transparent,
                disabledInactiveTrackColor = Color.Transparent,
                activeTickColor = Color.Transparent,
                inactiveTickColor = Color.Transparent,
                thumbColor = tint.copy(alpha = 0.95f)
            ),
            thumb = {
                Box(
                    modifier = Modifier
                        .size(thumbSize)
                        .clip(CircleShape)
                        .background(tint.copy(alpha = 0.95f))
                )
            }
        )
    }
}


