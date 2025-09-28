package com.catchpig.kmvvm.main.view

import android.view.WindowInsets
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.viewpager.widget.ViewPager
import com.catchpig.annotation.StatusBar
import com.catchpig.kmvvm.adapter.MainAdapter
import com.catchpig.kmvvm.databinding.ActivityMainBinding
import com.catchpig.kmvvm.entity.UserSharedPrefs
import com.catchpig.kmvvm.main.viewmodel.MainViewModel
import com.catchpig.mvvm.base.activity.BaseVMActivity
import com.catchpig.mvvm.ext.lifecycleNull
import com.catchpig.utils.ext.logd
import com.gyf.immersionbar.ktx.immersionBar
import kotlinx.coroutines.flow.flow

@StatusBar(transparent = true)
class MainActivity : BaseVMActivity<ActivityMainBinding, MainViewModel>(),
    ViewPager.OnPageChangeListener {
    companion object {
        private const val TAG = "MainActivity"
    }

    private lateinit var mainAdapter: MainAdapter
    override fun initParam() {
        immersionBar {
            statusBarDarkFont(true, 0.5f)
        }
    }

    override fun initView() {
        ViewCompat.setOnApplyWindowInsetsListener(window.decorView) { v, insets ->
            val statusBarInsets = insets.getInsets(WindowInsetsCompat.Type.statusBars())
            statusbavi
            insets
        }
        UserSharedPrefs.isMan()
        mainAdapter = MainAdapter(supportFragmentManager)
        bodyBinding.run {
            viewPager.adapter = mainAdapter
            tabLayout.setupWithViewPager(viewPager)
            viewPager.addOnPageChangeListener(this@MainActivity)
            viewPager.currentItem = 0
        }
    }

    override fun initFlow() {
        "initFlow".logd(TAG)
        flow<Int?> {
            emit(null)
        }.lifecycleNull(this) {
            "initFlow->flow:${this}".logd(TAG)
        }
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
