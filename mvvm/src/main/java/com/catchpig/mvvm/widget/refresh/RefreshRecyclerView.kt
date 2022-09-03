package com.catchpig.mvvm.widget.refresh

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.catchpig.mvvm.R
import com.catchpig.mvvm.base.adapter.RecyclerAdapter
import com.catchpig.utils.ext.logi
import com.scwang.smart.refresh.layout.constant.RefreshState


class RefreshRecyclerView(
    context: Context,
    attrs: AttributeSet
) : RefreshLayoutWrapper(context, attrs) {
    companion object {
        private const val TAG = "RefreshRecyclerView"
    }

    private var recyclerBackground: Int
    private val recyclerView: RecyclerView

    init {
        val typedArray =
            context.obtainStyledAttributes(attrs, R.styleable.RefreshRecyclerView)
        recyclerBackground = typedArray.getColor(
            R.styleable.RefreshRecyclerView_recycler_background,
            Color.TRANSPARENT
        )
        typedArray.recycle()
        recyclerView = RecyclerView(context, attrs)
        recyclerView.layoutParams =
            LayoutParams(LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        addView(recyclerView)
        recyclerView.setBackgroundColor(recyclerBackground)
    }

    fun setLayoutManager(layoutManage: RecyclerView.LayoutManager) {
        recyclerView.layoutManager = layoutManage
    }

    fun <M> setAdapter(adapter: RecyclerAdapter<M, out ViewBinding>) {
        recyclerView.adapter = adapter
    }

    fun <M> updateData(data: MutableList<M>?) {
        val adapter = recyclerView.adapter as RecyclerAdapter<M, out ViewBinding>
        when (state) {
            RefreshState.Refreshing -> {
                adapter.set(data)
            }
            RefreshState.Loading -> {
                adapter.add(data)
            }
            else -> {
                "state:$state".logi(TAG)
            }
        }
        updateSuccess(data)
    }
}