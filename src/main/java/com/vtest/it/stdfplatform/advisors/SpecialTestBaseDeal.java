package com.vtest.it.stdfplatform.advisors;

import com.vtest.it.stdfplatform.pojo.rawdataBean.RawdataInitBean;
import com.vtest.it.stdfplatform.services.FailDieCheck.impl.AdjacentFailDieCheck;
import com.vtest.it.stdfplatform.services.mes.MesServices;
import com.vtest.it.stdfplatform.services.parseRawdata.impl.parseRawdata;
import com.vtest.it.stdfplatform.services.urlMesInformation.WaferIdBinSummaryWrite;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@Aspect
@Component
@Order()
public class SpecialTestBaseDeal {
    @Value("${system.properties.stdf.rawdata-backup}")
    private String rawdataBackup;
    @Autowired
    private MesServices mesServices;
    @Autowired
    private AdjacentFailDieCheck adjacentFailDieCheck;
    @Autowired
    private WaferIdBinSummaryWrite waferIdBinSummaryWrite;

    @Before(value = "execution(* generate(..))&&target(com.vtest.it.stdfplatform.services.rawdatatools.GenerateRawdata)&&args(rawdataInitBean)")
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
                    ArrayList<String> passBinsArray = new ArrayList<>();
                    String[] passBins = parseRawdata.getProperties().get("Pass Bins").split(",");
                    for (int i = 0; i < passBins.length; i++) {
                        passBinsArray.add(passBins[i]);
                    }
                    HashMap<String, String> hardBinTestDieMap = parseRawdata.getHardBinTestDie();
                    HashMap<String, String> softBinTestDieMap = parseRawdata.getSoftBinTestDie();
                    HashMap<String, String> siteBinTestDieMap = parseRawdata.getSiteBinTestDieMap();
                    HashMap<String, String> testDieMap = rawdataInitBean.getTestDieMap();

                    for (Map.Entry<String, String> entry : softBinTestDieMap.entrySet()) {
                        if (!passBinsArray.contains(entry.getValue())) {
                            String coordinate = String.format("%4s", entry.getKey().split(":")[1]) + String.format("%4s", entry.getKey().split(":")[0]);
                            String site = siteBinTestDieMap.get(entry.getKey());
                            String softBin = entry.getValue();
                            testDieMap.put(coordinate, String.format("%4s", hardBinTestDieMap.get(entry.getKey())) + String.format("%4s", entry.getValue()) + String.format("%4s", site));
                        }
                    }
                    LinkedHashMap<String, String> properties = parseRawdata.getProperties();
                    rawdataInitBean.getProperties().put("Map Rows", properties.get("Map Rows"));
                    rawdataInitBean.getProperties().put("Map Cols", properties.get("Map Cols"));
                    rawdataInitBean.getProperties().put("MinX", properties.get("left"));
                    rawdataInitBean.getProperties().put("MinY", properties.get("up"));
                    rawdataInitBean.getProperties().put("MaxX", properties.get("right"));
                    rawdataInitBean.getProperties().put("MaxY", properties.get("down"));
                    rawdataInitBean.getProperties().put("TestDieMinX", properties.get("TestDieleft"));
                    rawdataInitBean.getProperties().put("TestDieMinY", properties.get("TestDieup"));
                    rawdataInitBean.getProperties().put("TestDieMaxX", properties.get("TestDieright"));
                    rawdataInitBean.getProperties().put("TestDieMaxY", properties.get("TestDiedown"));
                    adjacentFailDieCheck.perfectDeal(rawdataInitBean);
                    waferIdBinSummaryWrite.write(rawdataInitBean);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
