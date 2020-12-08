package com.vtest.it.stdfplatform.services.mes;

import com.vtest.it.stdfplatform.pojo.mes.CustomerCodeAndDeviceBean;
import com.vtest.it.stdfplatform.pojo.mes.MesConfigBean;
import com.vtest.it.stdfplatform.pojo.mes.SlotAndSequenceConfigBean;

import java.util.HashMap;

public interface MesServices {
    public String getWaferIdBySlot(String lot, String slot);

    public SlotAndSequenceConfigBean getLotSlotConfig(String lot);

    public MesConfigBean getWaferConfigFromMes(String waferId, String cpProcess);

    public CustomerCodeAndDeviceBean getCustomerAndDeviceByLot(String lot);

    public CustomerCodeAndDeviceBean getCustomerAndDeviceByWaferAndCpStep(String waferId, String cpStep);

    public void siteYieldToMes(HashMap<String, String> SiteInfor);

    public String getBinDescription(String waferId, String cpProcess);

    public String getPreviousCpStep(String waferId, String cpProcess);

    public String getTestBase(String waferId, String cpProcess);

    void rcsResultUp(String result);
}
