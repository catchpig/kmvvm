package com.catchpig.mvvm.interfaces

import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import androidx.annotation.DrawableRes
import androidx.annotation.LayoutRes

interface IGlobalConfig {
    @DimenRes
    fun getTitleHeight(): Int

    @DrawableRes
    fun getTitleBackIcon(): Int

    @ColorRes
    fun getTitleBackground(): Int

    @ColorRes
    fun getTitleTextColor(): Int

    fun isShowTitleLine(): Boolean

    @ColorRes
    fun getTitleLineColor(): Int

    @ColorRes
    fun getLoadingColor(): Int

    @ColorRes
    fun getLoadingBackground(): Int

    @LayoutRes
    fun getRecyclerEmptyLayout(): Int
}