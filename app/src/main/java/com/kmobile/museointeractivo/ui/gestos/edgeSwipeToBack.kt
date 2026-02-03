package com.kmobile.museointeractivo.ui.gestos


import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import kotlin.math.abs

fun Modifier.edgeSwipeToBack(
    edgeWidthDp: Float = 24f,  // zona invisible del borde
    thresholdPx: Float = 120f, // cuánto debe arrastrar
    onBack: () -> Unit
): Modifier = pointerInput(Unit) {
    var totalDrag = 0f

    detectHorizontalDragGestures(
        onDragStart = { offset ->
            // Solo iniciar si el toque empieza cerca del borde izquierdo
            val edgeWidthPx = edgeWidthDp * density
            if (offset.x > edgeWidthPx) {
                // cancelar “gesto” si no empezó en el borde
                totalDrag = 0f
            }
        },
        onHorizontalDrag = { change, dragAmount ->
            // solo si empezó en el borde (usa la condición anterior)
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
