package com.catchpig.mvvm.controller

import android.app.Activity
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.catchpig.mvvm.R
import com.catchpig.mvvm.apt.KotlinMvvmCompiler
import com.catchpig.mvvm.config.Config
import com.catchpig.mvvm.databinding.LayoutTitleBarBinding
import com.catchpig.mvvm.entity.TitleMenuParam
import com.catchpig.mvvm.entity.TitleParam
import com.catchpig.mvvm.interfaces.IGlobalConfig
import com.catchpig.utils.ext.colorResToInt

/**
 * 标题栏处理器
 * @author catchpig
 * @date 2019/8/18 00:18
 */
class TitleBarController(
    private val activity: Activity,
    private val title: TitleParam
) : View.OnClickListener {
    private val globalConfig: IGlobalConfig = KotlinMvvmCompiler.globalConfig()

    fun initTitleBar(view: View) {
        val titleBarBinding = LayoutTitleBarBinding.bind(view)
        titleBarBinding.titleBar.let {
            val titleBarHeight =
                activity.resources.getDimensionPixelOffset(globalConfig.getTitleHeight())
            it.post {
                var layoutParams = it.layoutParams
                layoutParams.height = titleBarHeight
                it.layoutParams = layoutParams
            }
            it.visibility = View.VISIBLE
            initListener(titleBarBinding)
            //设置背景色
            drawBackground(it)
        }
        //设置文字颜色
        drawTextColor(titleBarBinding)
        //设置返回按钮图标
        drawBackIcon(titleBarBinding.backIcon)
        if (title.value != Config.NO_ASSIGNMENT) {
            titleBarBinding.titleText.setText(title.value)
        }

        drawLine(titleBarBinding.line)
    }

    private fun drawLine(line: View) {
        if (globalConfig.isShowTitleLine()) {
            line.visibility = View.VISIBLE
            val lineColor = globalConfig.getTitleLineColor()
            if (lineColor != 0) {
                line.setBackgroundResource(lineColor)
            }
        } else {
            line.visibility = View.GONE
        }
    }

    /**
     * 绘制右边按钮
     */
    private fun drawTitleMenu(titleBar: FrameLayout, titleMenu: TitleMenuParam) {
        //右边第一个文字按钮
        titleBar.findViewById<TextView>(R.id.rightFirstText).run {
            if (titleMenu.rightFirstText != Config.NO_ASSIGNMENT) {
                visibility = View.VISIBLE
                setText(titleMenu.rightFirstText)
            } else {
                visibility = View.GONE
            }
        }
        //右边第二个文字按钮
        titleBar.findViewById<TextView>(R.id.rightSecondText).run {
            if (titleMenu.rightSecondText != Config.NO_ASSIGNMENT) {
                visibility = View.VISIBLE
                setText(titleMenu.rightSecondText)
            } else {
                visibility = View.GONE
            }
        }
        //右边第一个图标按钮
        titleBar.findViewById<ImageView>(R.id.rightFirstDrawable).run {
            if (titleMenu.rightFirstDrawable != Config.NO_ASSIGNMENT) {
                visibility = View.VISIBLE
                setImageResource(titleMenu.rightFirstDrawable)
            } else {
                visibility = View.GONE
            }
        }
        //右边第二个图标按钮
        titleBar.findViewById<ImageView>(R.id.rightSecondDrawable).run {
            if (titleMenu.rightSecondDrawable != Config.NO_ASSIGNMENT) {
                visibility = View.VISIBLE
                setImageResource(titleMenu.rightSecondDrawable)
            } else {
                visibility = View.GONE
            }
        }
    }

    /**
     * 初始化监听器
     */
    private fun initListener(titleBarBinding: LayoutTitleBarBinding) {
        titleBarBinding.backIcon.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        when (view!!.id) {
            //返回
            R.id.back_icon -> activity.finish()
        }
    }

    /**
     * 设置背景色
     */
    private fun drawBackground(titleBar: RelativeLayout) {
        val backgroundColor = if (title.backgroundColor == Config.NO_ASSIGNMENT) {
            globalConfig.getTitleBackground()
        } else {
            title.backgroundColor
        }
        titleBar.setBackgroundResource(backgroundColor)
    }

    /**
     * 设置文字颜色
     */
    private fun drawTextColor(titleBarBinding: LayoutTitleBarBinding) {
        var textColor = if (title.textColor == Config.NO_ASSIGNMENT) {
            activity.colorResToInt(globalConfig.getTitleTextColor())
        } else {
            activity.colorResToInt(title.textColor)
        }
        titleBarBinding.titleText.setTextColor(textColor)
        titleBarBinding.rightFirstText.setTextColor(textColor)
        titleBarBinding.rightSecondText.setTextColor(textColor)
    }

    /**
     * 设置返回按钮图标
     */
    private fun drawBackIcon(backIcon: ImageView) {
        if (title.backIcon == Config.NO_ASSIGNMENT) {
            backIcon.setImageResource(globalConfig.getTitleBackIcon())
        } else {
            backIcon.setImageResource(title.backIcon)
        }
    }
}