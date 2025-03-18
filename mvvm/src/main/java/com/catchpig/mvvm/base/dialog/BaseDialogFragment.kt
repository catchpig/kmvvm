package com.catchpig.mvvm.base.dialog

import android.content.DialogInterface
import android.content.DialogInterface.OnShowListener
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

    protected val bodyBinding: VB by lazy {
        val type = javaClass.genericSuperclass
        val vbClass: Class<VB> = (type as ParameterizedType).actualTypeArguments[0] as Class<VB>
        val method = vbClass.getDeclaredMethod("inflate", LayoutInflater::class.java)
        method.invoke(this, layoutInflater) as VB
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return bodyBinding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireDialog().setOnShowListener(this)
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