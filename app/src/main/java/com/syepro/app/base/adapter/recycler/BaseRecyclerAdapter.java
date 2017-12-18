package com.syepro.app.base.adapter.recycler;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import butterknife.ButterKnife;

/**
 * RecyclerAdapter通用适配器
 */
public abstract class BaseRecyclerAdapter<T, V extends RecyclerView.ViewHolder> extends
        RecyclerView.Adapter<V> {
    protected Context context;
    protected List<T> mDatas;

    public BaseRecyclerAdapter(Context context, List<T> mList) {
        super();
        this.context = context;
        this.mDatas = mList;
    }

    /**
     * <添加数据，指定其位置>
     */

    public void addData(T info, int position) {
        mDatas.add(position, info);
        notifyItemInserted(position);

    }

    /**
     * <添加数据到最后面添加>
     */

    public void addData(T info) {
        mDatas.add(info);
        notifyDataSetChanged();
    }

    /**
     * <删除数据，指定其位置>
     */
    public void deleteData(int position) {
        mDatas.remove(position);
        notifyItemRemoved(position);
    }

    /**
     * <某一位置开始，有itemCount个Item的数据删除>
     */
    public void deleteDataAll(int positionStart, int itemCount) {
        for (int i = positionStart; i < itemCount; i++) {
            mDatas.remove(positionStart);
        }
        notifyItemRangeRemoved(positionStart, itemCount);
    }

    @Override
    public int getItemCount() {
        return mDatas == null ? 0 : mDatas.size();
    }

    @Override
    public void onBindViewHolder(V viewHolder,
                                 int position) {
        convert(viewHolder, mDatas.get(position), position);
    }

    public abstract void convert(V holder,
                                 T item, int position);

    @Override
    public V onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        int mItemLayoutId = onCreateView(viewType);
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(mItemLayoutId, viewGroup, false);
        V viewHolder = getViewHolder(view);
        ButterKnife.bind(viewHolder, view);
        return viewHolder;
    }

    protected abstract V getViewHolder(View view);  //返回viewViewHolder

    public abstract int onCreateView(int viewType);//返回布局文件

    protected OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

}
