package com.catchpig.mvvm.base.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

/**
 * @author catchpig
 * @date 2019/7/21 0021
 */
class CommonViewHolder<VB : ViewBinding>(itemView: View) :
    RecyclerView.ViewHolder(itemView) {
    var itemViewBinding: VB? = null
    constructor(itemViewBinding: VB) : this(itemViewBinding.root){
        this.itemViewBinding = itemViewBinding
    }

}