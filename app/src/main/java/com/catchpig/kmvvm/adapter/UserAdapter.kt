package com.catchpig.kmvvm.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.catchpig.kmvvm.databinding.ItemUserBinding
import com.catchpig.kmvvm.entity.User
import com.catchpig.mvvm.base.adapter.CommonViewHolder
import com.catchpig.mvvm.base.adapter.RecyclerAdapter

/**
 *
 * @author TLi2
 **/
class UserAdapter : RecyclerAdapter<User, ItemUserBinding>() {
    override fun viewBinding(parent: ViewGroup): ItemUserBinding {
        return ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    }

    override fun bindViewHolder(holder: CommonViewHolder<ItemUserBinding>, m: User, position: Int) {
        holder.viewBanding {
            name.text = m.username
        }
    }
}