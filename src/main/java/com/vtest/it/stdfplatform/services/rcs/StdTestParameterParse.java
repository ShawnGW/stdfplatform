package com.vtest.it.stdfplatform.services.rcs;


import com.alibaba.fastjson.JSON;
import com.vtest.it.stdfplatform.pojo.rcs.ParameterSpecBean;
import com.vtest.it.stdfplatform.pojo.rcs.PrrBinBean;
import com.vtest.it.stdfplatform.pojo.rcs.PtrParameterBean;
import com.vtest.it.stdfplatform.pojo.rcs.RcsCheckResultBean;
import com.vtest.it.stdfplatform.pojo.system.WaferInitInformationBean;
import com.vtest.it.stdfplatform.services.mes.MesServices;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.*;

/**
 * @author shawn.sun
 * @date 2020/08/20  17:14
 */
@Service
@Slf4j
public class StdTestParameterParse {
    static List<String> parameterList = new ArrayList<>(17);
    static List<String> cpBurnList = new ArrayList<>(17);

    static {
        parameterList.add("FRC_LOTID0");
        parameterList.add("FRC_LOTID1");
        parameterList.add("FRC_LOTID2");
        parameterList.add("FRC_LOTID3");
        parameterList.add("FRC_LOTID4");
        parameterList.add("FRC_LOTID5");
        parameterList.add("FRC_WAFERID");
        parameterList.add("FRC_DIEX");
        parameterList.add("FRC_DIEY");
        parameterList.add("FRC_RESULT");

        parameterList.add("FRC_PASSFLAG_RT_CP");
        parameterList.add("FRC_DIEID_STD_VER");
        parameterList.add("FRC_MR_FLAG");


        cpBurnList.add("CP_Trim_Flag_OTP0_PDOB0");
        cpBurnList.add("CP_Trim_Flag_OTP0_PDOB1");
        cpBurnList.add("CP_Trim_Flag_OTP0_PDOB2");
        cpBurnList.add("CP_Trim_Flag_OTP0_PDOB3");
        cpBurnList.add("CP_Trim_Flag_OTP1_PDOB0");
        cpBurnList.add("CP_Trim_Flag_OTP1_PDOB1");
        cpBurnList.add("CP_Trim_Flag_OTP1_PDOB2");
        cpBurnList.add("CP_Trim_Flag_OTP1_PDOB3");
        cpBurnList.add("CP_Trim_Flag_OTP2_PDOB0");
        cpBurnList.add("CP_Trim_Flag_OTP2_PDOB1");
        cpBurnList.add("CP_Trim_Flag_OTP2_PDOB2");
        cpBurnList.add("CP_Trim_Flag_OTP3_PDOB3");
    }


    private static final String PTR_RECORD = "PTR Record";
    private static final String PRR_RECORD = "PRR Record";
    private static final String PIR_RECORD = "PIR Record";
    private static final String RULE_CODE = "01,02,03,04,05,06,07,08,09,10,";
    private static final String END = "End of file. Done!";
    private MesServices mesService;

    @Autowired
    public void setMesService(MesServices mesService) {
        this.mesService = mesService;
    }

    public void parse(WaferInitInformationBean bean, ArrayList<File> waferIdOrderList) throws FileNotFoundException {
        String lot = bean.getLot();
        String waferId = bean.getWaferId();
        String process = bean.getCp();
//        String passBins = mesService.passBinsByWaferAndProcess(waferId, process);
        String passBins = "1";
        Set<String> passBinSet = new HashSet<>(Arrays.asList(passBins.split(",")));
        Set<String> uidSet = new HashSet<>();
        Set<String> lotSet = new HashSet<>();
        Set<String> waferIdSet = new HashSet<>();
        Set<String> dieSet = new HashSet<>();
        boolean[] dieDuplicateFlag = new boolean[1];
        boolean[] frcResultFlag = new boolean[1];
        boolean[] frcPassRtCpFlag = new boolean[1];
        boolean[] frcDieIdStdVerFlag = new boolean[1];
        boolean[] frcMrFlag = new boolean[1];
        boolean[] cpBurnFlag = new boolean[1];


        int[] sum = new int[2];

        PrintWriter printWriter = new PrintWriter(new File("/server212/Datalog/TempData/DxLog/" + lot + "_" + waferId + "_" + process + ".csv"));
        Map<String, Map<String, PtrParameterBean>> ptrParameterBeanMap = new HashMap<>();
        Map<String, ParameterSpecBean> parameterSpecMap = new HashMap<>();
        List<PrrBinBean> prrBinBeanList = new LinkedList<>();
        PrrBinBean prrBinBean = new PrrBinBean();
        PtrParameterBean ptrParameterBean;

        printWriter.print("site,time,hardBin,softBin,n,partId,");
        for (String s : parameterList) {
            printWriter.print(s + ",");
        }
        printWriter.print("lotId,dieId,dieFullId\r\n");

        for (File file : waferIdOrderList) {
            try {
                FileReader in = new FileReader(file);
                BufferedReader reader = new BufferedReader(in);
                String content;
                boolean touchDownFlag = false;
                while ((content = reader.readLine()) != null) {
                    content = content.trim();
                    boolean recordFlag = (PIR_RECORD.equals(content) || END.equals(content)) && touchDownFlag;
                    if (recordFlag) {
                        touchDownFlag = false;
                        deal(prrBinBeanList, ptrParameterBeanMap, passBinSet, printWriter, parameterList, cpBurnList, uidSet, lotSet, waferIdSet, dieSet, frcResultFlag, frcPassRtCpFlag, frcDieIdStdVerFlag, frcMrFlag, cpBurnFlag, sum, dieDuplicateFlag);
                        continue;
                    }
                    if (PTR_RECORD.equals(content)) {
                        ptrParameterBean = new PtrParameterBean();
                        String testNumber = getVal(reader.readLine().trim());
                        ptrParameterBean.setTestNumber(testNumber);
                        reader.readLine();
                        String site = getVal(reader.readLine());
                        ptrParameterBean.setSite(site);
                        reader.readLine();
                        reader.readLine();
                        String testResult = getVal(reader.readLine().trim());
                        ptrParameterBean.setResult(testResult);
                        String parameter = getVal(reader.readLine().trim());
                        ptrParameterBean.setParameter(parameter);
                        if (!parameterSpecMap.containsKey(parameter)) {
                            reader.readLine();
                            reader.readLine();
                            String lowLimit = getVal(reader.readLine().trim());
                            String highLimit = getVal(reader.readLine().trim());
                            String utils = "na";
                            try {
                                utils = getVal(reader.readLine().trim());
                            } catch (Exception ignored) {
                            }
                            String lowSpec = getVal(reader.readLine().trim());
                            String highSpec = getVal(reader.readLine().trim());
                            ParameterSpecBean parameterSpecBean = new ParameterSpecBean();
                            parameterSpecBean.setParameter(parameter);
                            parameterSpecBean.setTestNumber(ptrParameterBean.getTestNumber());
                            parameterSpecBean.setLowLimit(lowLimit);
                            parameterSpecBean.setHighLimit(highLimit);
                            parameterSpecBean.setUtils(utils);
                            parameterSpecBean.setLowSpec(lowSpec);
                            parameterSpecBean.setHighSpec(highSpec);
                            parameterSpecMap.put(parameter, parameterSpecBean);
                        }
                        if (ptrParameterBeanMap.containsKey(ptrParameterBean.getSite())) {
                            ptrParameterBeanMap.get(ptrParameterBean.getSite()).put(ptrParameterBean.getParameter(), ptrParameterBean);
                        } else {
                            Map<String, PtrParameterBean> map = new HashMap<>();
                            map.put(ptrParameterBean.getParameter(), ptrParameterBean);
                            ptrParameterBeanMap.put(ptrParameterBean.getSite(), map);
                        }
                    }

                    if (PRR_RECORD.equals(content)) {
                        touchDownFlag = true;
                        reader.readLine();
                        String site = getVal(reader.readLine());
                        reader.readLine();
                        String testNumber = getVal(reader.readLine());
                        String hardBin = getVal(reader.readLine());
                        String softBin = getVal(reader.readLine());
                        String coordinateX = getVal(reader.readLine());
                        String coordinateY = getVal(reader.readLine());
                        String testTime = getVal(reader.readLine());
                        String partId = getVal(reader.readLine());
                        prrBinBean.setO(testTime);
                        prrBinBean.setY(coordinateY);
                        prrBinBean.setX(coordinateX);
                        prrBinBean.setS(softBin);
                        prrBinBean.setH(hardBin);
                        prrBinBean.setN(testNumber);
                        prrBinBean.setT(site);
                        prrBinBean.setI(partId);
                        prrBinBeanList.add(prrBinBean);
                        prrBinBean = new PrrBinBean();
                    }
                }
                try {
                    in.close();
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        StringBuilder result = new StringBuilder();

        // DIE ID去重回传pass bin数量与实物pass数量比对
        result.append((sum[0]));
        result.append(",");
        //Pass Die ID重复数量大于20颗需要Hold
        result.append(sum[1] - sum[0]);
        result.append(",");
        //CP_DieID 检查相同VT lot只能出现一个wafer lot
        result.append((lotSet.size() <= 1) ? 1 : 0);
        result.append(",");
        result.append((waferIdSet.size() <= 1) ? 1 : 0);
        result.append(",");
        result.append(dieDuplicateFlag[0] ? "0" : "1");
        result.append(",");
        result.append(frcResultFlag[0] ? "0" : "1");
        result.append(",");
        result.append(frcPassRtCpFlag[0] ? "0" : "1");
        result.append(",");
        result.append(frcDieIdStdVerFlag[0] ? "0" : "1");
        result.append(",");
        result.append(frcMrFlag[0] ? "0" : "1");
        result.append(",");
        result.append(cpBurnFlag[0] ? "0" : "1");
        result.append(",");


        RcsCheckResultBean rcsCheckResultBean = new RcsCheckResultBean();
        rcsCheckResultBean.setRuleCode(RULE_CODE);
        rcsCheckResultBean.setRuleResult(result.toString());
        rcsCheckResultBean.setWaferLot(lot);
        rcsCheckResultBean.setWaferId(waferId);
        rcsCheckResultBean.setStep(process);
        log.info(JSON.toJSONString(rcsCheckResultBean));
        mesService.rcsResultUp(JSON.toJSONString(rcsCheckResultBean));
        printWriter.flush();
        printWriter.close();
    }

    private void deal(List<PrrBinBean> prrBinBeanList, Map<String, Map<String, PtrParameterBean>> ptrParameterBeanMap, Set<String> passBinSet, PrintWriter printWriter, List<String> parameterList, List<String> cpBurnList, Set<String> uidSet, Set<String> lotSet, Set<String> waferIdSet, Set<String> dieSet, boolean[] frcResultFlag, boolean[] frcPassRtCpFlag, boolean[] frcDieIdStdVerFlag, boolean[] frcMrFlag, boolean[] cpBurnFlag, int[] sum, boolean[] dieDuplicateFlag) {

        prrBinBeanList.forEach(e -> e.setMap(ptrParameterBeanMap.get(e.getT())));
        ptrParameterBeanMap.clear();
        prrBinBeanList.forEach(e -> {
            if (!passBinSet.contains(e.getS())) {
                return;
            }
            StringBuilder vLot = new StringBuilder();
            String waferId = "NA";
            StringBuilder dieId = new StringBuilder();
            StringBuilder dieFullId = new StringBuilder();

            printWriter.print(e.getT() + ",");
            printWriter.print(e.getO() + ",");
            printWriter.print(e.getH() + ",");
            printWriter.print(e.getS() + ",");
            printWriter.print(e.getN() + ",");
            printWriter.print(e.getI() + ",");
            Map<String, PtrParameterBean> map = e.getMap();

            try {
                if (!"1".equals(getVal(map.get("FRC_RESULT").getResult()))) {
                    frcResultFlag[0] = true;
                }
            } catch (Exception ignored) {
            }
            try {
                if (!"1".equals(getVal(map.get("FRC_PASSFLAG_RT_CP").getResult()))) {
                    frcPassRtCpFlag[0] = true;
                }
            } catch (Exception ignored) {
            }
            try {
                if (!"3".equals(getVal(map.get("FRC_DIEID_STD_VER").getResult()))) {
                    frcDieIdStdVerFlag[0] = true;
                }
            } catch (Exception ignored) {
            }
            try {
                if (!"0".equals(getVal(map.get("FRC_MR_FLAG").getResult()))) {
                    frcMrFlag[0] = true;
                }
            } catch (Exception ignored) {
            }

            if (!cpBurnFlag[0]) {
                try {
                    for (String par : cpBurnList) {
                        if (!"1".equals(getVal(map.get(par).getResult()))) {
                            cpBurnFlag[0] = true;
                        }
                    }
                } catch (Exception ignored) {

                }
            }

            try {
                String frcLotId0 = getValWithOutPoint(map.get("FRC_LOTID0").getResult());
                String frcLotId1 = getValWithOutPoint(map.get("FRC_LOTID1").getResult());
                String frcLotId2 = getValWithOutPoint(map.get("FRC_LOTID2").getResult());
                String frcLotId3 = getValWithOutPoint(map.get("FRC_LOTID3").getResult());
                String frcLotId4 = getValWithOutPoint(map.get("FRC_LOTID4").getResult());
                String frcLotId5 = getValWithOutPoint(map.get("FRC_LOTID5").getResult());


                String dieX = getValWithOutPoint(map.get("FRC_DIEX").getResult());
                String dieY = getValWithOutPoint(map.get("FRC_DIEY").getResult());

                vLot.append(frcLotId0);
                vLot.append("-");
                vLot.append(frcLotId1);
                vLot.append("-");
                vLot.append(frcLotId2);
                vLot.append("-");
                vLot.append(frcLotId3);
                vLot.append("-");
                vLot.append(frcLotId4);
                vLot.append("-");
                vLot.append(frcLotId5);


                dieId.append(dieX);
                dieId.append("-");
                dieId.append(dieY);

                waferId = getValWithOutPoint(map.get("FRC_WAFERID").getResult());

                dieFullId.append(vLot.toString());
                dieFullId.append("-");
                dieFullId.append(waferId);
                dieFullId.append("-");
                dieFullId.append(dieId.toString());


            } catch (Exception ignored) {
            }

            String vLotStr = vLot.toString();
            String dieIdStr = dieId.toString();
            String dieFullIdStr = dieFullId.toString();
            lotSet.add(vLotStr);
            waferIdSet.add(waferId);

//            if (!lotFlag[0] && !) {
//                lotFlag[0] = true;
//            }
//            if (!waferFlag[0] && !) {
//                waferFlag[0] = true;
//            }
            if (!dieDuplicateFlag[0] && !dieSet.add(dieIdStr)) {
                dieDuplicateFlag[0] = true;
            }

            sum[1]++;
            if (uidSet.add(dieFullIdStr)) {
                sum[0]++;
            }

            parameterList.forEach(par -> {
                if (map.containsKey(par)) {
                    printWriter.print(map.get(par).getResult() + ",");
                } else {
                    printWriter.print("NA,");
                }
            });
            printWriter.print(vLot.toString() + ",");
            printWriter.print(waferId + ",");
            printWriter.print(dieId.toString() + "-,");
            printWriter.print(dieFullId.toString() + "\r\n");
        });
        prrBinBeanList.clear();
    }

    String getVal(String content) {
        return content.trim().split(":")[1].trim();
    }

    String getValWithOutPoint(String regex) {
        if (!regex.contains(".")) {
            return regex;
        }
        return regex.substring(0, regex.indexOf("."));
    }
}
