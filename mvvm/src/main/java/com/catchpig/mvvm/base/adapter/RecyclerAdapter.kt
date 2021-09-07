package com.catchpig.mvvm.base.adapter

import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.annotation.IntRange
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.catchpig.mvvm.R
import com.catchpig.mvvm.base.adapter.RecyclerAdapter.ItemViewType.*
import com.catchpig.mvvm.ext.getEmptyLayout
import com.catchpig.mvvm.widget.refresh.IPageControl
import com.scwang.smart.refresh.layout.constant.RefreshState
import java.util.*


/**
 * RecyclerViewAdapter基类
 * @author catchpig
 * @date 2017年12月22日13:43:56
 */

abstract class RecyclerAdapter<M>(private val iPageControl: IPageControl? = null) : RecyclerView.Adapter<CommonViewHolder>(), IAdapterListControl<M> {
    /**
     * item的类型
     */
    enum class ItemViewType(val value: Int) {
        /**
         * 头部类型
         */
        TYPE_HEADER(-0x01),
        /**
         * 底部类型
         */
        TYPE_FOOTER(-0x02),
        /**
         * 无数据类型
         */
        TYPE_EMPTY(-0x03),
        /**
         * 正常的item
         */
        TYPE_NORMAL(0x00);

        companion object {
            fun enumOfValue(value: Int): ItemViewType {
                return when (value) {
                    TYPE_HEADER.value -> {
                        TYPE_HEADER
                    }
                    TYPE_FOOTER.value -> {
                        TYPE_FOOTER
                    }
                    TYPE_EMPTY.value -> {
                        TYPE_EMPTY
                    }
                    else -> {
                        TYPE_NORMAL
                    }
                }
            }
        }


    }

    var data: MutableList<M> = ArrayList()

    /**
     * 头部
     */
    private var headerView: View? = null

    /**
     * 底部
     */
    private var footerView: View? = null

    /**
     * 是否展示空页面
     */
    private var showEmpty: Boolean = false
    /**
     * 空页面layout
     */
    private var emptyLayout = R.layout.view_load_empty
    /**
     * 是否是第一次加载数据
     */
    private var firstLoad = true

    var onItemClickListener: OnItemClickListener<M>? = null

    fun onItemClickListener(listener:(id: Int, m: M, position: Int)->Unit){
        onItemClickListener = object : OnItemClickListener<M> {
            override fun itemClick(id: Int, m: M, position: Int) {
                listener(id,m,position)
            }

        }
    }

    fun addHeaderView(@LayoutRes layoutId: Int){
        check(recyclerView!=null){
            "请在adapter被RecyclerView初始化之后调用"
        }
        headerView = LayoutInflater.from(recyclerView?.context).inflate(layoutId,recyclerView,false)
        notifyDataSetChanged()
    }

    fun headerView(header:View.()->Unit){
        headerView?.let {
            header(it)
        }
    }

    fun addFooterView(@LayoutRes layoutId: Int){
        check(recyclerView!=null){
            "请在adapter被RecyclerView初始化之后调用"
        }
        footerView = LayoutInflater.from(recyclerView?.context).inflate(layoutId,recyclerView,false)
        notifyDataSetChanged()
    }

    fun footerView(footer:View.()->Unit){
        footerView?.let {
            footer(it)
        }
    }


    override fun set(list: MutableList<M>?) {
        firstLoad = false
        if (list != null) {
            data = list
            notifyDataSetChanged()
        } else {
            clear()
        }
    }

    override fun getItemCount(): Int {
        var size = data.size
        //有头部,item的个数+1
        if (headerView != null) {
            size++
        }
        //有底部,item的个数+1
        if (footerView != null) {
            size++
        }
        if (size == 0) {
            showEmpty = true
            size = 1
        } else {
            showEmpty = false
        }
        return size
    }


    private var recyclerView: RecyclerView? = null

    /**
     * 设置空页面
     */
    fun setEmptyLayout(@LayoutRes emptyLayout: Int) {
        this.emptyLayout = emptyLayout
    }

    override fun get(position: Int): M? {
        check(position > 0 && position < data.size) {
            "position必须大于0,且不能大于data.size"
        }
        return data[position]
    }

    /**
     * 清空数据
     */
    fun clear() {
        data.clear()
        notifyDataSetChanged()
    }

    /**
     * list中添加更多的数据
     */
    override fun add(list: MutableList<M>?) {
        list?.let {
            data.addAll(it)
        }
        notifyDataSetChanged()
    }

    override var pageSize: Int
        get() {
            check(iPageControl != null) {
                "获取pageSize,IPageControl不能为空"
            }
            return iPageControl!!.pageSize
        }
        set(_) {
            throw IllegalStateException("pageSize不能外部赋值")
        }

    override var nextPageIndex: Int
        get() {
            check(iPageControl != null) {
                "获取nextPageIndex,IPageControl不能为空"
            }
            return iPageControl!!.nextPageIndex
        }
        set(_) {
            throw IllegalStateException("nextPageIndex不能外部赋值")
        }

    override fun autoUpdateList(list: MutableList<M>?) {
        iPageControl?.let {
            when (it.getRefreshStatus()) {
                RefreshState.Refreshing -> {
                    set(list)
                }
                RefreshState.Loading -> {
                    add(list)
                }
            }
            it.updateSuccess(list)
        }
    }

    override fun updateFailed() {
        iPageControl?.let {
            it.updateError()
        }
    }

    /**
     *
     * @param position Int
     * @return Int
     */
    override fun getItemViewType(position: Int): Int {
        return if (position == 0 && showEmpty) {
            //当前数据空位,展示空页面
            TYPE_EMPTY.value
        } else if (position == 0 && headerView != null) {
            //当前view是头部信息
            TYPE_HEADER.value
        } else if (position == itemCount && footerView != null) {
            //当前view是底部信息
            TYPE_FOOTER.value
        } else getCenterViewType(position)
    }

    /**
     * 标准的item的类型
     * @param position
     * @return 返回参数不能小于0
     */
    @IntRange(from = 0)
    fun getCenterViewType(position: Int): Int {
        return TYPE_NORMAL.value
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommonViewHolder {
        val view = when (ItemViewType.enumOfValue(viewType)) {
            TYPE_HEADER -> {
                headerView!!
            }
            TYPE_FOOTER -> {
                footerView!!
            }
            TYPE_EMPTY -> {
                parent.context.getEmptyLayout().let {
                    if (it != Resources.ID_NULL) {
                        emptyLayout = it
                    }
                }
                inflate(emptyLayout, parent)
            }
            else -> {
                inflate(layoutId(), parent)
            }
        }
        return CommonViewHolder(view)
    }

    protected abstract fun layoutId(): Int

    /**
     * 获取需要viewHolder的view
     *
     * @param layoutId 布局文件
     */
    private fun inflate(layoutId: Int, group: ViewGroup): View {
        val inflater = LayoutInflater.from(group.context)
        return inflater.inflate(layoutId, group, false)
    }

    override fun onBindViewHolder(holder: CommonViewHolder, position: Int) {
        var index = position
        when (ItemViewType.enumOfValue(getItemViewType(position))) {
            TYPE_HEADER, TYPE_FOOTER -> {
                /*
                 * 当前holder是头部或者底部就直接返回,不需要去设置viewholder的内容
                 */
                return
            }
            TYPE_EMPTY -> {
                //第一次加载数据,不展示空页面
                if (firstLoad) {
                    holder.itemView.visibility = View.INVISIBLE
                } else {
                    holder.itemView.visibility = View.VISIBLE
                }
                return
            }
            else -> {
                if (headerView != null) {
                    /*
                     * 有头部的情况,需要要减1,否则取item的数据会取到当前数据的下一条,
                     * 取出最后一条数据的时候,会报下标溢出
                     */
                    index--
                }
                val m = data[index]
                //设置item的点击回调事件
                holder.itemView.setOnClickListener {
                    onItemClickListener?.let {
                        it.itemClick(recyclerView!!.id, m, index)
                    }
                }
                bindViewHolder(holder, m, position)
            }
        }
    }

    /**
     * 绑定viewHolder的数据
     */
    abstract fun bindViewHolder(holder: CommonViewHolder, m: M, position: Int)

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        this.recyclerView = recyclerView
        val manager = recyclerView.layoutManager
        if (manager is GridLayoutManager) {
            manager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    return when (ItemViewType.enumOfValue(getItemViewType(position))) {
                        TYPE_HEADER, TYPE_EMPTY, TYPE_FOOTER -> {
                            manager.spanCount
                        }
                        else -> {
                            1
                        }
                    }
                }
            }
        }
    }

    override fun onViewAttachedToWindow(holder: CommonViewHolder) {
        super.onViewAttachedToWindow(holder)
        val lp = holder.itemView.layoutParams
        if (lp is StaggeredGridLayoutManager.LayoutParams
                && holder.layoutPosition === 0
        ) {
            lp.isFullSpan = true
        }
    }

    /**
     * item点击事件
     */
    interface OnItemClickListener<M> {
        /**
         * @param id RecyclerView.getId()
         * @param m item下的实体
         * @param position item所在的位置
         */
        fun itemClick(@IdRes id: Int, m: M, position: Int)
    }


}
