package com.catchpig.kmvvm.main.view

import androidx.viewpager.widget.ViewPager
import com.catchpig.annotation.StatusBar
import com.catchpig.kmvvm.adapter.MainAdapter
import com.catchpig.kmvvm.databinding.ActivityMainBinding
import com.catchpig.kmvvm.main.viewmodel.MainViewModel
import com.catchpig.mvvm.base.activity.BaseVMActivity
import com.gyf.immersionbar.ktx.immersionBar

@StatusBar(transparent = true)
class MainActivity : BaseVMActivity<ActivityMainBinding, MainViewModel>(),
    ViewPager.OnPageChangeListener {

    private lateinit var mainAdapter: MainAdapter
    override fun initParam() {
        immersionBar {
            statusBarDarkFont(true, 0.5f)
        }
    }

    override fun initView() {
        mainAdapter = MainAdapter(supportFragmentManager)
        bodyBinding.run {
            viewPager.adapter = mainAdapter
            tabLayout.setupWithViewPager(viewPager)
            viewPager.addOnPageChangeListener(this@MainActivity)
            viewPager.currentItem = 0
        }
    }

    override fun initFlow() {

    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

    }

    override fun onPageSelected(position: Int) {
        when (position) {
            0 -> {
                immersionBar {
                    statusBarDarkFont(true, 0.5f)
                }
            }
            1 -> {
                immersionBar {
                    statusBarDarkFont(true, 0.2f)
                }
            }
            else -> {
            }
        }
    }

    override fun onPageScrollStateChanged(state: Int) {

    }
}
