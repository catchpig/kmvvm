package com.catchpig.mvvm.widget.refresh

import com.scwang.smart.refresh.layout.constant.RefreshState

/**
 * 页码关联
 * @author catchpig
 * @date 2017/12/21  19:49
 */
interface IPageControl {
    /**
     * 每页的数据量
     */
    var pageSize:Int
    /**
     * 下一页的页码
     */
    var nextPageIndex:Int

    /**
     * 当前的刷新状态
     */
    fun getRefreshStatus():RefreshState

    /**
     * 重置当前的页码
     */
    fun resetPageIndex()

    /**
     * 加载下一页的页码
     */
    fun loadNextPageIndex()

    /**
     * 更新数据成功
     */
    fun updateSuccess(list: MutableList<*>?)

    /**
     * 更新数据失败
     */
    fun updateError()
}
