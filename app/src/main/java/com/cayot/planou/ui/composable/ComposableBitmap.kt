package com.cayot.planou.ui.composable

import android.graphics.Bitmap
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.view.drawToBitmap

@Composable
fun composableBitmap(composable : @Composable () -> Unit) : () -> Bitmap {
    val context = LocalContext.current

    val composeView = remember { ComposeView(context) }

    AndroidView(
        factory = {
            composeView.apply {
                setContent { composable.invoke() }
            }
        }
    )
    return ({ composeView.drawToBitmap() })
}
