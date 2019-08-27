package com.dike.test.twhomework.action;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public abstract class TAction implements IAction
{
    private List<OnResultListener> mListeners;
    private Handler mUiHandler = new Handler(Looper.getMainLooper())
    {
        @Override
        public void handleMessage(Message msg)
        {
            int code = msg.arg1;
            Map<String,Object> result = (Map<String, Object>) msg.obj;
            notifyListener(code,result);
        }
    };

    private ExecutorService executor;

    public TAction()
    {
        mListeners = new Vector<>();
        executor = Executors.newCachedThreadPool();
    }

    @Override
    public void addListener(OnResultListener listener)
    {
        mListeners.add(listener);
    }

    @Override
    public void removeListener(OnResultListener listener)
    {
        mListeners.remove(listener);
    }

    @Override
    public void doAction(final Map<String, Object> params)
    {
        boolean isSync = false;
        if(null != params)
        {
            Object value = params.get(COMMON_KEY_IS_SYNC);
            isSync = Boolean.class.isInstance(value) ?(Boolean) value : false;
        }

        if(isSync)
        {
            execute(params);
        }
        else
        {
            executor.execute(new Runnable()
            {
                @Override
                public void run()
                {
                    execute(params);
                }
            });
        }
    }



    private synchronized void notifyListener(int code,Map<String,Object> result)
    {
        int size = mListeners.size();
        OnResultListener listener;
        for(int i = size -1; i >= 0; i--)
        {
            listener = mListeners.get(i);
            if(null != listener)
            {
                listener.onResult(code, result);
            }
        }
    }


    protected void sendToUi(int code,Map<String,Object> result)
    {
        if(Looper.myLooper() == Looper.getMainLooper())
        {
            notifyListener(code, result);
        }
        else
        {
            Message message = Message.obtain(mUiHandler);
            message.arg1 = code;
            message.obj = result;
            message.sendToTarget();
        }
    }


    protected abstract void execute(Map<String, Object> params);


}
