package com.vtest.it.stdfplatform.advisors;

import com.alibaba.fastjson.JSON;
import com.vtest.it.stdfplatform.pojo.mes.SiteYieldToMes;
import com.vtest.it.stdfplatform.pojo.rawdataBean.RawdataInitBean;
import com.vtest.it.stdfplatform.pojo.vtptmt.BinWaferInforBean;
import com.vtest.it.stdfplatform.services.FailDieCheck.impl.AdjacentFailDieCheck;
import com.vtest.it.stdfplatform.services.mes.MesServices;
import com.vtest.it.stdfplatform.services.rawdatatools.GenerateVtptmtWaferInforBean;
import com.vtest.it.stdfplatform.services.tester.impl.TesterInforImpl;
import com.vtest.it.stdfplatform.services.urlMesInformation.StdfTouchDownWrite;
import com.vtest.it.stdfplatform.services.urlMesInformation.WaferIdBinSummaryWrite;
import com.vtest.it.stdfplatform.services.vtptmt.impl.VtptmtInforImpl;
import com.vtest.it.stdfplatform.services.vtptmt.impl.VtptmtServices;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.*;

@Aspect
@Component
public class MesRecord {
    @Autowired
    private AdjacentFailDieCheck adjacentFailDieCheck;
    @Autowired
    private GenerateVtptmtWaferInforBean generateVtptmtWaferInforBean;
    @Autowired
    private StdfTouchDownWrite stdfTouchDownWrite;
    @Autowired
    private VtptmtInforImpl vtptmtInfor;
    @Autowired
    private VtptmtServices vtptmtServices;
    @Autowired
    private MesServices mesServices;
    @Autowired
    private WaferIdBinSummaryWrite waferIdBinSummaryWrite;
    @Autowired
    private TesterInforImpl testerInfor;

    @Pointcut(value = "execution(* generateTempRawdata(..))&&target(com.vtest.it.stdfplatform.services.rawdatatools.GenerateRawdataTemp)")
    private void perfectRawdataBeanMethod() {
    }

    @AfterReturning(value = "perfectRawdataBeanMethod()&&args(rawdataInitBean,*)", returning = "checkFlag")
    public void record(RawdataInitBean rawdataInitBean, boolean checkFlag) {
        try {
            if (checkFlag && !vtptmtServices.checkDeviceIfInsetIntoMes(rawdataInitBean.getProperties().get("Customer Code"), rawdataInitBean.getProperties().get("Device Name"))) {
                waferIdBinSummaryWrite.write(rawdataInitBean);
                stdfTouchDownWrite.write(rawdataInitBean);

                ArrayList<Integer> osBinArray = new ArrayList<>();
                String[] osBins = rawdataInitBean.getProperties().get("OS Bins").split(",");
                for (int i = 0; i < osBins.length; i++) {
                    osBinArray.add(Integer.valueOf(osBins[i]));
                }
                ArrayList<Integer> passBinArray = new ArrayList<>();
                String[] passBins = rawdataInitBean.getProperties().get("Pass Bins").split(",");
                for (int i = 0; i < passBins.length; i++) {
                    passBinArray.add(Integer.valueOf(passBins[i]));
                }
                SiteYieldToMes siteYieldToMes = new SiteYieldToMes();
                siteYieldToMes.setLot(rawdataInitBean.getProperties().get("Lot ID"));
                siteYieldToMes.setCpStep(rawdataInitBean.getProperties().get("CP Process"));
                siteYieldToMes.setWaferId(rawdataInitBean.getProperties().get("Wafer ID"));
                HashMap<String, String> siteOsAndPassMap = new HashMap<>();
                HashMap<Integer, HashMap<Integer, Integer>> siteBinMap = rawdataInitBean.getSiteBinSum();
                Set<Integer> sites = siteBinMap.keySet();
                for (Integer site : sites) {
                    HashMap<Integer, Integer> binMap = siteBinMap.get(site);
                    Set<Integer> binSet = binMap.keySet();
                    Integer totalBin = 0;
                    Integer totalPassBin = 0;
                    Integer totalOSBin = 0;
                    for (Integer bin : binSet) {
                        Integer binValue = binMap.get(bin);
                        totalBin += binValue;
                        if (passBinArray.contains(bin)) {
                            totalPassBin += binValue;
                        }
                        if (osBinArray.contains(bin)) {
                            totalOSBin += binValue;
                        }
                    }
                    siteOsAndPassMap.put("Site" + site, String.format("%.2f", (double) (totalPassBin * 100) / totalBin) + "," + String.format("%.2f", (double) (totalOSBin * 100) / totalBin));
                }
                siteYieldToMes.setSiteYieldSummary(siteOsAndPassMap);
                HashMap<String, String> siteInfor = new HashMap<>();
                siteInfor.put("infor", JSON.toJSONString(siteYieldToMes));
                mesServices.siteYieldToMes(siteInfor);
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    @AfterReturning(value = "perfectRawdataBeanMethod()&&args(rawdataInitBean,*)", returning = "checkFlag")
    public void adjacentDieCheck(RawdataInitBean rawdataInitBean, boolean checkFlag) {
        if (checkFlag) {
            BinWaferInforBean binWaferInforBean = new BinWaferInforBean();
            generateVtptmtWaferInforBean.generate(rawdataInitBean, binWaferInforBean);
            adjacentFailDieCheck.deal(rawdataInitBean, binWaferInforBean);
            String customerCode = rawdataInitBean.getProperties().get("Customer Code");
            String device = rawdataInitBean.getProperties().get("Device Name");
            String tester = rawdataInitBean.getProperties().get("Tester ID");
            String lot = rawdataInitBean.getProperties().get("Lot ID");
            String cp = rawdataInitBean.getProperties().get("CP Process");
            String waferId = rawdataInitBean.getProperties().get("Wafer ID");
            String[] passBins = rawdataInitBean.getProperties().get("Pass Bins").split(",");
            ArrayList<Integer> passBinsArray = new ArrayList<>();
            for (int i = 0; i < passBins.length; i++) {
                passBinsArray.add(Integer.valueOf(passBins[i]));
            }
            testerInfor.singleWaferDeal(customerCode, device, lot, cp, waferId, rawdataInitBean);
            LinkedHashMap<String, String> properties = rawdataInitBean.getProperties();
            if (tester.equals("NA")) {
                return;
            }
            try {
                String endTime = properties.get("Test End Time").substring(0, 14);
                Date testEndTime = new SimpleDateFormat("yyyyMMddHHmmss").parse(endTime);
                BinWaferInforBean dbOldTesterStatus = vtptmtInfor.getTesterStatusSingle(tester);
                Date dbEendTime = dbOldTesterStatus.getEndTime();
                if (dbEendTime.getTime() > testEndTime.getTime()) {
                    return;
                }
            } catch (Exception e) {

            }
            vtptmtInfor.singleWaferDeal(binWaferInforBean, waferId, cp, tester);
        }
    }
}
