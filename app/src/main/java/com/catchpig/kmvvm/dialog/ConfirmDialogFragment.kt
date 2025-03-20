package com.catchpig.kmvvm.dialog

import android.os.Bundle
import android.view.View
import com.catchpig.kmvvm.databinding.DialogConfirmBinding
import com.catchpig.mvvm.base.dialog.BaseDialogFragment

class ConfirmDialogFragment : BaseDialogFragment<DialogConfirmBinding>() {
    companion object {
        fun newInstance(): ConfirmDialogFragment {
            return ConfirmDialogFragment()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireDialog().run {
            setCanceledOnTouchOutside(true)
            setCancelable(true)
        }
        bodyBinding.confirm.setOnClickListener {
            dismiss()
        }
    }
}