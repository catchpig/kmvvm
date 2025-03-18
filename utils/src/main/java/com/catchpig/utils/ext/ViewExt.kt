package com.catchpig.utils.ext

import android.view.View

private val quickClickMap = mutableMapOf<Int, Long>()

/**
 * 防快点
 * @receiver View
 * @param interval Long
 * @param block Function0<Unit>
 */
fun View.quickClickListener(interval: Long = 500, block: () -> Unit) {
    setOnClickListener {
        val key = hashCode()
        val currentTimeMillis = System.currentTimeMillis()
        val lastClickTime = quickClickMap[key] ?: 0L
        if (currentTimeMillis - lastClickTime > interval) {
            quickClickMap[key] = currentTimeMillis
            block()
        }
    }
}