package com.yulu.kotlindemo.api.network


import java.io.Serializable

/*
 * 文件名: Result.java
 * 描    述: 请求返回结果对象
 * 参   考: http://www.cnblogs.com/qq78292959/p/3781808.html
 *
 * */
class ResultBean<T> : Serializable {

    var code: Int = 0

    var msg: String? = null

    /**
     * 数据
     */
    var data: T? = null
}
