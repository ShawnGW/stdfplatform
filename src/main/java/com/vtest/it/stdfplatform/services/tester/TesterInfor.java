package com.vtest.it.stdfplatform.services.tester;

import com.vtest.it.stdfplatform.pojo.equipment.EquipmentBean;
import com.vtest.it.stdfplatform.pojo.mes.PrimaryTestYieldBean;
import com.vtest.it.stdfplatform.pojo.rawdataBean.RawdataInitBean;
import com.vtest.it.stdfplatform.pojo.tester.waferYieldBean;
import com.vtest.it.stdfplatform.pojo.vtptmt.BinWaferInforBean;

import java.util.ArrayList;
import java.util.HashMap;

public interface TesterInfor {
    public int insertSiteInforToBinInfoSummary(String customerCode, String device, String lot, String cp, String waferId, HashMap<Integer, HashMap<Integer, Integer>> siteMap, String testType, ArrayList<Integer> passBins);

    public int deleteSiteInforToBinInfoSummary(String customerCode, String device, String lot, String cp, String waferId);

    public int insertEquipmentInforToeqCardSummary(EquipmentBean equipmentBean);

    public int insertWaferInforToBinWaferSummary(BinWaferInforBean binWaferInforBean);

    public int insertEquipmentInforToeqCardSummaryHis(EquipmentBean equipmentBean);

    public void singleWaferDeal(String customerCode, String device, String lot, String cp, String waferId, RawdataInitBean rawdataInitBean);

    public ArrayList<waferYieldBean> getWaferBinSummaryUnifiedEntrance(String customerCode, String device, String lot, String cp, String waferId, String type);

    public ArrayList<PrimaryTestYieldBean> getPrimaryTestYield(String lot, String cpStep);
}
