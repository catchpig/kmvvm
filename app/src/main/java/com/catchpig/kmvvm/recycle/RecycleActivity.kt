package com.catchpig.kmvvm.recycle

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.catchpig.annotation.StatusBar
import com.catchpig.kmvvm.R
import com.catchpig.kmvvm.databinding.ActivityRecycleBinding
import com.catchpig.kmvvm.databinding.LayoutFooterBinding
import com.catchpig.kmvvm.databinding.LayoutHeaderBinding
import com.catchpig.kmvvm.entity.User
import com.catchpig.mvvm.apt.KotlinMvvmCompiler
import com.catchpig.mvvm.base.activity.BaseActivity
import com.gyf.immersionbar.ktx.immersionBar
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Flowable
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
        var userAdapter = UserAdapter()
        userAdapter.setOnItemClickListener { _, m, _ ->
            snackBar(m.name)
        }
        bodyBinding.refresh.run {
            var linearLayoutManager = LinearLayoutManager(this@RecycleActivity)
            linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
            setLayoutManager(linearLayoutManager)
            setAdapter(userAdapter)
            setOnRefreshLoadMoreListener { nextPageIndex ->
                Flowable.timer(3, TimeUnit.SECONDS).observeOn(AndroidSchedulers.mainThread())
                    .subscribe {
                        var data: MutableList<User> = ArrayList()
                        val pageSize = KotlinMvvmCompiler.globalConfig().getPageSize()
                        var count = if (nextPageIndex == 3) {
                            pageSize - 1
                        } else {
                            pageSize
                        }
                        for (i in 1..count) {
                            data.add(User("姓名$i"))
                        }
                        userAdapter.autoUpdateList(data)
                    }
            }
            autoRefresh()
        }

//        bodyBinding.recyclerView.layoutManager = linearLayoutManager
//        bodyBinding.recyclerView.adapter = userAdapter
//        userAdapter.emptyView<LayoutEmptyBinding> {
//            this.root.setOnClickListener {
//                snackbar("数据飞了")
//            }
//        }
        userAdapter.headerView<LayoutHeaderBinding> {
            headerName.text = "我是头部"
        }
        userAdapter.footerView<LayoutFooterBinding> {
            footerName.text = "我是底部"
        }
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
