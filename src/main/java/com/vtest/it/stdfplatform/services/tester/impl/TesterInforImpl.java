package com.vtest.it.stdfplatform.services.tester.impl;

import com.vtest.it.stdfplatform.dao.tester.TesterDataDao;
import com.vtest.it.stdfplatform.pojo.equipment.EquipmentBean;
import com.vtest.it.stdfplatform.pojo.mes.PrimaryTestYieldBean;
import com.vtest.it.stdfplatform.pojo.rawdataBean.RawdataInitBean;
import com.vtest.it.stdfplatform.pojo.tester.waferYieldBean;
import com.vtest.it.stdfplatform.pojo.vtptmt.BinWaferInforBean;
import com.vtest.it.stdfplatform.services.rawdatatools.GenerateEquipmentInforBean;
import com.vtest.it.stdfplatform.services.rawdatatools.GenerateVtptmtWaferInforBean;
import com.vtest.it.stdfplatform.services.tester.TesterInfor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;

@Service
@Transactional(transactionManager = "testerTransactionManager", propagation = Propagation.REQUIRED, isolation = Isolation.SERIALIZABLE)
public class TesterInforImpl implements TesterInfor {
    @Autowired
    private TesterDataDao testerDataDAO;
    @Autowired
    private GenerateVtptmtWaferInforBean generateWaferInforBean;
    @Autowired
    private GenerateEquipmentInforBean generateEquipmentInforBean;

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

    @Override
    public int insertEquipmentInforToeqCardSummaryHis(EquipmentBean equipmentBean) {
        return testerDataDAO.insertEquipmentInforToeqCardSummaryHis(equipmentBean);
    }

    @Override
    public void singleWaferDeal(String customerCode, String device, String lot, String cp, String waferId, RawdataInitBean rawdataInitBean) {
        String[] passBins = rawdataInitBean.getProperties().get("Pass Bins").split(",");
        ArrayList<Integer> passBinsArray = new ArrayList<>();
        for (int i = 0; i < passBins.length; i++) {
            passBinsArray.add(Integer.valueOf(passBins[i]));
        }
        testerDataDAO.deleteSiteInforToBinInfoSummary(customerCode, device, lot, cp, waferId);
        testerDataDAO.insertSiteInforToBinInfoSummary(customerCode, device, lot, cp, waferId, rawdataInitBean.getPrimarySiteBinSum(), "P", passBinsArray);
        if (rawdataInitBean.getRetestSiteBinSum().size() > 0) {
            testerDataDAO.insertSiteInforToBinInfoSummary(customerCode, device, lot, cp, waferId, rawdataInitBean.getRetestSiteBinSum(), "R", passBinsArray);
        }
        testerDataDAO.insertSiteInforToBinInfoSummary(customerCode, device, lot, cp, waferId, rawdataInitBean.getSiteBinSum(), "F", passBinsArray);
        BinWaferInforBean binWaferInforBean = new BinWaferInforBean();
        generateWaferInforBean.generate(rawdataInitBean, binWaferInforBean);
        testerDataDAO.insertWaferInforToBinWaferSummary(binWaferInforBean);
        EquipmentBean equipmentBean = new EquipmentBean();
        generateEquipmentInforBean.generate(rawdataInitBean, equipmentBean);
        testerDataDAO.insertEquipmentInforToeqCardSummary(equipmentBean);
        testerDataDAO.insertEquipmentInforToeqCardSummaryHis(equipmentBean);
    }

    @Override
    public ArrayList<waferYieldBean> getWaferBinSummaryUnifiedEntrance(String customerCode, String device, String lot, String cp, String waferId, String type) {
        return testerDataDAO.getWaferBinSummaryUnifiedEntrance(customerCode, device, lot, cp, waferId, type);
    }

    @Override
    public ArrayList<PrimaryTestYieldBean> getPrimaryTestYield(String lot, String cpStep) {
        return testerDataDAO.getPrimaryTestYield(lot, cpStep);
    }
}
