package com.catchpig.kotlin_mvvm.mvp.recycle

import com.catchpig.kotlin_mvvm.databinding.ItemUserBinding
import com.catchpig.mvvm.base.adapter.CommonViewHolder
import com.catchpig.mvvm.base.adapter.RecyclerAdapter
import com.catchpig.mvvm.widget.refresh.IPageControl

/**
 *
 * @author TLi2
 **/
class UserAdapter(iPageControl: IPageControl) :
    RecyclerAdapter<User, ItemUserBinding>(iPageControl) {
    override fun itemViewBanding(): Class<ItemUserBinding> {
        return ItemUserBinding::class.java
    }

    override fun bindViewHolder(holder: CommonViewHolder<ItemUserBinding>, m: User, position: Int) {
        holder.viewBanding {
            it.name.text = m.name
        }
        setOnItemClickListener(object : OnItemClickListener<User> {
            override fun itemClick(id: Int, m: User, position: Int) {

            }
        })
    }
}