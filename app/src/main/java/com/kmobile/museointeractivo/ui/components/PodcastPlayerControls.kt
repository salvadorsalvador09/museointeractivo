package com.kmobile.museointeractivo.ui.components

import androidx.annotation.OptIn
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView

@OptIn(UnstableApi::class)
@Composable
fun PodcastPlayerCard(
    audioUrl: String,
    imageUrl: String?,
) {
    val context = LocalContext.current
    val player = remember { ExoPlayer.Builder(context).build() }
    val lifecycleOwner = androidx.lifecycle.compose.LocalLifecycleOwner.current


    DisposableEffect(lifecycleOwner) {
        val observer = androidx.lifecycle.LifecycleEventObserver { _, event ->
            if (event == androidx.lifecycle.Lifecycle.Event.ON_STOP) {
                player.pause()
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
            player.release()
        }
    }
    LaunchedEffect(audioUrl) {
        player.setMediaItem(MediaItem.fromUri(audioUrl))
        player.prepare()
        player.playWhenReady = true
    }

    LaunchedEffect(audioUrl) {
        player.setMediaItem(MediaItem.fromUri(audioUrl))
        player.prepare()
        player.playWhenReady = true
    }

    var positionMs by remember { mutableLongStateOf(0L) }
    var durationMs by remember { mutableLongStateOf(0L) }

    LaunchedEffect(player) {
        while (true) {
            positionMs = player.currentPosition
            durationMs = player.duration.coerceAtLeast(0L)
            kotlinx.coroutines.delay(250)
        }
    }

    val remainingMs = (durationMs - positionMs).coerceAtLeast(0L)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(220.dp)
    ) {
        AndroidView(
            factory = { ctx ->
                PlayerView(ctx).apply {
                    this.player = player
                    useController = true

                    setShowNextButton(true)
                    setShowPreviousButton(true)
                    setShowFastForwardButton(true)
                    setShowRewindButton(true)
                    setShowMultiWindowTimeBar(true)

                    setShutterBackgroundColor(android.graphics.Color.BLACK)
                    setBackgroundColor(android.graphics.Color.BLACK)

                    useArtwork = true
                    artworkDisplayMode = PlayerView.ARTWORK_DISPLAY_MODE_FILL
                    setDefaultArtwork(android.graphics.drawable.ColorDrawable(android.graphics.Color.BLACK))

                    controllerShowTimeoutMs = 0
                }
            },
            update = { pv ->
                pv.useArtwork = true
                pv.artworkDisplayMode = PlayerView.ARTWORK_DISPLAY_MODE_FILL

                if (!imageUrl.isNullOrBlank()) {
                    val request = coil.request.ImageRequest.Builder(context)
                        .data(imageUrl)
                        .allowHardware(false)
                        .target(
                            onSuccess = { drawable ->
                                pv.setDefaultArtwork(drawable)
                                pv.invalidate()
                            },
                            onError = {
                                pv.setDefaultArtwork(
                                    android.graphics.drawable.ColorDrawable(android.graphics.Color.BLACK)
                                )
                                pv.invalidate()
                            }
                        )
                        .build()
                    coil.Coil.imageLoader(context).enqueue(request)
                }
            },
            modifier = Modifier.fillMaxSize()
        )

        Text(
            text = "${formatTime(positionMs)}  (-${formatTime(remainingMs)})",
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(10.dp),
            color = androidx.compose.ui.graphics.Color.White
        )
    }
}
private fun formatTime(ms: Long): String {
    val totalSeconds = (ms / 1000).toInt()
    val seconds = totalSeconds % 60
    val minutes = (totalSeconds / 60) % 60
    val hours = totalSeconds / 3600

    return if (hours > 0) {
        String.format("%d:%02d:%02d", hours, minutes, seconds)
    } else {
        String.format("%02d:%02d", minutes, seconds)
    }
}
