package com.example.hobbyhub.core.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionContext
import androidx.compose.ui.platform.ComposeView
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import kotlin.coroutines.resume
import kotlinx.coroutines.suspendCancellableCoroutine

// Helper function to convert a composable to a BitmapDescriptor
suspend fun composableToBitmapDescriptor(
    context: Context,
    parentComposition: CompositionContext,
    content: @Composable () -> Unit
): BitmapDescriptor? = suspendCancellableCoroutine { continuation ->
    val composeView = ComposeView(context).apply {
        setParentCompositionContext(parentComposition)
        setContent(content)
    }

    val viewGroup = object : ViewGroup(context) {
        override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {}
    }
    viewGroup.addView(composeView)

    viewGroup.post {
        try {
            val spec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
            viewGroup.measure(spec, spec)
            viewGroup.layout(0, 0, viewGroup.measuredWidth, viewGroup.measuredHeight)

            val bitmap = Bitmap.createBitmap(viewGroup.width, viewGroup.height, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bitmap)
            viewGroup.draw(canvas)

            continuation.resume(BitmapDescriptorFactory.fromBitmap(bitmap))
        } catch (e: Exception) {
            continuation.resume(null)
        }
    }
}