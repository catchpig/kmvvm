package com.catchpig.kotlin_mvvm.mvvm.recycle

import com.catchpig.annotation.Adapter
import com.catchpig.kotlin_mvvm.databinding.ItemUserBinding
import com.catchpig.mvvm.base.adapter.CommonViewHolder
import com.catchpig.mvvm.base.adapter.RecyclerAdapter
import com.catchpig.mvvm.widget.refresh.IPageControl

/**
 *
 * @author TLi2
 **/
@Adapter
class UserAdapter(iPageControl: IPageControl) :
    RecyclerAdapter<User, ItemUserBinding>(iPageControl) {

    override fun bindViewHolder(holder: CommonViewHolder<ItemUserBinding>, m: User, position: Int) {
        holder.viewBanding {
            name.text = m.name
        }
    }
}