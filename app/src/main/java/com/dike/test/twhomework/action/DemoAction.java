package com.dike.test.twhomework.action;

import android.text.TextUtils;

import com.dike.test.twhomework.entity.TweetsItem;
import com.dike.test.twhomework.entity.UserInfoItem;
import com.dike.test.twhomework.utils.CommonUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class DemoAction extends TAction
{

    public static final int CMD_FETCH_USER_INFO = 1;
    public static final int CMD_FETCH_TWEETS_DATA = 2 ;
    public static final int CMD_GET_TWEETS_DATA = 3 ;

    private static final int PAGE_SIZE = 5;

    private OkHttpClient client = new OkHttpClient();
    private Gson gson = new Gson();

    private List<TweetsItem> mTweetsItems = new LinkedList<>();

    @Override
    protected void execute(Map<String, Object> params)
    {
        int cmd = CommonUtil.getValueFromMap(params,"cmd",-1);
        if(CMD_FETCH_USER_INFO == cmd)
        {
            String url = CommonUtil.getValueFromMap(params,"url","");
            if(!TextUtils.isEmpty(url))
            {
                Request request = new Request.Builder()
                        .url(url)
                        .build();

                try
                {
                    Response response = client.newCall(request).execute();
                    String result = response.body().string();

                    UserInfoItem userInfo = gson.fromJson(result, UserInfoItem.class);

                    if(null != userInfo)
                    {
                        Map<String,Object> map = new HashMap<>();
                        map.put(COMMON_KEY_CMD,cmd);
                        map.put(COMMON_KEY_ACTION,getClass().getName());
                        map.put(UserInfoItem.class.getName(),userInfo);
                        sendToUi(RESULT_CODE_SUCCESS,map);
                    }
                    else
                    {
                        Map<String,Object> map = new HashMap<>();
                        map.put(COMMON_KEY_CMD,cmd);
                        map.put(COMMON_KEY_ACTION,getClass().getName());
                        map.put(COMMON_KEY_MESSAGE,result);
                        sendToUi(RESULT_CODE_SUCCESS,map);
                    }
                }
                catch (IOException e)
                {
                    Map<String,Object> map = new HashMap<>();
                    map.put(COMMON_KEY_CMD,cmd);
                    map.put(COMMON_KEY_ACTION,getClass().getName());
                    map.put(COMMON_KEY_MESSAGE,e.getMessage());
                    sendToUi(RESULT_CODE_ERROR,map);
                }
            }
            else
            {
                Map<String,Object> map = new HashMap<>();
                map.put(COMMON_KEY_CMD,cmd);
                map.put(COMMON_KEY_ACTION,getClass().getName());
                map.put(COMMON_KEY_MESSAGE,"url is empty");
                sendToUi(RESULT_CODE_ERROR,map);
            }
        }
        else if(CMD_FETCH_TWEETS_DATA == cmd)
        {
            String url = CommonUtil.getValueFromMap(params,"url","");
            if(!TextUtils.isEmpty(url))
            {
                Request request = new Request.Builder()
                        .url(url)
                        .build();

                try
                {
                    Response response = client.newCall(request).execute();
                    String result = response.body().string();

                    Type type = new TypeToken<List<TweetsItem>>() {}.getType();
                    List<TweetsItem> tweetsItemList = gson.fromJson(result, type);

                    if(null != tweetsItemList)
                    {
                        mTweetsItems.clear();
                        int size = tweetsItemList.size();
                        TweetsItem item;
                        for(int i = 0; i < size; i++)
                        {
                            item = tweetsItemList.get(i);
                            if(item.isValid())
                            {
                                mTweetsItems.add(item);
                            }
                        }
                        Map<String,Object> map = new HashMap<>();
                        map.put(COMMON_KEY_CMD,cmd);
                        map.put(COMMON_KEY_ACTION,getClass().getName());
                        map.put(TweetsItem.class.getName(),getSubList(0,PAGE_SIZE));
                        sendToUi(RESULT_CODE_SUCCESS,map);
                    }
                    else
                    {
                        Map<String,Object> map = new HashMap<>();
                        map.put(COMMON_KEY_CMD,cmd);
                        map.put(COMMON_KEY_ACTION,getClass().getName());
                        map.put(COMMON_KEY_MESSAGE,result);
                        sendToUi(RESULT_CODE_SUCCESS,map);
                    }
                }
                catch (IOException e)
                {
                    Map<String,Object> map = new HashMap<>();
                    map.put(COMMON_KEY_CMD,cmd);
                    map.put(COMMON_KEY_ACTION,getClass().getName());
                    map.put(COMMON_KEY_MESSAGE,e.getMessage());
                    sendToUi(RESULT_CODE_ERROR,map);
                }
            }
            else
            {
                Map<String,Object> map = new HashMap<>();
                map.put(COMMON_KEY_CMD,cmd);
                map.put(COMMON_KEY_ACTION,getClass().getName());
                map.put(COMMON_KEY_MESSAGE,"url is empty");
                sendToUi(RESULT_CODE_ERROR,map);
            }
        }
        else if(CMD_GET_TWEETS_DATA == cmd)
        {
            int page = CommonUtil.getValueFromMap(params,"page",0);
            Map<String,Object> map = new HashMap<>();
            map.put(COMMON_KEY_CMD,cmd);
            map.put(COMMON_KEY_ACTION,getClass().getName());
            map.put(TweetsItem.class.getName(),getSubList(page*PAGE_SIZE,PAGE_SIZE));
            sendToUi(RESULT_CODE_SUCCESS,map);
        }

    }

    private List<TweetsItem> getSubList(int start,int count)
    {
        if(null == mTweetsItems)
        {
            return null;
        }

        int size = mTweetsItems.size();
        if(size == 0)
        {
            return null;
        }
        start = Math.max(start,0);
        if(start > size)
        {
            return null;
        }
        List<TweetsItem> data = new LinkedList<>();
        int end = Math.min(size,start + count);
        for(int i = start; i < end; i++)
        {
            data.add(mTweetsItems.get(i));
        }
        return data;
    }

    public static Map<String,Object> getFetchUserInfoParams()
    {
        Map<String,Object> params = new HashMap<>();
        params.put("cmd",CMD_FETCH_USER_INFO);
        params.put("url","http://thoughtworks-ios.herokuapp.com/user/jsmith");
        return params;
    }

    public static Map<String,Object> getFetchTweetsDataParams()
    {
        Map<String,Object> params = new HashMap<>();
        params.put("cmd",CMD_FETCH_TWEETS_DATA);
        params.put("url","http://thoughtworks-ios.herokuapp.com/user/jsmith/tweets");
        return params;
    }

    public static Map<String,Object> getTweetsDataParams(int page)
    {
        Map<String,Object> params = new HashMap<>();
        params.put("cmd",CMD_GET_TWEETS_DATA);
        params.put("page",page);
        return params;
    }
}
