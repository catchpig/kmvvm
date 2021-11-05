package com.catchpig.kmvvm.recycle

import com.catchpig.annotation.Adapter
import com.catchpig.kmvvm.databinding.ItemUserBinding
import com.catchpig.kmvvm.entity.User
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