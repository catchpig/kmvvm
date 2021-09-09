package com.catchpig.kotlin_mvvm.mvp.recycle

import android.view.LayoutInflater
import com.catchpig.kotlin_mvvm.databinding.ItemUserBinding
import com.catchpig.mvvm.base.adapter.CommonViewHolder
import com.catchpig.mvvm.base.adapter.RecyclerAdapter
import com.catchpig.mvvm.widget.refresh.IPageControl

/**
 *
 * @author TLi2
 **/
class UserAdapter(iPageControl: IPageControl):RecyclerAdapter<User,ItemUserBinding>(iPageControl) {
    override fun getViewBanding(layoutInflater: LayoutInflater): ItemUserBinding {
        return ItemUserBinding.inflate(layoutInflater)
    }

    override fun bindViewHolder(holder: CommonViewHolder<ItemUserBinding>, m: User, position: Int) {
        holder.itemViewBinding?.run {
            name.text = m.name
        }
    }
}