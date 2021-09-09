package com.catchpig.mvvm.base.activity

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.catchpig.mvvm.apt.KotlinMvpCompiler
import com.catchpig.mvvm.controller.LoadingViewController
import com.catchpig.mvvm.databinding.ViewRootBinding
import com.catchpig.utils.ext.longToast
import com.catchpig.utils.ext.toast
import kotlinx.android.synthetic.main.view_root.*
import java.lang.reflect.ParameterizedType

/**
 * --------------状态栏----------------
 * 请使用注解[com.catchpig.annotation.StatusBar]
 * 想让注解不可用,请设置[com.catchpig.annotation.StatusBar.enabled]为true
 * --------------状态栏----------------
 *
 * --------------标题栏----------------
 * 请使用注解[com.catchpig.annotation.Title]
 * --------------标题栏----------------
 *
 * --------------标题栏右边按钮点击事件---------------
 * 第一个文字按钮点击事件,请方法上实现以下注解
 * @[com.catchpig.annotation.OnClickFirstText]
 *
 * 第一个图标按钮的点击事件,请方法上实现以下注解
 * @[com.catchpig.annotation.OnClickFirstDrawable]
 *
 * 第二个文字按钮的点击事件,请方法上实现以下注解
 * @[com.catchpig.annotation.OnClickSecondText]
 *
 * 第二个图标按钮的点击事件,请方法上实现以下注解
 * @[com.catchpig.annotation.OnClickSecondDrawable]
 * --------------标题栏右边按钮点击事件---------------
 *
 * @author catchpig
 * @date 2019/4/4 00:09
 */
open class BaseActivity<VB : ViewBinding> : AppCompatActivity() {
    val bodyBinding: VB by lazy {
        var type = javaClass.genericSuperclass
        var vbClass: Class<VB> = (type as ParameterizedType).actualTypeArguments[0] as Class<VB>
        val method = vbClass.getDeclaredMethod("inflate", LayoutInflater::class.java)
        method.invoke(this, layoutInflater) as VB
    }
    private val rootBinding: ViewRootBinding by lazy {
        ViewRootBinding.inflate(layoutInflater)
    }
    private var loadingViewController: LoadingViewController? = null

    @CallSuper
    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
    }

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.setContentView(rootBinding.root)
        super.onCreate(savedInstanceState)
        setContentView(bodyBinding.root)
        KotlinMvpCompiler.inject(this)
    }

    override fun setContentView(view: View?) {
        rootBinding.layoutBody.let {
            it.addView(
                view,
                0,
                ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
            )
            loadingViewController = LoadingViewController(this, it)
        }
    }

    fun loadingView(isDialog: Boolean) {
        loadingViewController?.let {
            if (isDialog) {
                it.loadingDialog()
            } else {
                it.loadingView()
            }
        }
    }

    fun hideLoadingView() {
        loadingViewController?.let {
            it.hideLoading()
        }
    }

    fun closeActivity() {
        finish()
    }

    fun toast(text: String, isLong: Boolean) {
        if (isLong) {
            longToast(text)
        } else {
            toast(text)
        }
    }
}
