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
import com.dike.test.twhomework.ui.view.PictureView;
import com.dike.test.twhomework.ui.viewholder.ViewHolderOnPicture;
import com.dike.test.twhomework.utils.CommonUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
//        pictureView.setPictures(new String[]{"232","123","qwr","123","234","123","4343","4343","4343","4343"});

        mRv = findViewById(R.id.id_activity_main_content_rv);
        setupRecyclerView(mRv);
    }

    private String[] getRandomArr()
    {
        Random random = new Random();
        String[] arr = new String[random.nextInt(9)];
        for(int i = 0; i < arr.length; i++)
        {
            arr[i] = "1"+i;
        }
        return arr;
    }

    private void setupRecyclerView(RecyclerView recyclerView)
    {
        if(null == recyclerView)
        {
            return;
        }
        mDataSource = new ArrayList<>();
        //for test
        DemoItem item;
        for(int i = 0; i < 100; i++)
        {
            item = new DemoItem();
            item.setPhotoUrls(getRandomArr());
            mDataSource.add(item);
        }
        SparseArray<Class<? extends AViewHolder>> maps = new SparseArray<>();
        maps.put(0, ViewHolderOnPicture.class);
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
