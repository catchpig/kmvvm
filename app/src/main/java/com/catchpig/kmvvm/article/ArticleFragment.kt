package com.catchpig.kmvvm.article

import com.catchpig.kmvvm.R
import com.catchpig.kmvvm.databinding.FragmentArticleBinding
import com.catchpig.mvvm.base.fragment.BaseVMFragment
import com.gyf.immersionbar.ktx.hideStatusBar
import com.gyf.immersionbar.ktx.immersionBar

class ArticleFragment : BaseVMFragment<FragmentArticleBinding, ArticleViewModel>() {
    companion object {
        fun newInstance(): ArticleFragment {
            return ArticleFragment()
        }
    }

    override fun onStart() {
        super.onStart()
        immersionBar {
            transparentStatusBar()
            statusBarColor(R.color.colorPrimary)
        }
    }

    override fun initParam() {

    }

    override fun initView() {

    }

    override fun initFlow() {

    }
}