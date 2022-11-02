package com.catchpig.kmvvm.config

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.catchpig.annotation.GlobalConfig
import com.catchpig.kmvvm.R
import com.catchpig.kmvvm.databinding.LayoutEmptyBinding
import com.catchpig.kmvvm.databinding.LayoutErrorBinding
import com.catchpig.mvvm.interfaces.IGlobalConfig

@GlobalConfig
class MvvmGlobalConfig : IGlobalConfig {
    override fun getTitleHeight(): Int {
        return R.dimen.title_bar_height
    }

    override fun getTitleBackIcon(): Int {
        return R.drawable.back_black
    }

    override fun getTitleBackground(): Int {
        return R.color.colorPrimary
    }

    override fun getTitleTextColor(): Int {
        return R.color.white
    }

    override fun isShowTitleLine(): Boolean {
        return true
    }

    override fun getTitleLineColor(): Int {
        return R.color.color_black
    }

    override fun getLoadingColor(): Int {
        return R.color.color_black
    }

    override fun getLoadingBackground(): Int {
        return R.color.white
    }

    override fun getRecyclerEmptyBanding(parent: ViewGroup): ViewBinding {
        return LayoutEmptyBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    }

    override fun getFailedBinding(layoutInflater: LayoutInflater): ViewBinding? {
        return LayoutErrorBinding.inflate(layoutInflater)
    }

    override fun onFailedReloadClickId(): Int {
        return R.id.failed_reload
    }

    override fun getPageSize(): Int {
        return 16
    }

    override fun getStartPageIndex(): Int {
        return 0
    }
}