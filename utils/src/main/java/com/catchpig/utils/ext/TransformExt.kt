package com.catchpig.utils.ext

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.PixelFormat
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import java.io.ByteArrayOutputStream
import java.util.*
import kotlin.experimental.or
import kotlin.math.absoluteValue


/**
 * catchpig
 * on 2019/6/14 15:43
 */

/**
 * bitmap to byte array
 */
fun Bitmap.toByteArray(format: Bitmap.CompressFormat = Bitmap.CompressFormat.PNG): ByteArray {
    ByteArrayOutputStream().use {
        compress(format, 100, it)
        return toByteArray()
    }
}

/**
 * byte array to bitmap
 */
fun ByteArray.toBitmap(): Bitmap = BitmapFactory.decodeByteArray(this, 0, size)

/**
 * drawable to bitmap
 */
fun Drawable.toBitmap(): Bitmap {
    if (this is BitmapDrawable) return bitmap

    val bitmap = if (intrinsicHeight <= 0 || intrinsicWidth <= 0) {
        Bitmap.createBitmap(
            1,
            1,
            if (opacity != PixelFormat.OPAQUE) Bitmap.Config.ARGB_8888 else Bitmap.Config.RGB_565
        )
    } else {
        Bitmap.createBitmap(
            intrinsicWidth,
            intrinsicHeight,
            if (opacity != PixelFormat.OPAQUE) Bitmap.Config.ARGB_8888 else Bitmap.Config.RGB_565
        )
    }

    val canvas = Canvas(bitmap)
    setBounds(0, 0, canvas.width, canvas.height)
    draw(canvas)
    return bitmap
}

/**
 * bitmap to drawable
 */
fun Bitmap.toDrawable(context: Context): Drawable = BitmapDrawable(context.resources, this)

/**
 * drawable to byte array
 */
fun Drawable.toByteArray(format: Bitmap.CompressFormat = Bitmap.CompressFormat.PNG): ByteArray =
    toBitmap().toByteArray(format)

/**
 * byte array to drawable
 */
fun ByteArray.toDrawable(context: Context): Drawable = toBitmap().toDrawable(context)

