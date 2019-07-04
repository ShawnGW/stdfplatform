package com.vtest.it.stdfplatform.services.mes.impl;

import com.vtest.it.stdfplatform.dao.mes.MesDao;
import com.vtest.it.stdfplatform.pojo.mes.CustomerCodeAndDeviceBean;
import com.vtest.it.stdfplatform.pojo.mes.MesConfigBean;
import com.vtest.it.stdfplatform.pojo.mes.SlotAndSequenceConfigBean;
import com.vtest.it.stdfplatform.services.mes.MesServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;

@Service
@Transactional(isolation = Isolation.READ_UNCOMMITTED,propagation = Propagation.REQUIRED,transactionManager = "mesDataSourceTransactionManager",readOnly = true)
public class MesServicesImpl implements MesServices {
    @Autowired
    private MesDao mesDao;
    @Override
    @Cacheable(cacheNames = "MesInformationCache",key = "#root.methodName+'&'+#lot+'&'+#slot")
    public String getWaferIdBySlot(String lot, String slot) {
        String waferId=mesDao.getWaferIdBySlot(lot,slot);
        return null==waferId?"NA":waferId;
    }

    @Override
    @Cacheable(cacheNames = "MesInformationCache",key = "#root.methodName+'&'+#lot")
    public SlotAndSequenceConfigBean getLotSlotConfig(String lot) {
        SlotAndSequenceConfigBean slotAndSequenceConfigBean= mesDao.getLotSlotConfig(lot);
        SlotAndSequenceConfigBean slotAndSequenceConfigBeanTemp=new SlotAndSequenceConfigBean();
        slotAndSequenceConfigBeanTemp.setGpibBin("0");
        slotAndSequenceConfigBeanTemp.setReadType("OCR");
        slotAndSequenceConfigBeanTemp.setSequence("1-25");
        return null==slotAndSequenceConfigBean?slotAndSequenceConfigBeanTemp:slotAndSequenceConfigBean;
    }

    @Override
    @Cacheable(cacheNames = "MesInformationCache",key = "#root.methodName+'&'+#waferId+'&'+#cpProcess")
    public MesConfigBean getWaferConfigFromMes(String waferId, String cpProcess) {
        MesConfigBean mesConfigBean= mesDao.getWaferConfigFromMes(waferId,cpProcess);
        return null==mesConfigBean?new MesConfigBean():mesConfigBean;
    }

    @Override
    @Cacheable(cacheNames = "MesInformationCache",key = "#root.methodName+'&'+#lot")
    public CustomerCodeAndDeviceBean getCustomerAndDeviceByLot(String lot) {
        CustomerCodeAndDeviceBean customerCodeAndDeviceBean= mesDao.getCustomerAndDeviceByLot(lot);
        return null==customerCodeAndDeviceBean?new CustomerCodeAndDeviceBean():customerCodeAndDeviceBean;
    }

    @Override
    @Cacheable(cacheNames = "MesInformationCache", key = "#root.methodName+'&'+#waferId+'&'+#cpStep")
    public CustomerCodeAndDeviceBean getCustomerAndDeviceByWaferAndCpStep(String waferId, String cpStep) {
        CustomerCodeAndDeviceBean customerCodeAndDeviceBean = mesDao.getCustomerAndDeviceByWaferAndCpStep(waferId, cpStep);
        return null == customerCodeAndDeviceBean ? new CustomerCodeAndDeviceBean() : customerCodeAndDeviceBean;
    }

    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED, transactionManager = "mesDataSourceTransactionManager")
    public void siteYieldToMes(HashMap<String, String> siteInformation) {
        mesDao.siteYieldToMes(siteInformation);
    }

    @Override
    public String getBinDescription(String waferId, String cpProcess) {
        return mesDao.getBinDescription(waferId, cpProcess);
    }

    @Override
    @Cacheable(value = "MesInformationCache", key = "#root.methodName+'&'+#waferId+'&'+#cpProcess")
    public String getPreviousCpStep(String waferId, String cpProcess) {
        String result = mesDao.getPreviousCpStep(waferId, cpProcess);
        return null == result ? "NA" : result;
    }

    @Override
    @Cacheable(value = "MesInformationCache", key = "#root.methodName+'&'+#waferId+'&'+#cpProcess")
    public String getTestBase(String waferId, String cpProcess) {
        String result = mesDao.getTestBase(waferId, cpProcess);
        return null == result ? "FULL-TEST" : result;
    }
}
