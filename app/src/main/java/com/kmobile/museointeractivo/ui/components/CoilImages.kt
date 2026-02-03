package com.kmobile.museointeractivo.ui.components

import android.os.SystemClock
import android.util.Log
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import androidx.compose.ui.res.painterResource
import com.kmobile.museointeractivo.R

@Composable
fun CoilImages(
    imageUrl: String?,
    modifier: Modifier = Modifier,
    description: String? = null
) {
    var scale by remember { mutableFloatStateOf(1f) }
    var offset by remember { mutableStateOf(Offset.Zero) }

    val state = rememberTransformableState { zoomChange, panChange, _ ->
        val newScale = (scale * zoomChange).coerceIn(1f, 4f)
        scale = newScale

        if (scale > 1f) offset += panChange else offset = Offset.Zero
    }
    val context = LocalContext.current

    if (imageUrl.isNullOrBlank()) {
        AsyncImage(
            model = null,
            contentDescription = description,
            modifier = modifier,
            placeholder = painterResource(R.drawable.placeholder),
            error = painterResource(R.drawable.nosystem),
            contentScale = ContentScale.Crop
        )
        return
    }

    val startTime = remember(imageUrl) { SystemClock.elapsedRealtime() }

    val request = remember(imageUrl) {
        ImageRequest.Builder(context)
            .data(imageUrl)
            .crossfade(true)
            .memoryCachePolicy(CachePolicy.ENABLED)
            .diskCachePolicy(CachePolicy.ENABLED)
            .networkCachePolicy(CachePolicy.ENABLED)
            .allowHardware(false)
            .size(1080, 400)
            .listener(
                onStart = { Log.d("IMG", "START url=$imageUrl") },
                onSuccess = { _, _ ->
                    val took = SystemClock.elapsedRealtime() - startTime
                    Log.d("IMG", "SUCCESS took=${took}ms url=$imageUrl")
                },
                onError = { _, result ->
                    val took = SystemClock.elapsedRealtime() - startTime
                    Log.e("IMG", "ERROR took=${took}ms url=$imageUrl throwable=${result.throwable}")
                }
            )
            .build()
    }

    AsyncImage(
        model = request,
        contentDescription = description,
        modifier = modifier
            .pointerInput(Unit) {
                detectTapGestures(
                    onDoubleTap = {
                        if (scale > 1f) {
                            scale = 1f
                            offset = Offset.Zero
                        } else {
                            scale = 2f
                        }
                    }
                )
            }
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
                translationX = offset.x
                translationY = offset.y
            }
            .transformable(state),
        contentScale = ContentScale.Crop,
        placeholder = painterResource(R.drawable.placeholder),
        error = painterResource(R.drawable.nosystem),
    )
}
