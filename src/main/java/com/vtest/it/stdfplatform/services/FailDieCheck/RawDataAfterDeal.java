package com.vtest.it.stdfplatform.services.FailDieCheck;


import com.vtest.it.stdfplatform.pojo.rawdataBean.RawdataInitBean;
import com.vtest.it.stdfplatform.pojo.vtptmt.BinWaferInforBean;

public interface RawDataAfterDeal {
    void deal(RawdataInitBean rawdataInitBean, BinWaferInforBean binWaferInforBean);
}
