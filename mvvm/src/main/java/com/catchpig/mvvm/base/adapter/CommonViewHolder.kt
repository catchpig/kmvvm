package com.catchpig.mvvm.base.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.extensions.LayoutContainer

/**
 * @author catchpig
 * @date 2019/7/21 0021
 */
class CommonViewHolder(itemView:View) : RecyclerView.ViewHolder(itemView),LayoutContainer {
    override val containerView = itemView
}