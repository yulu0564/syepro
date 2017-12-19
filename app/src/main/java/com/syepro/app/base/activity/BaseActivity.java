package com.syepro.app.base.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.syepro.app.base.BaseApplication;
import com.syepro.app.base.skip.Skip;
import com.syepro.app.base.skip.SkipView;
import com.syepro.app.utils.EventBus;
import com.syepro.app.utils.EventBusCenter;
import com.syepro.app.utils.NetUtils;

import java.io.Serializable;
import java.util.ArrayList;

import butterknife.ButterKnife;
import io.reactivex.disposables.Disposable;

public abstract class BaseActivity extends AppCompatActivity implements SkipView {
    public BaseActivity mContext;
    public BaseApplication mApplication;
    protected boolean isScreen = false;
    private String pageName;  //TCAgent的pageName
    protected boolean isSavedInstance = false;//是否是恢复的数据
    /**
     * 关联到此Activity的网络请求，当当前Activity结束时，会结束此次请求
     */
    private ArrayList<Disposable> disposableList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        isSavedInstance = onRestoreState(savedInstanceState);
        initFirst();
        if (!isScreen) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        super.onCreate(savedInstanceState);
        mApplication = BaseApplication.getInstance();
        initSystemBar();
        int layoutResID = getContentView();
        if (layoutResID != 0) {
            setContentView(layoutResID);
        }
        //添加注解
        ButterKnife.bind(this);
        if (isRegistEventBus()) {
            EventBus.getDefault().registerOnMainThread(this, new EventBus.EventCallback<EventBusCenter>() {

                @Override
                public void onEvent(EventBusCenter value) {
                    onMsgEvent(value);
                }
            });
        }

        mContext = this;
        if (mApplication.mScreenWidth == 0) {
            DisplayMetrics metric = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(metric);
            mApplication.mScreenWidth = metric.widthPixels;
            mApplication.mScreenHeight = metric.heightPixels;
        }
        initBase();
        initViews();
        initData();
    }


    /**
     * 返回当前布局
     */
    public abstract int getContentView();

    protected void initFirst() {

    }

    protected void initBase() {

    }

    protected boolean onRestoreState(Bundle savedInstanceState) {
        return false;
    }

    /**
     * 设置状态栏背景颜色
     */
    protected void initSystemBar() {
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    /**
     * 否则会报异常
     * */
    protected void onDestroy() {
        super.onDestroy();
        if (isRegistEventBus()) {
            EventBus.getDefault().unregister(this);
        }
        disposableAllRequest();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (pageName == null) {
            pageName = mContext.getClass().getName();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    /**
     * 初始化视图 *
     */
    protected abstract void initViews();

    /**
     * 初始化数据 *
     */
    protected void initData() {
    }

    /**
     * 设置状态栏背景状态
     */
    @SuppressWarnings("unused")
    protected void initSystemBar(int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window win = getWindow();
            // Translucent status bar
            win.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            WindowManager.LayoutParams winParams = win.getAttributes();
            final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
            winParams.flags |= bits;
            win.setAttributes(winParams);
        } else {
            // getActionBar().hide();
        }
    }

    protected boolean onBackActivity() {
        return true;
    }

    /**
     * 弹出toast
     *
     * @param o
     */
    protected void toast(Object o) {
        if (o instanceof Integer) {
            Toast.makeText(mApplication, (Integer) o, Toast.LENGTH_SHORT).show();
        } else if (o instanceof CharSequence) {
            Toast.makeText(mApplication, (CharSequence) o, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 当前网络是否有连接
     *
     * @return
     */
    public boolean network() {
        if (NetUtils.checkNet(this)) {
            return true;
        }
//		toast("未连接网络！");
        return false;
    }

    public boolean network(String text) {
        if (NetUtils.checkNet(this)) {
            return true;
        }
        toast(text);
        return false;
    }

    /**
     * 获取控件 *
     */
    public <T extends View> T getView(int id) {
        return (T) findViewById(id);
    }

    //Activity之间跳转
    @Override
    public void skip(Class<? extends Activity> cls, ActivityOptionsCompat options, Serializable... serializ) {
        Skip.skip(mContext, cls, options, serializ);
    }

    @Override
    public void skipForResult(Class clasz, ActivityOptionsCompat options, int requestCode, Serializable... serializ) {
        Skip.skipForResult(mContext, clasz, options, requestCode, serializ);
    }

    @Override
    public void skipForResult(Class clasz, int requestCode, Serializable... serializ) {
        Skip.skipForResult(mContext, clasz, requestCode, serializ);
    }

    @Override
    public void skip(Class<? extends Activity> cls) {
        Skip.skip(mContext, cls);
    }

    @Override
    public void skip(String action) {
        Skip.skip(mContext, action);
    }

    @Override
    public void skip(String action, Serializable... serializ) {
        Skip.skip(mContext, action, serializ);
    }

    @Override
    public void skip(Class<? extends Activity> cls, Serializable... serializ) {
        Skip.skip(mContext, cls, serializ);
    }

    public <T extends Serializable> T getVo(String key) {
        Intent myIntent = getIntent();
        Bundle bundle = myIntent.getExtras();
        if (bundle == null) {
            return (T) "";
        }
        Serializable vo = bundle.getSerializable(key);
        return (T) vo;
    }

    public void setScreen(boolean screen) {
        isScreen = screen;
    }

    /**
     * @return 是否需要注册EventBus
     */
    public boolean isRegistEventBus() {
        return false;
    }

    /**
     * @param eventBusCenter EventBus 通知事件
     */
    public void onMsgEvent(EventBusCenter eventBusCenter) {

    }

    public void addDisposable(Disposable disposable) {
        this.disposableList.add(disposable);
    }

    public boolean removeDisposable(Disposable disposable) {
        return disposableList.remove(disposable);
    }

    protected void disposableAllRequest() {
        for (Disposable disposable : disposableList) {
            if (disposable != null && !disposable.isDisposed())
                disposable.dispose();
        }
    }

}
