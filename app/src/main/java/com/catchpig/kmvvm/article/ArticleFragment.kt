package com.catchpig.kmvvm.article

import com.catchpig.kmvvm.R
import com.catchpig.kmvvm.databinding.FragmentArticleBinding
import com.catchpig.mvvm.base.fragment.BaseVMFragment
import com.gyf.immersionbar.ktx.statusBarHeight

class ArticleFragment : BaseVMFragment<FragmentArticleBinding, ArticleViewModel>() {
    companion object {
        fun newInstance(): ArticleFragment {
            return ArticleFragment()
        }
    }

    override fun initParam() {
    }

    override fun initView() {
        bodyBinding.topView.let {
            it.setBackgroundResource(R.color.colorPrimary);
            it.post {
                it.layoutParams.height = statusBarHeight
            }
        }
    }

    override fun initFlow() {

    }

}