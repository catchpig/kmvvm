package com.catchpig.mvvm.base.activity

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.CallSuper
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.contains
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.lifecycleScope
import androidx.viewbinding.ViewBinding
import com.catchpig.mvvm.R
import com.catchpig.mvvm.base.view.BaseView
import com.catchpig.mvvm.controller.LoadingViewController
import com.catchpig.mvvm.controller.StatusBarController
import com.catchpig.mvvm.databinding.ViewRootBinding
import com.catchpig.mvvm.ksp.KotlinMvvmCompiler
import com.catchpig.utils.ext.showSnackBar
import java.lang.reflect.ParameterizedType

/**
 * --------------状态栏----------------<br/>
 * 请使用注解[com.catchpig.annotation.StatusBar]<br/>
 * 想让注解不可用,请设置[com.catchpig.annotation.StatusBar.enabled]为false<br/>
 * --------------状态栏----------------<br/>
 * <br/>
 * --------------标题栏----------------<br/>
 * 请使用注解[com.catchpig.annotation.Title]<br/>
 * --------------标题栏----------------<br/>
 * <br/>
 * --------------标题栏右边按钮点击事件---------------<br/>
 * 第一个文字按钮点击事件,请方法上实现以下注解<br/>
 * @[com.catchpig.annotation.OnClickFirstText]<br/>
 * <br/>
 * 第一个图标按钮的点击事件,请方法上实现以下注解<br/>
 * @[com.catchpig.annotation.OnClickFirstDrawable]<br/>
 * <br/>
 * 第二个文字按钮的点击事件,请方法上实现以下注解<br/>
 * @[com.catchpig.annotation.OnClickSecondText]<br/>
 *
 * 第二个图标按钮的点击事件,请方法上实现以下注解<br/>
 * @[com.catchpig.annotation.OnClickSecondDrawable]<br/>
 * --------------标题栏右边按钮点击事件---------------<br/>
 *
 * @author catchpig
 * @date 2019/4/4 00:09
 */
open class BaseActivity<VB : ViewBinding> : AppCompatActivity(), BaseView {
    protected val bodyBinding: VB by lazy {
        var type = javaClass.genericSuperclass
        var vbClass: Class<VB> = (type as ParameterizedType).actualTypeArguments[0] as Class<VB>
        val method = vbClass.getDeclaredMethod("inflate", LayoutInflater::class.java)
        method.invoke(this, layoutInflater) as VB
    }
    private val rootBinding: ViewRootBinding by lazy {
        ViewRootBinding.inflate(layoutInflater)
    }

    private val loadingViewController: LoadingViewController by lazy {
        LoadingViewController(
            this,
            rootBinding
        )
    }

    private var statusBarController: StatusBarController? = null

    private var failedBinding: ViewBinding? = null

    fun initStatusBarController(statusBarController: StatusBarController) {
        this.statusBarController = statusBarController
    }

    fun resetStatusBar() {
        statusBarController?.checkStatusBar()
    }

    fun getRootBanding(): ViewRootBinding {
        return rootBinding
    }

    inline fun <reified FVB : ViewBinding> failedBinding(block: FVB.() -> Unit) {
        getFailedBinding()?.let {
            (it as FVB).run(block)
        }
    }

    override fun getFailedBinding(): ViewBinding? {
        if (failedBinding == null) {
            failedBinding = KotlinMvvmCompiler.globalConfig().getFailedBinding(layoutInflater, this)
        }
        return failedBinding
    }

    override fun scope(): LifecycleCoroutineScope {
        return lifecycleScope
    }

    override fun showFailedView() {
        getFailedBinding()?.let {
            rootBinding {
                if (!layoutBody.contains(it.root)) {
                    layoutBody.addView(it.root)
                }
            }
        }
    }

    override fun removeFailedView() {
        failedBinding?.let {
            rootBinding {
                if (layoutBody.contains(it.root)) {
                    layoutBody.removeView(it.root)
                }
            }
        }
    }

    open fun rootBinding(block: ViewRootBinding.() -> Unit) {
        rootBinding.run(block)
    }

    open fun bodyBinding(block: VB.() -> Unit) {
        bodyBinding.run(block)
    }

    @CallSuper
    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
    }

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.setContentView(rootBinding.root)
        super.onCreate(savedInstanceState)
        setContentView(bodyBinding.root)
        KotlinMvvmCompiler.inject(this)
    }

    override fun setContentView(view: View?) {
        rootBinding.layoutBody.let {
            it.addView(
                view, 0, ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT
                )
            )
        }
    }

    /**
     * 改变title文字
     */
    fun updateTitle(title: String) {
        var titleText = rootBinding.root.findViewById<TextView>(R.id.title_text)
        titleText.text = title
    }

    /**
     * 改变title文字
     */
    fun updateTitle(@StringRes title: Int) {
        var titleText = rootBinding.root.findViewById<TextView>(R.id.title_text)
        titleText.setText(title)
    }

    override fun snackBar(text: CharSequence, gravity: Int) {
        bodyBinding.root.showSnackBar(text, R.drawable.snackbar_bg, gravity)
    }

    override fun snackBar(@StringRes textRes: Int, gravity: Int) {
        bodyBinding.root.showSnackBar(textRes, R.drawable.snackbar_bg, gravity)
    }

    override fun loadingDialog() {
        loadingViewController.let {
            it.loadingDialog()
        }
    }

    override fun loadingView() {
        loadingViewController.let {
            it.loadingView()
        }
    }

    override fun hideLoading() {
        loadingViewController.let {
            it.hideLoading()
        }
    }

    fun closeActivity() {
        finish()
    }
}
