package com.dike.test.twhomework.action;


import java.util.Map;

public interface IAction
{

    String COMMON_KEY_IS_SYNC       = "is_sync";
    String COMMON_KEY_CMD           = "cmd";
    String COMMON_KEY_ACTION        = "action";
    String COMMON_KEY_MESSAGE       = "message";

    int RESULT_CODE_ERROR   = -1;
    int RESULT_CODE_SUCCESS = 1;


    interface OnResultListener
    {
        void onResult(int code,Map<String,Object> result);
    }

    void doAction(Map<String,Object> params);

    void addListener(OnResultListener listener);

    void removeListener(OnResultListener listener);
}
