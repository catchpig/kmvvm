package com.catchpig.kmvvm.adapter

import com.catchpig.annotation.Adapter
import com.catchpig.kmvvm.databinding.ItemArticleBinding
import com.catchpig.kmvvm.entity.Article
import com.catchpig.mvvm.base.adapter.CommonViewHolder
import com.catchpig.mvvm.base.adapter.RecyclerAdapter
import com.catchpig.mvvm.widget.refresh.IPageControl

@Adapter
class ArticleAdapter(iPageControl: IPageControl) :
    RecyclerAdapter<Article, ItemArticleBinding>(iPageControl) {

    override fun bindViewHolder(
        holder: CommonViewHolder<ItemArticleBinding>,
        m: Article,
        position: Int
    ) {
        holder.viewBanding {
            title.text = m.title
        }
    }
}