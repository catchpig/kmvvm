package com.catchpig.mvvm.base.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.annotation.IntRange
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.viewbinding.ViewBinding
import com.catchpig.mvvm.ksp.KotlinMvvmCompiler
import com.catchpig.mvvm.base.adapter.RecyclerAdapter.ItemViewType.*


/**
 * RecyclerViewAdapter基类
 * @author catchpig
 * @date 2017年12月22日13:43:56
 */

abstract class RecyclerAdapter<M, VB : ViewBinding> :
    RecyclerView.Adapter<CommonViewHolder<VB>>() {
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
                values().forEach {
                    if (it.value == value) {
                        return it
                    }
                }
                return TYPE_NORMAL
            }
        }
    }

    private var data: MutableList<M> = ArrayList()

    /**
     * 头部
     */
    var headerView: View? = null

    /**
     * 底部
     */
    var footerView: View? = null

    /**
     * 是否展示空页面
     */
    private var showEmpty: Boolean = false

    var emptyView: View? = null

    /**
     * 是否是第一次加载数据
     */
    private var firstLoad = true

    private var onItemClickListener: OnItemClickListener<M>? = null

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener<M>) {
        this.onItemClickListener = onItemClickListener
    }

    fun setOnItemClickListener(listener: (id: Int, m: M, position: Int) -> Unit) {
        object : OnItemClickListener<M> {
            override fun itemClick(id: Int, m: M, position: Int) {
                listener(id, m, position)
            }

        }.also { onItemClickListener = it }
    }

    fun setOnItemClickListener(listener: (m: M) -> Unit) {
        object : OnItemClickListener<M> {
            override fun itemClick(id: Int, m: M, position: Int) {
                listener(m)
            }
        }.also { onItemClickListener = it }
    }

    fun setOnItemClickListener(listener: (m: M, position: Int) -> Unit) {
        object : OnItemClickListener<M> {
            override fun itemClick(id: Int, m: M, position: Int) {
                listener(m, position)
            }
        }.also { onItemClickListener = it }
    }

    inline fun <reified EVB : ViewBinding> emptyView(empty: EVB.() -> Unit) {
        val viewBindingClass = EVB::class.java
        val method = viewBindingClass.getDeclaredMethod(
            "inflate",
            LayoutInflater::class.java,
            ViewGroup::class.java,
            Boolean::class.java
        )
        val emptyViewBinding = method.invoke(
            this,
            LayoutInflater.from(recyclerView.context),
            recyclerView,
            false
        ) as EVB
        emptyView = emptyViewBinding.root
        empty(emptyViewBinding)
    }

    fun addHeaderView(@LayoutRes layoutId: Int) {
        headerView =
            LayoutInflater.from(recyclerView.context).inflate(layoutId, recyclerView, false)
        notifyDataSetChanged()
    }

    inline fun <reified HVB : ViewBinding> headerView(header: HVB.() -> Unit) {
        val viewBindingClass = HVB::class.java
        val method = viewBindingClass.getDeclaredMethod(
            "inflate",
            LayoutInflater::class.java,
            ViewGroup::class.java,
            Boolean::class.java
        )
        val headerViewBinding = method.invoke(
            this,
            LayoutInflater.from(recyclerView.context),
            recyclerView,
            false
        ) as HVB
        headerView = headerViewBinding.root
        header(headerViewBinding)
    }

    inline fun <reified FVB : ViewBinding> footerView(header: FVB.() -> Unit) {
        val viewBindingClass = FVB::class.java
        val method = viewBindingClass.getDeclaredMethod(
            "inflate",
            LayoutInflater::class.java,
            ViewGroup::class.java,
            Boolean::class.java
        )
        val headerViewBinding = method.invoke(
            this,
            LayoutInflater.from(recyclerView.context),
            recyclerView,
            false
        ) as FVB
        footerView = headerViewBinding.root
        header(headerViewBinding)
    }

    fun addFooterView(@LayoutRes layoutId: Int) {
        footerView =
            LayoutInflater.from(recyclerView.context).inflate(layoutId, recyclerView, false)
        notifyDataSetChanged()
    }

    /**
     * 获取数据源
     * @return MutableList<M>
     */
    fun getData(): MutableList<M> {
        return data
    }

    /**
     * 设置数据源
     * @param list MutableList<M>?
     */
    fun set(list: MutableList<M>?) {
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


    lateinit var recyclerView: RecyclerView
        private set

    /**
     * 获取单个数据源
     * @param position Int 下表
     * @return M?
     */
    fun get(position: Int): M? {
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
    fun add(list: MutableList<M>?) {
        list?.let {
            data.addAll(it)
        }
        notifyDataSetChanged()
    }

    /**
     *
     * @param position Int
     * @return Int
     */
    override fun getItemViewType(position: Int): Int {
        return if (showEmpty) {
            if (position == 0 && headerView != null) {
                //当前view是头部信息
                TYPE_HEADER.value
            } else if (position == 0 && headerView == null) {
                //当前数据空位,展示空页面
                TYPE_EMPTY.value
            } else if (position == 1 && headerView != null) {
                //当前数据空位,展示空页面
                TYPE_EMPTY.value
            } else {
                getCenterViewType(position)
            }
        } else {
            if (position == 0 && headerView != null) {
                //当前view是头部信息
                TYPE_HEADER.value
            } else if (position == (itemCount - 1) && headerView != null && footerView != null) {
                //当前view是底部信息
                TYPE_FOOTER.value
            } else if (position == (itemCount - 1) && headerView == null && footerView != null) {
                //当前view是底部信息
                TYPE_FOOTER.value
            } else {
                getCenterViewType(position)
            }
        }
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommonViewHolder<VB> {
        val view = when (ItemViewType.enumOfValue(viewType)) {
            TYPE_HEADER -> {
                headerView!!
            }
            TYPE_FOOTER -> {
                footerView!!
            }
            TYPE_EMPTY -> {
                if (emptyView == null) {
                    emptyView =
                        KotlinMvvmCompiler.globalConfig().getRecyclerEmptyBanding(parent).root
                }
                emptyView!!
            }
            else -> {
                return CommonViewHolder(viewBinding(parent))
            }
        }
        return CommonViewHolder(view)
    }

    abstract fun viewBinding(parent: ViewGroup): VB

    override fun onBindViewHolder(holder: CommonViewHolder<VB>, position: Int) {
        var index = position
        when (ItemViewType.enumOfValue(getItemViewType(position))) {
            TYPE_HEADER, TYPE_FOOTER -> {
                /*
                 * 当前holder是头部或者底部就直接返回,不需要去设置ViewHolder的内容
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
                        it.itemClick(recyclerView.id, m, index)
                    }
                }
                bindViewHolder(holder, m, position)
            }
        }
    }

    /**
     * 绑定viewHolder的数据
     */
    abstract fun bindViewHolder(holder: CommonViewHolder<VB>, m: M, position: Int)

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

    override fun onViewAttachedToWindow(holder: CommonViewHolder<VB>) {
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
