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
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.viewbinding.ViewBinding
import com.catchpig.mvvm.R
import com.catchpig.mvvm.base.view.BaseView
import com.catchpig.mvvm.controller.LoadingViewController
import com.catchpig.mvvm.controller.StatusBarController
import com.catchpig.mvvm.databinding.ViewRootBinding
import com.catchpig.mvvm.ksp.KotlinMvvmCompiler
import com.catchpig.utils.ext.showSnackBar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.lang.reflect.ParameterizedType
import kotlin.coroutines.CoroutineContext

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
@Suppress("UNCHECKED_CAST")
open class BaseActivity<VB : ViewBinding> : AppCompatActivity(), BaseView {
    protected val bodyBinding: VB by lazy {
        val vbClass =
            (javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[0] as Class<VB>
        vbClass.getDeclaredMethod("inflate", LayoutInflater::class.java)
            .invoke(this, layoutInflater) as VB
    }
    val rootBinding: ViewRootBinding by lazy {
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

    inline fun <reified FVB : ViewBinding> failedBinding(block: FVB.() -> Unit) {
        (getFailedBinding() as? FVB)?.block()
    }

    override fun getFailedBinding(): ViewBinding? {
        return failedBinding ?: KotlinMvvmCompiler.globalConfig()
            .getFailedBinding(layoutInflater, this)
            .also { failedBinding = it }
    }

    override fun launcherOnLifecycle(
        context: CoroutineContext,
        block: suspend CoroutineScope.() -> Unit
    ): Job {
        return lifecycleScope.launch(context) {
            block()
        }
    }

    override fun repeatLauncherOnLifecycle(
        context: CoroutineContext,
        state: Lifecycle.State,
        block: suspend CoroutineScope.() -> Unit
    ): Job {
        return lifecycleScope.launch(context) {
            repeatOnLifecycle(state) {
                block()
            }
        }
    }

    override fun showFailedView() {
        getFailedBinding()?.let { binding ->
            rootBinding {
                if (!layoutBody.contains(binding.root)) {
                    layoutBody.addView(binding.root)
                }
            }
        }
    }

    override fun removeFailedView() {
        failedBinding?.let { binding ->
            rootBinding {
                if (layoutBody.contains(binding.root)) {
                    layoutBody.removeView(binding.root)
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

    private val titleTextView: TextView?
        get() = rootBinding.root.findViewById(R.id.title_text)

    /** 改变 title 文字 */
    fun updateTitle(title: String) {
        titleTextView?.text = title
    }

    /** 改变 title 文字 */
    fun updateTitle(@StringRes title: Int) {
        titleTextView?.setText(title)
    }

    override fun snackBar(text: CharSequence, gravity: Int) {
        bodyBinding.root.showSnackBar(text, R.drawable.snackbar_bg, gravity)
    }

    override fun snackBar(@StringRes textRes: Int, gravity: Int) {
        bodyBinding.root.showSnackBar(textRes, R.drawable.snackbar_bg, gravity)
    }

    override fun loadingDialog() {
        loadingViewController.loadingDialog()
    }

    override fun loadingView() {
        loadingViewController.loadingView()
    }

    override fun hideLoading() {
        loadingViewController.hideLoading()
    }

    fun closeActivity() {
        finish()
    }
}
