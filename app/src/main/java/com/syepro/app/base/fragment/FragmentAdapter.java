package com.syepro.app.base.fragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import com.syepro.app.base.activity.BaseActivity;

import java.util.ArrayList;
import java.util.List;

public class FragmentAdapter {

    private BaseActivity acticity;
    public List<BaseFragment> fragments = new ArrayList<BaseFragment>();
    public int currentFragment;
    private int viewId;

    public FragmentAdapter(BaseActivity acticity, int viewId) {
        this.acticity = acticity;
        this.viewId = viewId;
    }

    /**
     * 对两个Fragment进行交换
     *
     * @param i
     */
    public void convertFragment(int i) {
        if (currentFragment == i) {
            return;
        }
        Fragment fragment = fragments.get(i);
        FragmentTransaction ft = acticity.getSupportFragmentManager()
                .beginTransaction();
        fragments.get(currentFragment).onPause();
        ft.hide(fragments.get(currentFragment));
        if (fragment.isAdded()) {
            fragment.onResume();
        } else {
            ft.add(viewId, fragment);
        }
        ft.show(fragment);
        // showTab(i);
        ft.commit();
        currentFragment = i;
    }

    public void startFragment(int i) {
        Fragment fragment = fragments.get(i);
        FragmentTransaction ft = acticity.getSupportFragmentManager()
                .beginTransaction();
        ft.add(viewId, fragment);
        ft.show(fragment);
        ft.commit();
        currentFragment = i;
    }

    public BaseFragment getFragment() {
        return fragments.get(currentFragment);
    }

}