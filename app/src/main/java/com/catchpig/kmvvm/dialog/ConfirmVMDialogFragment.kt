package com.catchpig.kmvvm.dialog

import android.os.Bundle
import android.view.View
import com.catchpig.kmvvm.databinding.DialogConfirmBinding
import com.catchpig.kmvvm.dialog.viewmodel.ConfirmViewModel
import com.catchpig.mvvm.base.dialog.BaseVMDialogFragment

class ConfirmVMDialogFragment : BaseVMDialogFragment<DialogConfirmBinding, ConfirmViewModel>() {
    companion object {
        fun newInstance(): ConfirmVMDialogFragment {
            return ConfirmVMDialogFragment()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireDialog().run {
            setCanceledOnTouchOutside(true)
            setCancelable(true)
        }
    }

    override fun initParam() {
    }

    override fun initView() {
        bodyBinding.confirm.setOnClickListener {
            dismiss()
        }
    }

    override fun initFlow() {
    }

}