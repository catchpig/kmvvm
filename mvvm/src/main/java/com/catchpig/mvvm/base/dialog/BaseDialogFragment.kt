package com.catchpig.mvvm.base.dialog

import android.content.DialogInterface
import android.content.DialogInterface.OnShowListener
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.FrameLayout
import androidx.annotation.CallSuper
import androidx.fragment.app.DialogFragment
import androidx.viewbinding.ViewBinding
import java.lang.reflect.ParameterizedType

open class BaseDialogFragment<VB : ViewBinding> : DialogFragment(), OnShowListener {
    private var onDismissListener: OnDismissListener? = null
    private var onShowListener: OnShowListener? = null

    fun setOnShowListener(onShowListener: OnShowListener) {
        this.onShowListener = onShowListener
    }

    fun setOnShowListener(listener: (dialog: DialogInterface) -> Unit) {
        setOnShowListener(object : OnShowListener {
            override fun onShow(dialog: DialogInterface) {
                listener(dialog)
            }
        })
    }

    fun setOnDismissListener(onDismissListener: OnDismissListener) {
        this.onDismissListener = onDismissListener
    }

    fun setOnDismissListener(listener: (dialog: DialogInterface) -> Unit) {
        setOnDismissListener(object : OnDismissListener {
            override fun onDismiss(dialog: DialogInterface) {
                listener(dialog)
            }
        })
    }

    protected lateinit var bodyBinding: VB

    @CallSuper
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dialog?.run {
            /**
             * 设置无标题
             */
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            /**
             * 设置背景透明,可以去除弹窗默认边距
             */
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
        return onCreateView(layoutInflater)
    }

    private fun onCreateView(inflater: LayoutInflater): View {
        val rootView = FrameLayout(requireContext())
        val type = javaClass.genericSuperclass
        val vbClass: Class<VB> = (type as ParameterizedType).actualTypeArguments[0] as Class<VB>
        val method = vbClass.getDeclaredMethod(
            "inflate",
            LayoutInflater::class.java,
            ViewGroup::class.java,
            Boolean::class.java
        )
        bodyBinding = method.invoke(this, inflater, rootView, true) as VB
        return rootView
    }


    @CallSuper
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireDialog().run {
            setOnShowListener(this@BaseDialogFragment)
        }
    }

    override fun onShow(dialog: DialogInterface) {
        onShowListener?.onShow(dialog)
    }

    override fun onDismiss(dialog: DialogInterface) {
        onDismissListener?.onDismiss(dialog)
        super.onDismiss(dialog)
    }

    interface OnDismissListener {
        fun onDismiss(dialog: DialogInterface)
    }

    interface OnShowListener {
        fun onShow(dialog: DialogInterface)
    }
}