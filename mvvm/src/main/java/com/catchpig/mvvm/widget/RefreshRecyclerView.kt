package com.catchpig.mvvm.widget

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.catchpig.mvvm.R
import com.catchpig.mvvm.base.adapter.RecyclerAdapter
import com.catchpig.mvvm.widget.refresh.RefreshLayoutWrapper


class RefreshRecyclerView(
    context: Context,
    attrs: AttributeSet
) : RefreshLayoutWrapper(context, attrs) {
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

    fun setAdapter(adapter: RecyclerView.Adapter<*>) {
        recyclerView.adapter = adapter
        if (adapter is RecyclerAdapter<*, *>) {
            adapter.iPageControl = this
        }
    }
}