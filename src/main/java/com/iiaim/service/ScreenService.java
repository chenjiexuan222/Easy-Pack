package com.iiaim.service;

import com.iiaim.pojo.ScreenBatch;
import com.iiaim.pojo.ScreenPara;

import java.util.List;

/**
 * @author 陈杰炫
 */
public interface ScreenService {
    //表示将进性反应筛选的操作
    public String doScreen(ScreenBatch screenBatch);

    public String doDispense(ScreenBatch screenBatch);

    public void doTimeScr(final ScreenBatch screenBatch) throws InterruptedException;
}
