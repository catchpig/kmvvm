package com.catchpig.kmvvm.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.catchpig.kmvvm.article.ArticleFragment
import com.catchpig.kmvvm.index.IndexFragment

class MainAdapter(fragmentManager: FragmentManager) :
    FragmentStatePagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    private var fragments = mutableListOf<Fragment>()
    private val tiitles = listOf("首页", "文章")

    init {
        fragments.add(IndexFragment.newInstance())
        fragments.add(ArticleFragment.newInstance())
    }

    override fun getCount(): Int {
        return fragments.size
    }

    override fun getItem(position: Int): Fragment {
        return fragments[position]
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return tiitles[position]
    }
}