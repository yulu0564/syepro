package com.hazz.kotlinmvp.base


interface IBaseInit {


    /**
     * 获取资源id 子类返回
     */
    abstract fun getLayoutId(): Int

    /**
     *初始化View与事件
     */
    abstract fun initViewsAndEvents()

    /**
     *初始化数据
     */
    abstract fun loadData()
}
