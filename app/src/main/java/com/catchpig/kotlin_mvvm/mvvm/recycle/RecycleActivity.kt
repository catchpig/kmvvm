package com.catchpig.kotlin_mvvm.mvvm.recycle

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.catchpig.annotation.StatusBar
import com.catchpig.kotlin_mvvm.R
import com.catchpig.kotlin_mvvm.databinding.ActivityRecycleBinding
import com.catchpig.mvvm.base.activity.BaseActivity
import com.catchpig.utils.ext.logd
import com.gyf.immersionbar.ktx.immersionBar
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.subscribers.ResourceSubscriber
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList

@StatusBar(enabled = true)
class RecycleActivity : BaseActivity<ActivityRecycleBinding>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initToolBar()
        initAdapter()
        Glide.with(this).load(getPic()).placeholder(R.drawable.fullscreen).into(bodyBinding.image)
    }

    private fun initAdapter() {
        var userAdapter = UserAdapter(bodyBinding.refresh)
        userAdapter.setOnItemClickListener { _, m, _ ->
            "${m.name}".logd("adsd")
        }
        var linearLayoutManager = LinearLayoutManager(this)
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        bodyBinding.recycleView.layoutManager = linearLayoutManager
        bodyBinding.recycleView.adapter = userAdapter
        userAdapter.addHeaderView(R.layout.layout_header)
//        userAdapter.headerView {
//            header_name.text = "我是头部"
//        }
        bodyBinding.refresh.setOnRefreshLoadMoreListener { nextPageIndex ->
            Flowable.timer(3, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : ResourceSubscriber<Long>() {
                    override fun onComplete() {
                        "onComplete".logd()
                    }

                    override fun onNext(t: Long?) {
                        "onNext".logd()
                        var data: MutableList<User> = ArrayList()
                        var count = if (bodyBinding.refresh.nextPageIndex == 3) {
                            15
                        } else {
                            16
                        }
                        for (i in 1..count) {
                            data.add(User("姓名$i"))
                        }
                        userAdapter.autoUpdateList(data)
                    }

                    override fun onError(t: Throwable?) {
                        "onError".logd()
                        bodyBinding.refresh.updateError()
                    }
                })
        }
        bodyBinding.refresh.autoRefresh()
    }

    private fun initToolBar() {
        immersionBar {
            titleBar(bodyBinding.detailToolbar)
        }
        setSupportActionBar(bodyBinding.detailToolbar)
    }

    private fun getPic(): String {
        val random = Random()
        return "http://106.14.135.179/ImmersionBar/" + random.nextInt(40) + ".jpg"
    }
}