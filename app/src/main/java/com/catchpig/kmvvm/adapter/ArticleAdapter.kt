package com.catchpig.kmvvm.adapter

import com.catchpig.kmvvm.databinding.ItemArticleBinding
import com.catchpig.kmvvm.entity.Article
import com.catchpig.mvvm.base.adapter.CommonViewHolder
import com.catchpig.mvvm.base.adapter.RecyclerAdapter

class ArticleAdapter : RecyclerAdapter<Article, ItemArticleBinding>() {

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