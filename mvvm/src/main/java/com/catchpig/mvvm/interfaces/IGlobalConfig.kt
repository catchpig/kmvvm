package com.catchpig.mvvm.interfaces

import android.view.ViewGroup
import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import androidx.annotation.DrawableRes
import androidx.viewbinding.ViewBinding

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

    fun getRecyclerEmptyBanding(parent: ViewGroup): ViewBinding
}