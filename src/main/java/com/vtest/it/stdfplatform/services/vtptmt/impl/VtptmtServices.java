package com.vtest.it.stdfplatform.services.vtptmt.impl;

import com.vtest.it.stdfplatform.pojo.vtptmt.DataInforToMesBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class VtptmtServices {
    @Autowired
    private VtptmtInforImpl vtptmtInfor;

    public boolean checkDeviceIfInsetIntoMes(String customCode, String device) {
        ArrayList<DataInforToMesBean> allConfigs = vtptmtInfor.getList();
        for (DataInforToMesBean bean : allConfigs) {
            if (bean.getCustomCode().equals(customCode) && (bean.getDevice().equals("ALL") || bean.getDevice().equals(device))) {
                return true;
            }
        }
        return false;
    }

    public boolean checkDeviceIfGeneratePrimaryYieldReport(String customCode, String device) {
        ArrayList<DataInforToMesBean> allConfigs = vtptmtInfor.getPrimaryTestYieldReportList();
        for (DataInforToMesBean bean : allConfigs) {
            if (bean.getCustomCode().equals(customCode) && (bean.getDevice().equals("ALL") || bean.getDevice().equals(device))) {
                return true;
            }
        }
        return false;
    }

    public boolean checkDeviceIfGenerateSiteInformationReport(String customCode, String device) {
        ArrayList<DataInforToMesBean> allConfigs = vtptmtInfor.getSiteInformationReportList();
        for (DataInforToMesBean bean : allConfigs) {
            if (bean.getCustomCode().equals(customCode) && (bean.getDevice().equals("ALL") || bean.getDevice().equals(device))) {
                return true;
            }
        }
        return false;
    }
}
