package com.syepro.app.base.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.syepro.app.base.BaseApplication;
import com.syepro.app.base.activity.BaseActivity;
import com.syepro.app.base.skip.Skip;
import com.syepro.app.base.skip.SkipView;
import com.syepro.app.commonjar.utils.EventBus;
import com.syepro.app.commonjar.utils.EventBusCenter;
import com.syepro.app.commonjar.utils.NetUtils;

import java.io.Serializable;

import butterknife.ButterKnife;

public abstract class BaseFragment extends Fragment implements SkipView {

    protected BaseActivity mContext;
    protected View rootView;
    private boolean isRefresh = true;  //是否每次进入都重新加载界面
    private int resId;
    public BaseApplication mApplication;
    protected boolean isCreated = false;
    private String pageName;  //TCAgent的pageName

    protected void initBase() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isCreated = true;
        try {
            mContext = (BaseActivity) getActivity();
            mApplication = mContext.mApplication;
        } catch (ClassCastException ex) {
            throw new ClassCastException(getActivity().getClass()
                    .getSimpleName() + " 不是BaseActivity");
        }
        resId = getContentView();
        getIntentBundle(getArguments());
    }

    //结束参数
    public void getIntentBundle(@Nullable Bundle mArguments) {
    }

    protected String TAG = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (isRefresh || rootView == null) {
            rootView = inflater.inflate(resId, container, false);
            //添加注解
            ButterKnife.bind(this, rootView);
            TAG = this.getClass().getSimpleName() + "--";
            initBase();
            initViews();
        }
        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null) {
            parent.removeView(rootView);
        }
        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (isRegistEventBus()) {
            EventBus.getDefault().registerOnMainThread(this, new EventBus.EventCallback<EventBusCenter>() {

                @Override
                public void onEvent(EventBusCenter value) {
                    onMsgEvent(value);
                }
            });
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (isRegistEventBus()) {
            EventBus.getDefault().unregister(this);
        }
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

    /**
     * 返回当前布局
     */
    public abstract int getContentView();

    /**
     * 初始化视图 *
     */
    protected abstract void initViews();

    /**
     * 获取控件 *
     */
    @SuppressWarnings("unchecked")
    protected <T extends View> T getView(int id) {
        return (T) rootView.findViewById(id);
    }

    public boolean onBack() {
        return false;
    }

    /**
     * 滑动视图里判断是否可见
     *
     * @param isVisibleToUser
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        String name = this.getClass().getName();
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            onUserVisible();
        } else if (isVisibleToUser) {
            onUserInVisible();
        }
        if (isResumed()) {
            onVisibilityChangedToUser(isVisibleToUser, true);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        if (getUserVisibleHint()) {
            onVisibilityChangedToUser(true, false);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (getUserVisibleHint()) {
            onVisibilityChangedToUser(false, false);
        }
    }

    protected void onUserVisible() {
    }

    protected void onUserInVisible() {
    }

    /**
     * 当前网络是否有连接
     *
     * @return
     */
    public boolean network() {
        if (NetUtils.checkNet(mContext)) {
            return true;
        }
        return false;
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

    public void skip(View v, Class<? extends Activity> cls, Serializable... serializ) {
        Intent intent = new Intent(mContext, cls);
        Bundle extras = new Bundle();
        for (int i = 0; i < serializ.length; i++) {
            Serializable s = serializ[i];
            // 放对象的规则，以顺序为键
            extras.putSerializable(i + "", s);
        }
        intent.putExtras(extras);

        ActivityOptionsCompat options =
                ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(),
                        v, "transition_news_img");

        ActivityCompat.startActivity(mContext, intent, options.toBundle());
    }

    public <T extends Serializable> T getVo(String key) {
        Intent myIntent = mContext.getIntent();
        Bundle bundle = myIntent.getExtras();
        if (bundle == null) {
            return (T) "";
        }
        Serializable vo = bundle.getSerializable(key);
        return (T) vo;
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
    }

    /**
     * 设置每次进入是否重新加载界面
     *
     * @param isRefresh
     */
    public void setIsRefresh(boolean isRefresh) {
        this.isRefresh = isRefresh;
    }

    /**
     * 当Fragment对用户的可见性发生了改变的时候就会回调此方法
     *
     * @param isVisibleToUser                      true：用户能看见当前Fragment；false：用户看不见当前Fragment
     * @param isHappenedInSetUserVisibleHintMethod true：本次回调发生在setUserVisibleHintMethod方法里；false：发生在onResume或onPause方法里
     */
    public void onVisibilityChangedToUser(boolean isVisibleToUser, boolean isHappenedInSetUserVisibleHintMethod) {
        if (pageName == null) {
            pageName = this.getClass().getName();
        }
        if (isVisibleToUser) {
            if (pageName != null) {
                //可见
            }
        } else {
            if (pageName != null) {
                //不可见
            }
        }
    }

    public void setPageName(String pageName) {
        this.pageName = pageName;
    }
}
