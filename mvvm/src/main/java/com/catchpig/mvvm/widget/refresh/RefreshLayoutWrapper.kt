package com.catchpig.mvvm.widget.refresh

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import com.catchpig.mvvm.apt.KotlinMvvmCompiler
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.constant.RefreshState


/**
 * 刷新界面
 * @author catchpig
 * @date 2017/12/21 20:10
 */

class RefreshLayoutWrapper(
    context: Context,
    attrs: AttributeSet
) : SmartRefreshLayout(context, attrs), IPageControl {
    companion object {

        /**
         * 预显示界面索引
         */
        const val NONE_PRE_PAGE_INDEX = -1

        /**
         * matreral风格的下拉控件
         */
        const val SIMPLE_NAME_MATERIAL_HEADER = "MaterialHeader"
    }

    init {
        //初始化加载更多不可用
        setEnableLoadMore(false)
    }

    private val startPageIndex = KotlinMvvmCompiler.globalConfig().getStartPageIndex()

    /**
     * 当前页面index
     */
    private var currentPageIndex: Int = startPageIndex

    override var nextPageIndex: Int = NONE_PRE_PAGE_INDEX

    /**
     * 一页的条目，默认16
     */
    override var pageSize = KotlinMvvmCompiler.globalConfig().getPageSize()
    override fun getRefreshStatus(): RefreshState {
        return state
    }

    /**
     * 列表更新成功
     *
     * @param list 更新数据集合
     */
    override fun updateSuccess(list: MutableList<*>?) {
        if (list == null) {
            //设置加载更多不可用
            setEnableLoadMore(false)
        } else {
            list?.apply {
                if (size < pageSize) {
                    //设置加载更多不可用
                    setEnableLoadMore(false)
                } else {
                    //设置加载更多可用
                    setEnableLoadMore(true)
                }
            }
        }
        setEnableRefresh(true)
        updateCurrentPageIndex()
        finishUpdate(true)
    }

    /**
     * 列表更新失败
     */
    override fun updateError() {
        this.nextPageIndex = NONE_PRE_PAGE_INDEX
        finishUpdate(false)
    }

    override fun resetPageIndex() {
        this.nextPageIndex = startPageIndex
    }

    override fun loadNextPageIndex() {
        this.nextPageIndex = this.currentPageIndex + 1
    }

    /**
     * 结束刷新或加载状态
     *
     * @param success 状态成功或失败
     */
    private fun finishUpdate(success: Boolean) {
        if (isRefreshing) {
            finishRefresh(success)
        } else if (isLoading) {
            finishLoadMore(success)
        }
    }

    /**
     * 当前页面索引更新
     */
    private fun updateCurrentPageIndex() {
        this.currentPageIndex = this.nextPageIndex
        this.nextPageIndex = NONE_PRE_PAGE_INDEX
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        super.onLayout(changed, l, t, r, b)
        //如果google原生的刷新样式，将头部背景设置为透明
        refreshHeader?.let {
            if (SIMPLE_NAME_MATERIAL_HEADER == it.view.javaClass.simpleName) {
                it.view.setBackgroundColor(Color.TRANSPARENT)
            }
        }
    }

    /**
     * 设置刷新监听回调
     */
    fun setOnRefreshLoadMoreListener(listener: (nextPageIndex: Int) -> Unit) {
        setOnRefreshLoadMoreListener(object : OnRefreshListener() {

            override fun update(refreshLayout: RefreshLayoutWrapper) {
                listener(refreshLayout.nextPageIndex)
            }
        })
    }

    /**
     * 设置刷新监听回调
     */
    fun setOnRefreshLoadMoreListener(listener: (nextPageIndex: Int, pageSize: Int) -> Unit) {
        setOnRefreshLoadMoreListener(object : OnRefreshListener() {

            override fun update(refreshLayout: RefreshLayoutWrapper) {
                listener(refreshLayout.nextPageIndex, refreshLayout.pageSize)
            }
        })
    }
}
