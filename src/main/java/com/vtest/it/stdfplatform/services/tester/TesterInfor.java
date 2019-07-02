package com.vtest.it.stdfplatform.services.tester;

import com.vtest.it.stdfplatform.pojo.equipment.EquipmentBean;
import com.vtest.it.stdfplatform.pojo.vtptmt.BinWaferInforBean;

import java.util.ArrayList;
import java.util.HashMap;

public interface TesterInfor {
    public int insertSiteInforToBinInfoSummary(String customerCode, String device,String lot,String cp,String waferId,HashMap<Integer, HashMap<Integer, Integer>> siteMap, String testType,ArrayList<Integer> passBins);
    public int deleteSiteInforToBinInfoSummary(String customerCode, String device,String lot,String cp,String waferId);
    public int insertEquipmentInforToeqCardSummary(EquipmentBean equipmentBean);
    public int insertWaferInforToBinWaferSummary(BinWaferInforBean binWaferInforBean);
}
