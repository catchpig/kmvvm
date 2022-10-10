package com.catchpig.kmvvm.adapter

import com.catchpig.annotation.Adapter
import com.catchpig.kmvvm.databinding.ItemUserBinding
import com.catchpig.kmvvm.entity.User
import com.catchpig.mvvm.base.adapter.CommonViewHolder
import com.catchpig.mvvm.base.adapter.RecyclerAdapter

/**
 *
 * @author TLi2
 **/
@Adapter
class UserAdapter : RecyclerAdapter<User, ItemUserBinding>() {

    override fun bindViewHolder(holder: CommonViewHolder<ItemUserBinding>, m: User, position: Int) {
        holder.viewBanding {
            name.text = m.username
        }
    }
}