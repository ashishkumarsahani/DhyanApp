package com.epilepto.dhyanapp.utils

import android.graphics.Picture
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.drawscope.draw
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas

fun Modifier.capture(picture: Picture):Modifier{
    return this.then(
        Modifier.drawWithCache {
        // Example that shows how to redirect rendering to an Android Picture and then
        // draw the picture into the original destination
        val width = this.size.width.toInt()
        val height = this.size.height.toInt()

        onDrawWithContent {
            val pictureCanvas =
                androidx.compose.ui.graphics.Canvas(
                    picture.beginRecording(
                        width,
                        height
                    )
                )
            draw(this, this.layoutDirection, pictureCanvas, this.size) {
                this@onDrawWithContent.drawContent()
            }
            picture.endRecording()

            drawIntoCanvas { canvas ->
                canvas.nativeCanvas.drawPicture(
                    picture
                )
            }
        }
    })
}


