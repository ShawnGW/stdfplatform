package com.vtest.it.stdfplatform.datadeal;


import com.vtest.it.stdfplatform.pojo.mes.MesConfigBean;
import com.vtest.it.stdfplatform.pojo.rawdataBean.RawdataInitBean;
import com.vtest.it.stdfplatform.pojo.system.WaferInitInformationBean;
import com.vtest.it.stdfplatform.services.mes.impl.MesServicesImpl;
import com.vtest.it.stdfplatform.services.tools.InitMesConfigToRawdataProperties;
import com.vtest.it.stdfplatform.services.tools.StdfTesterMappingParse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;

@Service
public class GenerateRawdataInitInformation {
    @Autowired
    private StdfTesterMappingParse stdfTesterMappingParse;
    @Autowired
    private InitMesConfigToRawdataProperties initMesConfigToRawdataProperties;
    @Autowired
    private MesServicesImpl getMesInfor;

    public RawdataInitBean generateRawdata(WaferInitInformationBean waferInitInformationBean, ArrayList<File> waferIdOrderList) throws Exception {
        RawdataInitBean rawdataInitBean = new RawdataInitBean();
        try {
            stdfTesterMappingParse.get(waferIdOrderList,rawdataInitBean);
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("there are error in file coding:" + e.getMessage());
        }
        MesConfigBean mesConfigBean = getMesInfor.getWaferConfigFromMes(waferInitInformationBean.getWaferId(), waferInitInformationBean.getCp());
        if (null == mesConfigBean.getInnerLot()) {
            throw new Exception("can't find this wafer in mes system : no such wafer or cpProcess");
        }
        initMesConfigToRawdataProperties.initMesConfig(rawdataInitBean, mesConfigBean);
        return rawdataInitBean;
    }
}
