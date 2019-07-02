package com.vtest.it.stdfplatform.services.tester.impl;

import com.vtest.it.stdfplatform.dao.tester.TesterDataDAO;
import com.vtest.it.stdfplatform.pojo.equipment.EquipmentBean;
import com.vtest.it.stdfplatform.pojo.vtptmt.BinWaferInforBean;
import com.vtest.it.stdfplatform.services.tester.TesterInfor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;

@Service
@Transactional(transactionManager = "testerTransactionManager",propagation = Propagation.REQUIRED,isolation = Isolation.SERIALIZABLE)
public class TesterInforImpl implements TesterInfor {
    @Autowired
    private TesterDataDAO testerDataDAO;
    @Override
    public int insertSiteInforToBinInfoSummary(String customerCode, String device, String lot, String cp, String waferId, HashMap<Integer, HashMap<Integer, Integer>> siteMap, String testType, ArrayList<Integer> passBins) {
        return testerDataDAO.insertSiteInforToBinInfoSummary(customerCode, device, lot, cp, waferId, siteMap, testType, passBins);
    }

    @Override
    public int deleteSiteInforToBinInfoSummary(String customerCode, String device, String lot, String cp, String waferId) {
        return testerDataDAO.deleteSiteInforToBinInfoSummary(customerCode, device, lot, cp, waferId);
    }

    @Override
    public int insertEquipmentInforToeqCardSummary(EquipmentBean equipmentBean) {
        return testerDataDAO.insertEquipmentInforToeqCardSummary(equipmentBean);
    }

    @Override
    public int insertWaferInforToBinWaferSummary(BinWaferInforBean binWaferInforBean) {
        return testerDataDAO.insertWaferInforToBinWaferSummary(binWaferInforBean);
    }
}
