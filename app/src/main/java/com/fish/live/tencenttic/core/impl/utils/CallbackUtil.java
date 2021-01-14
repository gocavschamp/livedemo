package com.fish.live.tencenttic.core.impl.utils;

import com.fish.live.tencenttic.core.TICManager;
import com.tencent.liteav.basic.log.TXCLog;

public class CallbackUtil {

    public static void notifySuccess(TICManager.TICCallback callBack, Object data) {
        if (null != callBack) {
            callBack.onSuccess(data);
        }
    }

    public static void notifyError(TICManager.TICCallback callBack, String module, int errCode, String errMsg) {
        if (null != callBack) {
            callBack.onError(module, errCode, errMsg);
        }
        TXCLog.e(module, errMsg);
    }
}
