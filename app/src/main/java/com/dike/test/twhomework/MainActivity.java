package com.dike.test.twhomework;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;

import com.dike.test.twhomework.entity.DemoItem;
import com.dike.test.twhomework.ui.recyclerview.AViewHolder;
import com.dike.test.twhomework.ui.recyclerview.RecyclerViewAdapter;
import com.dike.test.twhomework.utils.CommonUtil;
import com.dike.test.twhomework.view.PictureView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
{
    private static final String TAG = "MainActivity";
    private RecyclerView mRv;
    private List<DemoItem> mDataSource;
    private RecyclerViewAdapter mAdapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        CommonUtil.i(TAG,"after setContentView");
//        PictureView pictureView = findViewById(R.id.id_activity_main_pv);
//        pictureView.setPictures(new String[]{"232","123"});
        mRv = findViewById(R.id.id_activity_main_content_rv);

        setupRecyclerView(mRv);
    }

    private void setupRecyclerView(RecyclerView recyclerView)
    {
        if(null == recyclerView)
        {
            return;
        }
        mDataSource = new ArrayList<>();
        SparseArray<Class<? extends AViewHolder>> maps = new SparseArray<>();
//        maps.put(Book.COME_FROM_SPIDER, ChapterListViewHolder.class);
//        maps.put(Book.COME_FROM_SERVER, ChapterListViewHolderNew.class);
//        maps.put(TFooterItem.VIEW_TYPE_FOOTER, TFooterOrHeaderLoadMoreViewHolder.class);
//        maps.put(TFooterItem.VIEW_TYPE_HEADER, TFooterOrHeaderLoadMoreViewHolder.class);

        mAdapter = new RecyclerViewAdapter(getApplicationContext(),mDataSource,maps);

        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        CommonUtil.i(TAG,"onStart");
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        CommonUtil.i(TAG,"onResume");
    }
}
