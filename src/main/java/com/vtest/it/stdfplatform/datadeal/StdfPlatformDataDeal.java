package com.vtest.it.stdfplatform.datadeal;

import com.vtest.it.stdfplatform.pojo.rawdataBean.RawdataInitBean;
import com.vtest.it.stdfplatform.pojo.system.WaferInitInformationBean;
import com.vtest.it.stdfplatform.pojo.vtptmt.DataParseIssueBean;
import com.vtest.it.stdfplatform.services.rawdatatools.GenerateRawdataTemp;
import com.vtest.it.stdfplatform.services.rcs.StdTestParameterParse;
import com.vtest.it.stdfplatform.services.tools.GetOrder;
import com.vtest.it.stdfplatform.services.vtptmt.impl.VtptmtInforImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Service
public class StdfPlatformDataDeal {
    private GetOrder getOrder;

    private GenerateRawdataInitInformation generateRawdataInitInformation;

    private GenerateRawdataTemp generateRawdataTemp;

    private VtptmtInforImpl vtptmtInfor;

    private StdTestParameterParse stdTestParameterParse;

    @Autowired
    public void setGetOrder(GetOrder getOrder) {
        this.getOrder = getOrder;
    }

    @Autowired
    public void setGenerateRawdataInitInformation(GenerateRawdataInitInformation generateRawdataInitInformation) {
        this.generateRawdataInitInformation = generateRawdataInitInformation;
    }

    @Autowired
    public void setGenerateRawdataTemp(GenerateRawdataTemp generateRawdataTemp) {
        this.generateRawdataTemp = generateRawdataTemp;
    }

    @Autowired
    public void setVtptmtInfor(VtptmtInforImpl vtptmtInfor) {
        this.vtptmtInfor = vtptmtInfor;
    }

    @Autowired
    public void setStdTestParameterParse(StdTestParameterParse stdTestParameterParse) {
        this.stdTestParameterParse = stdTestParameterParse;
    }

    public ArrayList<File[]> deal(HashMap<WaferInitInformationBean, File[]> resources) {
        ArrayList<File[]> dataNeedDelete = new ArrayList<>();
        for (Map.Entry<WaferInitInformationBean, File[]> entry : resources.entrySet()) {
            try {
                ArrayList<File> waferIdOrderList = getOrder.Order(entry.getValue());
                try {
                    if ("CDC".equals(entry.getKey().getCustomCode())) {
                        stdTestParameterParse.parse(entry.getKey(), waferIdOrderList);
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                ArrayList<DataParseIssueBean> dataParseIssueBeans = new ArrayList<>();
                generateRawdata(entry.getKey(), waferIdOrderList, dataParseIssueBeans);
                dataNeedDelete.add(entry.getValue());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return dataNeedDelete;
    }

    public void generateRawdata(WaferInitInformationBean waferInitInformationBean, ArrayList<File> waferIdOrderList, ArrayList<DataParseIssueBean> dataParseIssueBeans) throws Exception {
        RawdataInitBean rawdataInitBean = generateRawdataInitInformation.generateRawdata(waferInitInformationBean, waferIdOrderList);
        boolean checkFlag = generateRawdataTemp.generateTempRawdata(rawdataInitBean, dataParseIssueBeans);
        if (checkFlag) {
            if (dataParseIssueBeans.size() > 0) {
                vtptmtInfor.dataErrorsRecord(dataParseIssueBeans);
            }
        }
    }
}
