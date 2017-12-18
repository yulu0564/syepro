package com.syepro.app;

import com.syepro.app.activity.VideoActivity;
import com.syepro.app.api.JyAPIUtil;
import com.syepro.app.api.network.Comments;
import com.syepro.app.api.network.ProgressSubscriber;
import com.syepro.app.base.activity.BaseActivity;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;

public class MainActivity extends BaseActivity {

    @Override
    public int getContentView() {
        return R.layout.activity_main;
    }

    @Override
    protected void initViews() {
        JyAPIUtil.jyApi.getOrderList("31", 1, 20).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ProgressSubscriber<List<Comments>>() {
                    @Override
                    public void onResponse(List<Comments> result) {
                    }
                });
        Comments mComments = new Comments();
        mComments.setUserid("31");
        mComments.setNewsid("1");
        mComments.setContects("fdsgfdgfghfdhhhhhhhhhhhh");
        JyAPIUtil.jyApi.post("31", "1", "fdfdsggfffffffff").observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ProgressSubscriber<Comments>() {
                    @Override
                    public void onResponse(Comments result) {
                    }
                });

        skip(VideoActivity.class);


    }


}
