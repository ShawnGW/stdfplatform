package com.vtest.it.stdfplatform.dao.tester;


import com.vtest.it.stdfplatform.pojo.equipment.EquipmentBean;
import com.vtest.it.stdfplatform.pojo.mes.PrimaryTestYieldBean;
import com.vtest.it.stdfplatform.pojo.tester.waferYieldBean;
import com.vtest.it.stdfplatform.pojo.vtptmt.BinWaferInforBean;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;

@Repository
public interface TesterDataDao {
    public int insertSiteInforToBinInfoSummary(@Param("customerCode") String customerCode, @Param("device") String device, @Param("lot") String lot, @Param("cp") String cp, @Param("waferId") String waferId, @Param("siteMap") HashMap<Integer, HashMap<Integer, Integer>> siteMap, @Param("testType") String testType, @Param("passBins") ArrayList<Integer> passBins);

    public int deleteSiteInforToBinInfoSummary(@Param("customerCode") String customerCode, @Param("device") String device, @Param("lot") String lot, @Param("cp") String cp, @Param("waferId") String waferId);

    public int insertEquipmentInforToeqCardSummary(EquipmentBean equipmentBean);

    public int insertWaferInforToBinWaferSummary(BinWaferInforBean binWaferInforBean);

    public ArrayList<waferYieldBean> getWaferBinSummaryUnifiedEntrance(@Param("customerCode") String customerCode, @Param("device") String device, @Param("lot") String lot, @Param("cp") String cp, @Param("waferId") String waferId, @Param("type") String type);

    public ArrayList<PrimaryTestYieldBean> getPrimaryTestYield(@Param("lot") String lot, @Param("cp") String cpStep);
}
