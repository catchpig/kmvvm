package com.catchpig.mvvm.widget.refresh

/**
 * 页码关联
 * @author catchpig
 * @date 2017/12/21  19:49
 */
interface IPageControl {

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
