package com.iiaim.service;

import com.iiaim.pojo.ClickBatch;
import com.iiaim.pojo.ScreenPara;

import java.util.List;

/**
 * @author 陈杰炫
 */
public interface ClickService {

    public String doClick(ClickBatch clickBatch);

    public String doDispense(ClickBatch clickBatch);

    public void doTimeScr(final List<ScreenPara> list) throws InterruptedException;
}
