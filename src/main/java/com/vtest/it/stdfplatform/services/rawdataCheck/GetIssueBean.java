package com.vtest.it.stdfplatform.services.rawdataCheck;


import com.vtest.it.stdfplatform.pojo.mes.CustomerCodeAndDeviceBean;
import com.vtest.it.stdfplatform.pojo.system.WaferInitInformationBean;
import com.vtest.it.stdfplatform.pojo.vtptmt.DataParseIssueBean;
import com.vtest.it.stdfplatform.services.mes.impl.MesServicesImpl;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

@Service
public class GetIssueBean {
    @Value("${system.properties.stdf.error-path}")
    private String errorPath;
    @Autowired
    private MesServicesImpl getMesInfor;

    public DataParseIssueBean getDataBean(HashMap<String, String> waferInfor, int level, String descripth) {
        DataParseIssueBean dataParseIssueBean = new DataParseIssueBean();
        dataParseIssueBean.setCustomCode(waferInfor.get("customCode"));
        dataParseIssueBean.setDevice(waferInfor.get("device"));
        dataParseIssueBean.setLotId(waferInfor.get("lot"));
        dataParseIssueBean.setCpStep(waferInfor.get("cpStep"));
        dataParseIssueBean.setWaferNo(waferInfor.get("waferNo"));
        dataParseIssueBean.setResource(waferInfor.get("resource"));
        dataParseIssueBean.setIssueType("data Check");
        dataParseIssueBean.setIssuLevel(level);
        dataParseIssueBean.setIssuePath("na");
        dataParseIssueBean.setIssueDescription(descripth);
        dataParseIssueBean.setDealFlag(0);
        return dataParseIssueBean;
    }

    public DataParseIssueBean getDataBeanForException(int level, String description, WaferInitInformationBean waferInitInformationBean, ArrayList<File> waferIdOrderList) throws IOException {
        CustomerCodeAndDeviceBean customerCodeAndDeviceBean = getMesInfor.getCustomerAndDeviceByWaferAndCpStep(waferInitInformationBean.getWaferId(), waferInitInformationBean.getCp());
        HashMap<String, String> waferInfor = new HashMap<>();
        waferInfor.put("customCode", customerCodeAndDeviceBean.getCustomerCode());
        waferInfor.put("device", customerCodeAndDeviceBean.getDevice());
        waferInfor.put("lot", waferInitInformationBean.getLot());
        waferInfor.put("cpStep", waferInitInformationBean.getCp());
        waferInfor.put("waferNo", waferInitInformationBean.getWaferId());
        waferInfor.put("resource", "STDF");
        DataParseIssueBean dataParseIssueBean = getDataBean(waferInfor, level, description);
        if (description.equals("there are error in file coding")) {
            dataParseIssueBean.setIssueType("mapping parse");
        } else {
            dataParseIssueBean.setIssueType("mes information");
        }
        dataParseIssueBean.setIssuePath("files has been deleted,because it was transformed from stdf file");
        try {
            for (File file : waferIdOrderList) {
                FileUtils.forceDelete(file);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return dataParseIssueBean;
    }
}
