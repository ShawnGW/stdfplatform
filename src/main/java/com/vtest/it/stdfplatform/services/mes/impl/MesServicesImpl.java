package com.vtest.it.stdfplatform.services.mes.impl;

import com.vtest.it.stdfplatform.dao.mes.MesDao;
import com.vtest.it.stdfplatform.pojo.mes.CustomerCodeAndDeviceBean;
import com.vtest.it.stdfplatform.pojo.mes.MesConfigBean;
import com.vtest.it.stdfplatform.pojo.mes.SlotAndSequenceConfigBean;
import com.vtest.it.stdfplatform.services.mes.MesServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MesServicesImpl implements MesServices {
    @Autowired
    private MesDao mesDao;
    @Override
    public String getWaferIdBySlot(String lot, String slot) {
        return mesDao.getWaferIdBySlot(lot,slot);
    }

    @Override
    public SlotAndSequenceConfigBean getLotSlotConfig(String lot) {
        return mesDao.getLotSlotConfig(lot);
    }

    @Override
    public MesConfigBean getWaferConfigFromMes(String waferId, String cpProcess) {
        return mesDao.getWaferConfigFromMes(waferId, cpProcess);
    }

    @Override
    public CustomerCodeAndDeviceBean getCustomerAndDeviceByLot(String lot) {
        return mesDao.getCustomerAndDeviceByLot(lot);
    }

    @Override
    public CustomerCodeAndDeviceBean getCustomerAndDeviceByWaferAndCpStep(String waferId, String cpStep) {
        return mesDao.getCustomerAndDeviceByWaferAndCpStep(waferId, cpStep);
    }
}
