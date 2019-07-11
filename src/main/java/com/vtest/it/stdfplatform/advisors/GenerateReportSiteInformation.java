package com.vtest.it.stdfplatform.advisors;

import com.vtest.it.stdfplatform.pojo.rawdataBean.RawdataInitBean;
import com.vtest.it.stdfplatform.services.generateReport.SiteInforReport;
import com.vtest.it.stdfplatform.services.vtptmt.impl.VtptmtServices;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class GenerateReportSiteInformation {
    @Autowired
    private SiteInforReport siteInforReport;
    @Autowired
    private VtptmtServices vtptmtServices;

    @AfterReturning(value = "execution(* com.vtest.it.stdfplatform.services.rawdatatools.GenerateRawdataTemp.generateTempRawdata(..))&&args(rawdataInitBean,..)", returning = "flag")
    public void generateReport(RawdataInitBean rawdataInitBean, boolean flag) {
        if (flag) {
            try {
                String customerCode = rawdataInitBean.getProperties().get("Customer Code");
                String device = rawdataInitBean.getProperties().get("Device Name");
                if (flag && vtptmtServices.checkDeviceIfGenerateSiteInformationReport(customerCode, device)) {
                    String lot = rawdataInitBean.getProperties().get("Lot ID");
                    String cpStep = rawdataInitBean.getProperties().get("CP Process");
                    siteInforReport.write(customerCode, device, lot, cpStep, null, null);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
