package com.yulu.kotlindemo.base

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.hazz.kotlinmvp.base.IBaseInit
import io.reactivex.disposables.Disposable
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.util.*
import kotlin.reflect.jvm.internal.impl.load.kotlin.JvmType

abstract class BaseActivity : AppCompatActivity() ,IBaseInit{
    /**
     * 关联到此Activity的网络请求，当当前Activity结束时，会结束此次请求
     */
    private var disposableList = ArrayList<Disposable>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutId())
        if (isRegistEventBus())
            EventBus.getDefault().register(this)
        initViewsAndEvents()
    }



    /**
     * 是否注册EventBus true 注册 false 不注册
     */
    open fun isRegistEventBus(): Boolean {
        return false
    }

    @Subscribe(threadMode = ThreadMode.MAIN_ORDERED)
    open fun onMessageEvent(eventBusCenter: EventBusCenter<JvmType.Object>) {
    }

    /**
     * 释放资源 与 取消掉 页面Presenter 相关请求
     */
    override fun onDestroy() {
        super.onDestroy()
        if (isRegistEventBus()) EventBus.getDefault().unregister(this)
    }


    fun removeDisposable(disposable: Disposable): Boolean {
        return disposableList.remove(disposable)
    }

    fun addDisposable(disposable: Disposable) {
        this.disposableList.add(disposable)
    }
}