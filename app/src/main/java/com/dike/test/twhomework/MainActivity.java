package com.dike.test.twhomework;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.widget.Toast;

import com.dike.test.twhomework.action.DemoAction;
import com.dike.test.twhomework.action.IAction;
import com.dike.test.twhomework.entity.TweetsItem;
import com.dike.test.twhomework.entity.UserInfoItem;
import com.dike.test.twhomework.ui.recyclerview.AViewHolder;
import com.dike.test.twhomework.ui.recyclerview.RecyclerViewAdapter;
import com.dike.test.twhomework.ui.recyclerview.TItem;
import com.dike.test.twhomework.ui.view.PullToRefreshView;
import com.dike.test.twhomework.ui.viewholder.ViewHolderContent;
import com.dike.test.twhomework.ui.viewholder.ViewHolderHeader;
import com.dike.test.twhomework.utils.CommonUtil;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements IAction.OnResultListener
{

    private static final int STATUS_IDLE = 1;
    private static final int STATUS_LOADING = 2;
    private static final int STATUS_LOAD_NO_MORE = 3;

    private static final String TAG = "MainActivity";
    private RecyclerView mRv;
    private PullToRefreshView mPullToRefreshView;
    private List<TItem> mDataSource;
    private RecyclerViewAdapter mAdapter;
    private IAction mAction;

    private UserInfoItem mUserInfoItem;
    private int mPageIndex = 1;
    private int mStatus = STATUS_IDLE;
    @Override
    public void onResult(int code, Map<String, Object> result)
    {
        String actionName = CommonUtil.getValueFromMap(result,IAction.COMMON_KEY_ACTION,"");
        if(actionName.endsWith(DemoAction.class.getName()))
        {
            int cmd = CommonUtil.getValueFromMap(result,IAction.COMMON_KEY_CMD,0);
            if(DemoAction.CMD_FETCH_USER_INFO == cmd)
            {
                if(IAction.RESULT_CODE_SUCCESS == code)
                {
                    UserInfoItem infoItem = CommonUtil.getValueFromMap(result, UserInfoItem.class.getName(),mUserInfoItem);
                    mUserInfoItem.copyFrom(infoItem);
                    mAdapter.notifyDataSetChanged();
                }
                else
                {
                    String msg = CommonUtil.getValueFromMap(result,IAction.COMMON_KEY_MESSAGE,"error");
                    Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_SHORT).show();
                }
            }
            else if(DemoAction.CMD_FETCH_TWEETS_DATA == cmd)
            {
                mPullToRefreshView.setRefreshComplete(PullToRefreshView.PULL_TYPE_BOTH);
                if(IAction.RESULT_CODE_SUCCESS == code)
                {
                    mStatus = STATUS_IDLE;
                    List<TweetsItem> tweetsItemList = CommonUtil.getValueFromMap(result, TweetsItem.class.getName(),new LinkedList<TweetsItem>());
                    if(0 == mPageIndex)
                    {
                        mDataSource.clear();
                        mDataSource.add(mUserInfoItem);
                    }
                    if(tweetsItemList.size() > 0)
                    {
                        mDataSource.addAll(tweetsItemList);
                    }

                    mAdapter.notifyDataSetChanged();
                }
                else
                {
                    String msg = CommonUtil.getValueFromMap(result,IAction.COMMON_KEY_MESSAGE,"error");
                    Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_SHORT).show();
                }
            }

            else if(DemoAction.CMD_GET_TWEETS_DATA == cmd)
            {

                if(IAction.RESULT_CODE_SUCCESS == code)
                {
                    mStatus = STATUS_IDLE;
                    List<TweetsItem> tweetsItemList = CommonUtil.getValueFromMap(result, TweetsItem.class.getName(),new LinkedList<TweetsItem>());

                    int size = tweetsItemList.size();
                    if(tweetsItemList.size() > 0)
                    {
                        mDataSource.addAll(tweetsItemList);
                        int addCount = tweetsItemList.size();
                        mAdapter.notifyItemRangeInserted(size,addCount);
                        mAdapter.notifyItemRangeChanged(size,addCount);
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(),"No more data~",Toast.LENGTH_SHORT).show();
                        mStatus = STATUS_LOAD_NO_MORE;
                    }
                }

            }

        }

    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRv = findViewById(R.id.id_activity_main_content_rv);
        mPullToRefreshView  = findViewById(R.id.id_activity_main_content_ptr);
        mPullToRefreshView.setContentView(null,mRv);
        mPullToRefreshView.setPullRefreshType(PullToRefreshView.PULL_TYPE_DOWN);
        mPullToRefreshView.setmScrollInterface(new PullToRefreshView.ScrollInsterface()
        {

            @Override
            public void onRefreshView(PullToRefreshView pullView, int direction)
            {
                if(PullToRefreshView.PULL_DIRECTION_DOWN == direction)
                {
                    mPageIndex = 0;
                    mStatus = STATUS_LOADING;
                    mAction.doAction(DemoAction.getFetchTweetsDataParams());
                }
            }

            @Override
            public void onPreRefreshView(PullToRefreshView pullView, int direction)
            {
                // TODO Auto-generated method stub

            }

        });
        setupRecyclerView(mRv);

        mAction = new DemoAction();
        mAction.addListener(this);
        mAction.doAction(DemoAction.getFetchUserInfoParams());
        mAction.doAction(DemoAction.getFetchTweetsDataParams());
    }


    private void setupRecyclerView(RecyclerView recyclerView)
    {
        if(null == recyclerView)
        {
            return;
        }
        mDataSource = new ArrayList<>();

        mUserInfoItem = new UserInfoItem();
        mUserInfoItem.setViewType(0);
        mDataSource.add(mUserInfoItem);
        SparseArray<Class<? extends AViewHolder>> maps = new SparseArray<>();
        maps.put(0, ViewHolderHeader.class);
        maps.put(1, ViewHolderContent.class);

        mAdapter = new RecyclerViewAdapter(getApplicationContext(),this,mDataSource,maps);

        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(mAdapter);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener()
        {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState)
            {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy)
            {
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                int lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                int firstVisibleItem = linearLayoutManager.findFirstVisibleItemPosition();
                int totalItemCount = linearLayoutManager.getItemCount();

                //lastVisibleItem >= totalItemCount - 4 表示剩下4个item自动加载，各位自由选择
                // dy>0 表示向下滑动
                if (dy > 0 && lastVisibleItem >= totalItemCount - 1)
                {
                    if(STATUS_LOADING == mStatus)
                    {
                        return;
                    }
                    if(STATUS_LOAD_NO_MORE == mStatus)
                    {
                        Toast.makeText(getApplicationContext(),"No more data~",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    mStatus = STATUS_LOADING;
                    mPageIndex++;
                    mAction.doAction(DemoAction.getTweetsDataParams(mPageIndex));
                }

            }
        });
    }

    @Override
    protected void onDestroy()
    {
        mAction.removeListener(this);
        super.onDestroy();
    }
}
