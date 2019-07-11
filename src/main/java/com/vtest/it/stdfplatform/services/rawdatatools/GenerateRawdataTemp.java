package com.vtest.it.stdfplatform.services.rawdatatools;


import com.vtest.it.stdfplatform.pojo.rawdataBean.RawdataInitBean;
import com.vtest.it.stdfplatform.pojo.vtptmt.DataParseIssueBean;
import com.vtest.it.stdfplatform.services.rawdataCheck.RawDataCheck;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;

@Service
public class GenerateRawdataTemp {
    @Autowired
    private GenerateRawdataFinal generateRawdataFinal;
    @Autowired
    private GenerateRawdata generateRawdata;
    @Autowired
    private RawDataCheck rawDataCheck;
    @Autowired
    private GeneratePrimaryAndReTestMap generatePrimaryAndReTestMap;
    public boolean generateTempRawdata(RawdataInitBean rawdataInitBean, ArrayList<DataParseIssueBean> dataParseIssueBeans) throws Exception {
        RawdataInitBean rawdataInitBeanTarget = new RawdataInitBean();
        BeanUtils.copyProperties(rawdataInitBean, rawdataInitBeanTarget);
        File tempRawdata = generateRawdata.generate(rawdataInitBeanTarget);
        boolean checkFlag = rawDataCheck.check(tempRawdata, dataParseIssueBeans);
        if (!checkFlag) {
            return false;
        }
        generateRawdataFinal.generateFinalRawdata(tempRawdata, rawdataInitBean);
        generatePrimaryAndReTestMap.generate(rawdataInitBean);
        return true;
    }
}
