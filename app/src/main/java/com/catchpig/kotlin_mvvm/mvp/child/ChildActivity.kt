package com.catchpig.kotlin_mvvm.mvp.child

import android.view.View
import com.catchpig.annotation.*
import com.catchpig.kotlin_mvvm.R
import com.catchpig.mvvm.base.activity.BaseActivity
import com.catchpig.utils.ext.toast
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Flowable
import java.util.concurrent.TimeUnit

@Title(R.string.child_title)
@StatusBar
class ChildActivity : BaseActivity(){
    @OnClickFirstDrawable(R.drawable.more)
    fun clickFirstDrawable(v: View) {
        toast(" 第一个图标按钮点击生效")

    }
    @OnClickFirstText(R.string.more)
    fun clickFirstText() {
        toast("第一个文字按钮点击生效")

    }

    /**
     * dialog形式的loading
     */
    fun loadingDialog(v: View){
        loadingView(true)
        Flowable.timer(5,TimeUnit.SECONDS).observeOn(AndroidSchedulers.mainThread()).subscribe {
            hideLoadingView()
        }
    }

    /**
     * 标题栏以下的loading
     */
    fun loadingExtTitle(v: View){
        loadingView(false)
        Flowable.timer(5,TimeUnit.SECONDS).observeOn(AndroidSchedulers.mainThread()).subscribe {
            hideLoadingView()
        }
    }
    override fun layoutId(): Int {
        return R.layout.activity_child
    }
}
