package com.kmobile.museointeractivo.ui.gestos


import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput

fun Modifier.edgeSwipeToBack(
    edgeWidthDp: Float = 24f,
    thresholdPx: Float = 120f,
    onBack: () -> Unit
): Modifier = pointerInput(Unit) {
    var totalDrag = 0f

    detectHorizontalDragGestures(
        onDragStart = { offset ->
            val edgeWidthPx = edgeWidthDp * density
            if (offset.x > edgeWidthPx) {
                totalDrag = 0f
            }
        },
        onHorizontalDrag = { change, dragAmount ->
            totalDrag += dragAmount
            change.consume()
        },
        onDragEnd = {
            if (totalDrag > thresholdPx) onBack()
            totalDrag = 0f
        },
        onDragCancel = { totalDrag = 0f }
    )
}
