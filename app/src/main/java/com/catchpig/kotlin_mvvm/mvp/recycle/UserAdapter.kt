package com.catchpig.kotlin_mvvm.mvp.recycle

import com.catchpig.kotlin_mvvm.R
import com.catchpig.mvvm.base.adapter.CommonViewHolder
import com.catchpig.mvvm.base.adapter.RecyclerAdapter
import com.catchpig.mvvm.widget.refresh.IPageControl
import kotlinx.android.synthetic.main.item_user.*

/**
 *
 * @author TLi2
 **/
class UserAdapter(iPageControl: IPageControl):RecyclerAdapter<User>(iPageControl) {
    override fun layoutId(): Int {
        return R.layout.item_user
    }

    override fun bindViewHolder(holder: CommonViewHolder, m: User, position: Int) {
        holder.name.text = m.name
    }
}