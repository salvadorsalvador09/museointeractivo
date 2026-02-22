package com.kmobile.museointeractivo.ui.components

import android.util.Log
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.runtime.*
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
import coil.imageLoader
import com.kmobile.museointeractivo.R

@Composable
fun CoilImages(
    imageUrl: String?,
    modifier: Modifier = Modifier,
    description: String? = null,
    onImageClick: ((String?) -> Unit)? = null,
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
            modifier = modifier.pointerInput(Unit) {
                detectTapGestures(
                    onTap = {
                        Log.d("IMG", "CoilImages TAP url=$imageUrl")
                        onImageClick?.invoke(imageUrl)
                    }
                )

            },
            placeholder = painterResource(R.drawable.placeholder),
            error = painterResource(R.drawable.nosystem),
            contentScale = ContentScale.Crop,
        )
        return
    }


    val request = remember(imageUrl) {
        ImageRequest.Builder(context)
            .data(imageUrl)
            .crossfade(true)
            .memoryCachePolicy(CachePolicy.ENABLED)
            .diskCachePolicy(CachePolicy.ENABLED)
            .networkCachePolicy(CachePolicy.ENABLED)
            .allowHardware(false)
            .size(1080, 400)
            .build()
    }

    AsyncImage(
        model = request,
        contentDescription = description,
        modifier = modifier
            .pointerInput(imageUrl) {
                val ctx = context
                val loader = ctx.imageLoader
                detectTapGestures(
                    onTap = {
                    loader.enqueue(
                        ImageRequest.Builder(ctx)
                            .data(imageUrl)
                            .memoryCachePolicy(CachePolicy.ENABLED)
                            .diskCachePolicy(CachePolicy.ENABLED)
                            .networkCachePolicy(CachePolicy.ENABLED)
                            .size(1080, 400)
                            .build()
                    )
                        onImageClick?.invoke(imageUrl)
                    },
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

