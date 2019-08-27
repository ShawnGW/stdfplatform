package com.vtest.it.stdfplatform.services.urlMesInformation;

import com.vtest.it.stdfplatform.pojo.rawdataBean.RawdataInitBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StdfTouchDownWrite {
    private StdfTouchDownInforIntoMes stdfTouchDownInforIntoMes;
    private static final Logger LOGGER = LoggerFactory.getLogger(StdfTouchDownWrite.class);
    @Autowired
    public void setStdfTouchDownInforIntoMes(StdfTouchDownInforIntoMes stdfTouchDownInforIntoMes) {
        this.stdfTouchDownInforIntoMes = stdfTouchDownInforIntoMes;
    }

    public void write(RawdataInitBean bean) {
        String lotNum = bean.getProperties().get("Lot ID");
        String cp = bean.getProperties().get("CP Process");
        String waferId = bean.getProperties().get("Wafer ID");
        StringBuilder inforsBuilder = new StringBuilder();
        inforsBuilder.append("|FirstTestCnt:" + bean.getPrimaryTouchDownTimes());
        inforsBuilder.append("|FirstTestTime:" + bean.getPrimaryTouchDownDuringTime());
        inforsBuilder.append("|RetestCnt:" + bean.getReTestTouchDownTimes());
        inforsBuilder.append("|RetestTime:" + bean.getReTestTouchDownDuringTime());
        inforsBuilder.append("|SingleTestTime:" + bean.getSingleTouchDownDuringTime());
        inforsBuilder.append("|RetestYield:" + bean.getReTestRate());
        inforsBuilder.append("|TotalTestTime:" + bean.getTestDuringTime());
        LOGGER.error("stdfTouchDownInforIntoMes: " + waferId + "," + cp);
        stdfTouchDownInforIntoMes.write(lotNum, waferId, cp, inforsBuilder.toString());
    }
}
