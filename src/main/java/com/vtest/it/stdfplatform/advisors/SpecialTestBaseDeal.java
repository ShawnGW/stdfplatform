package com.vtest.it.stdfplatform.advisors;

import com.vtest.it.stdfplatform.pojo.rawdataBean.RawdataInitBean;
import com.vtest.it.stdfplatform.services.mes.MesServices;
import com.vtest.it.stdfplatform.services.parseRawdata.impl.parseRawdata;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.HashMap;

@Aspect
@Component
@Order()
public class SpecialTestBaseDeal {
    @Value("${system.properties.stdf.rawdata-backup}")
    private String rawdataBackup;
    @Autowired
    private MesServices mesServices;

    @AfterReturning(value = "execution(* generateRawdata(..))&&target(com.vtest.it.stdfplatform.datadeal.GenerateRawdataInitInformation)", returning = "rawdataInitBean")
    public void mergeFailDies(RawdataInitBean rawdataInitBean) {
        try {
            String waferId = rawdataInitBean.getProperties().get("Wafer ID");
            String cpProcess = rawdataInitBean.getProperties().get("CP Process");
            String testBase = mesServices.getTestBase(waferId, cpProcess);
            if (!testBase.equals("FULL-TEST")) {
                String previousCpProcess = mesServices.getPreviousCpStep(waferId, cpProcess);
                String customer = rawdataInitBean.getProperties().get("Customer Code");
                String device = rawdataInitBean.getProperties().get("Device Name");
                String lot = rawdataInitBean.getProperties().get("Lot ID");
                if (!"NA".equals(previousCpProcess.toUpperCase())) {
                    File previousRawdata = new File(rawdataBackup + "/" + customer + "/" + device + "/" + lot + "/" + previousCpProcess + "/" + waferId + ".raw");
                    parseRawdata parseRawdata = new parseRawdata(previousRawdata);
                    HashMap<String, String> hardBinTestDieMap = parseRawdata.getHardBinTestDie();
                    HashMap<String, String> softBinTestDieMap = parseRawdata.getSoftBinTestDie();
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
