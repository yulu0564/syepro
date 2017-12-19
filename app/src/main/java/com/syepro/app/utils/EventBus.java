package com.syepro.app.utils;


import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.ReplaySubject;
import io.reactivex.subjects.Subject;

public class EventBus {
    /**
     * 一个可以多线程安全的事件板块，用来记录所有的实时事件
     */
    private static Subject<Object> mSubject = PublishSubject.create().toSerialized();
    /**
     * 一个可以多线程安全的事件板块，用来记录所有的sticky事件
     */
    private static Subject<Object> mSubjectSticky = ReplaySubject.create().toSerialized();

    /**
     * 静态内部类单例
     */
    private static class EventBusInner {
        /**
         * EventBus单例
         */
        private static EventBus INSTANCE = new EventBus();
    }

    /**
     * 已经注册了事件的实体和对应的处理函数
     */
    private HashMap<Object, CompositeDisposable> registeredObjects = new HashMap<>();

    private EventBus() {
    }

    /**
     * 获取EventBus实体
     *
     * @return
     */
    public static EventBus getDefault() {
        return EventBusInner.INSTANCE;
    }

    /**
     * 发送事件
     *
     * @param object 要发送的事件
     */
    public void post(Object object) {
        mSubject.onNext(object);
        mSubjectSticky.onNext(object);
    }

    /**
     * 注册接收器 接收一个eventType 类型的事件
     *
     * @param master   要接收事件的主体
     * @param callback 事件发生后的回调
     * @param <T>      事件的类型
     */
    public <T> void registerOnMainThread(Object master, final EventCallback<T> callback) {
        registerInner(master, callback, mSubject);
    }

    /**
     * 注册一个粘性事件接收器  接收一个eventType 类型的事件
     *
     * @param master   要接收事件的主体
     * @param callback 事件发生后的回调
     * @param <T>      事件的类型
     */
    public <T> void registerStickyOnMainThread(Object master, final EventCallback<T> callback) {
        registerInner(master, callback, mSubjectSticky);
    }

    /**
     * @param master   要接收事件的主体
     * @param callback 事件发生后的回调
     * @param subject  处理类型，实时或者粘性
     * @param <T>      事件的类型
     */
    private <T> void registerInner(Object master, final EventCallback<T> callback, Subject<Object> subject) {
        DisposableObserver<T> disposableSubscriber = new SimpleDisposableObserver<T>(callback);
        CompositeDisposable compositeSubscription = registeredObjects.get(master);
        if (compositeSubscription == null) {
            compositeSubscription = new CompositeDisposable();
        }
        compositeSubscription.add(disposableSubscriber);
        registeredObjects.put(master, compositeSubscription);
        try {
            Type clazz = ((ParameterizedType) callback.getClass().getGenericInterfaces()[0]).getActualTypeArguments()[0];

            subject.ofType((Class<T>) clazz)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(disposableSubscriber);
        } catch (Exception e) {
            LogUtils.error("EventCallBack must has a Type T");
        }

    }

    /**
     * 取消注册
     *
     * @param master 取消master注册的事件
     */
    public void unregister(Object master) {
        Disposable disposable = registeredObjects.remove(master);
        if (disposable != null) {
            disposable.dispose();
        }
    }

    /**
     * EventBus 事件回调
     *
     * @param <T> 要接受的事件的类型
     */
    public interface EventCallback<T> {
        void onEvent(T value);
    }

    /**
     * 简单的、只有一个onNext回调的、可以取消的   Observer
     *
     * @param <T>
     */
    private static class SimpleDisposableObserver<T> extends DisposableObserver<T> {
        private final EventCallback<T> eventCallback;

        SimpleDisposableObserver(EventCallback<T> eventCallback) {
            this.eventCallback = eventCallback;
        }

        @Override
        public void onNext(T value) {
            eventCallback.onEvent(value);
        }

        @Override
        public void onError(Throwable e) {
        }

        @Override
        public void onComplete() {
        }
    }

}
