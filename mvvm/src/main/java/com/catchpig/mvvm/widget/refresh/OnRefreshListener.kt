package com.catchpig.mvvm.widget.refresh

import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener

/**
 * 下拉刷新和上拉加载更多的封装类
 * @author catchpig
 * @date 2017/12/20 18:13
 */

abstract class OnRefreshListener : OnRefreshLoadMoreListener {
    /**
     * 下拉刷新
     */
    override fun onRefresh(refreshlayout: RefreshLayout) {
        val refreshLayoutWrapper = refreshlayout as RefreshLayoutWrapper
        //将页码变为1
        refreshLayoutWrapper.resetPageIndex()
        refreshlayout.setEnableLoadMore(false)
        update(refreshLayoutWrapper)
    }

    /**
     * 上拉加载更多
     */
    override fun onLoadMore(refreshLayout: RefreshLayout) {
        val refreshLayoutWrapper = refreshLayout as RefreshLayoutWrapper
        //请求列表的参数页码加1
        refreshLayoutWrapper.loadNextPageIndex()
        refreshLayout.setEnableRefresh(false)
        update(refreshLayoutWrapper)
    }

    /**
     * 下拉刷新和上拉加载更多的回调接口
     */
    abstract fun update(refreshLayout: RefreshLayoutWrapper)
}