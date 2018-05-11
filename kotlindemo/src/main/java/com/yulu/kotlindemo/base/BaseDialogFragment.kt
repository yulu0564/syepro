package com.yulu.kotlindemo.base

import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AppCompatDialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.graphics.drawable.ColorDrawable
import android.app.DatePickerDialog
import android.app.ProgressDialog
import android.graphics.Color
import android.os.Build
import android.view.Window
import com.hazz.kotlinmvp.base.IBaseInit

/**
 * DialogFragment 基类
 */
abstract class BaseDialogFragment : AppCompatDialogFragment(), IBaseInit {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isCancelable = true
        val style = DialogFragment.STYLE_NO_TITLE
        val theme = 0
        setStyle(style, theme)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(getLayoutId(), container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewsAndEvents()
        loadData()
    }

    override fun onStart() {
        super.onStart()
        val dialog = dialog
        if (dialog != null) {
            val window = getDialog().window
            getBackgroundDrawableResource()?.let {
                if (it == null) {
                    val windowParams = window.attributes
                    //在5.0以下的版本会出现白色背景边框，若在5.0以上设置则会造成文字部分的背景也变成透明
                    if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
                        //目前只有这两个dialog会出现边框
                        if (dialog is ProgressDialog || dialog is DatePickerDialog) {
                            window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                        }
                    }
//                    windowParams.dimAmount = 0.0f
//                    window.attributes = windowParams
                } else {
                    window.setBackgroundDrawableResource(it)
                }
            }
            startDialg(window)
        }
    }

    //背景
    open fun getBackgroundDrawableResource(): Int? {
        return null
    }

    open fun startDialg(window: Window) {

//        //设置宽度顶满屏幕,无左右留白
//        val dm = DisplayMetrics()
//        activity?.windowManager?.defaultDisplay?.getMetrics(dm)
//        window.setLayout(dm.widthPixels, window.attributes.height)
        //再次设置出现动画
//        window.getAttributes().windowAnimations = R.style.DialogAnimation;
    }
}