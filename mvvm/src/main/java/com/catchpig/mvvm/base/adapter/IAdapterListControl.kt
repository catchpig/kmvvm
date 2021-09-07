package com.catchpig.mvvm.base.adapter

/**
 * 数据填充接口
 * @author catchpig
 * @date 2017/12/21 19:49
 */
interface IAdapterListControl<T> {

    fun set(list: MutableList<T>?)

    fun add(list: MutableList<T>?)
    /**
     * 自动更新数据
     */
    fun autoUpdateList(list: MutableList<T>?)

    operator fun get(index: Int): T?
    /**
     * 更新失败
     */
    fun updateFailed()
    /**
     * 每页的数据量
     */
    var pageSize:Int
    /**
     * 下一页的页码
     */
    var nextPageIndex:Int
}
