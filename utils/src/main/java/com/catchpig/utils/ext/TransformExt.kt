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

private val HEX_DIGITS =
    charArrayOf('0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f')

/**
 * byte array to hex string
 */
fun ByteArray.toHexString(): String {
    val result = CharArray(size shl 1)
    var index = 0
    for (b in this) {
        result[index++] = HEX_DIGITS[b.toInt().shr(4) and 0xf]
        result[index++] = HEX_DIGITS[b.toInt() and 0xf]
    }
    return String(result)
}

/**
 * hex string to byte array
 */
fun String.hexToByteArray(): ByteArray {
    var len = length
    var hexString = this
    if (len % 2 != 0) {
        hexString = "0$hexString"
        len++
    }
    val hexBytes = hexString.toUpperCase(Locale.getDefault()).toCharArray()
    val ret = ByteArray(len shr 1)
    var i = 0
    while (i < len) {
        ret[i shr 1] = ((hexBytes[i].hexToInt()) shl 4 or (hexBytes[i + 1].hexToInt())).toByte()
        i += 2
    }
    return ret
}

fun Char.hexToInt(): Int {
    return when (this) {
        in '0'..'9' -> this - '0'
        in 'A'..'F' -> this - 'A' + 10
        else -> throw IllegalArgumentException()
    }
}

/**
 * byte array to int
 */
fun ByteArray.toInt(): Int {
    return if (size==0) {
        0
    }else{
        (get(0).toInt() shl 24) or (get(1).toInt() shl 16) or (get(2).toInt() shl 8) or get(3).toInt()
    }
}

/**
 * int to byte array
 */
fun Int.toByteArray(): ByteArray {
    var byteArray = ByteArray(4)
    byteArray[0] = (this shr 24) as Byte
    byteArray[1] = (this shr 16) as Byte
    byteArray[2] = (this shr 8) as Byte
    byteArray[3] = this as Byte
    return byteArray
}

/**
 * byte array to short
 */
fun ByteArray.toShort(): Short {
    return if(size==0){
        return 0
    }else{
        (get(0).toInt() or  (get(1).toInt() shl 8)) as Short
    }
}

/**
 * short to byte array
 */
fun Short.toByteArray(): ByteArray {
    var temp:Int = toInt()
    var byteArray = ByteArray(2)
    byteArray.forEachIndexed { index, _ ->
        byteArray[index] = temp.toByte()
        temp = temp shr 8
    }
    return byteArray
}

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

