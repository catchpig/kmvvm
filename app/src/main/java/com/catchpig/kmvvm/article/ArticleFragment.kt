package com.catchpig.kmvvm.article

import androidx.recyclerview.widget.LinearLayoutManager
import com.catchpig.kmvvm.R
import com.catchpig.kmvvm.adapter.ArticleAdapter
import com.catchpig.kmvvm.databinding.FragmentArticleBinding
import com.catchpig.mvvm.base.fragment.BaseVMFragment
import com.catchpig.mvvm.ext.lifecycleRefresh
import com.gyf.immersionbar.ktx.statusBarHeight

class ArticleFragment : BaseVMFragment<FragmentArticleBinding, ArticleViewModel>() {
    companion object {
        fun newInstance(): ArticleFragment {
            return ArticleFragment()
        }
    }

    private lateinit var articleAdapter: ArticleAdapter;

    override fun initParam() {
    }

    override fun initView() {
        getRootBanding().topView.let {
            it.setBackgroundResource(R.color.colorPrimary);
            it.post {
                it.layoutParams.height = statusBarHeight
            }
        }
        articleAdapter = ArticleAdapter()
        bodyBinding.refreshView.apply {
            val linearLayoutManager = LinearLayoutManager(context)
            linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
            setLayoutManager(linearLayoutManager)
            setAdapter(articleAdapter)
            autoRefresh()
        }
    }

    override fun initFlow() {
        bodyBinding.refreshView.run {
            setOnRefreshLoadMoreListener { nextPageIndex ->
                viewModel.queryArticles(nextPageIndex).lifecycleRefresh(this@ArticleFragment,this)
            }
        }
    }

}