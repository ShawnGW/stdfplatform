package com.vtest.it.stdfplatform.datadeal;

import com.vtest.it.stdfplatform.pojo.rawdataBean.RawdataInitBean;
import com.vtest.it.stdfplatform.pojo.system.WaferInitInformationBean;
import com.vtest.it.stdfplatform.pojo.vtptmt.DataParseIssueBean;
import com.vtest.it.stdfplatform.services.rawdatatools.GenerateRawdataTemp;
import com.vtest.it.stdfplatform.services.tools.GetOrder;
import com.vtest.it.stdfplatform.services.vtptmt.impl.VtptmtInforImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Service
public class StdfPlatformDataDeal {
    @Autowired
    private GetOrder getOrder;
    @Autowired
    private GenerateRawdataInitInformation generateRawdataInitInformation;
    @Autowired
    private GenerateRawdataTemp generateRawdataTemp;
    @Autowired
    private VtptmtInforImpl vtptmtInfor;

    public ArrayList<File[]> deal(HashMap<WaferInitInformationBean, File[]> resources) {
        ArrayList<File[]> dataNeedDelete=new ArrayList<>();
        for (Map.Entry<WaferInitInformationBean,File[]> entry: resources.entrySet()) {
            try {
                ArrayList<File> waferIdOrderList = getOrder.Order(entry.getValue());
                ArrayList<DataParseIssueBean> dataParseIssueBeans = new ArrayList<>();
                generateRawdata(entry.getKey(),waferIdOrderList,dataParseIssueBeans);
                dataNeedDelete.add(entry.getValue());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return dataNeedDelete;
    }
    public void generateRawdata(WaferInitInformationBean waferInitInformationBean,ArrayList<File> waferIdOrderList,ArrayList<DataParseIssueBean> dataParseIssueBeans) throws Exception{
        RawdataInitBean rawdataInitBean=generateRawdataInitInformation.generateRawdata(waferInitInformationBean,waferIdOrderList);
        boolean checkFlag = generateRawdataTemp.generateTempRawdata(rawdataInitBean, dataParseIssueBeans);
        if (checkFlag) {
            if (dataParseIssueBeans.size() > 0) {
                vtptmtInfor.dataErrorsRecord(dataParseIssueBeans);
            }
        }
    }
}
